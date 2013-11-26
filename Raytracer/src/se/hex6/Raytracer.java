package se.hex6;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Raytracer {

    public Frame frame;

    Renderer renderer;

    public int height;
    public int width;

    public boolean running = true;

    final int MAX_REFLECTIONS = 1;
    final float MIN_DISTANCE = 20000.0f;
    final float OFFSET = 1e-4f;

    // Creates new Spheres with the parameters (Position, radius, color,
    // transparency, reflectivity)
    // public Sphere[] spheres = {
    // new Sphere(new Vector(400, 300, 0), 8, new Vector(1, 1, 1), 0f, 0f),
    // new Sphere(new Vector(100, 100, 0), 8, new Vector(1, 1, 1), 0f, 0f),
    // new Sphere(new Vector(600, 500, 0), 8, new Vector(1, 1, 1), 0f, 0f)
    // };
    public Sphere[] spheres = {
	    new Sphere(new Vector(0, -10000, -20), 10000, new Vector(0.2f), 0f, 0f),
	    new Sphere(new Vector(0, 0, -25), 4, new Vector(1.0f, 0.32f, 0.36f), 0f, 0.01f),
	    new Sphere(new Vector(5, -1, -20), 2, new Vector(0.90f, 0.76f, 0.46f), 0f, 0f),
	    new Sphere(new Vector(5, 0, -30), 3, new Vector(0.65f, 0.77f, 0.97f), 0f, 0f),
	    new Sphere(new Vector(-5.5f, 0, -20), 3, new Vector(0.90f, 0.90f, 0.90f), 0f, 0f) };

    public Light[] lights = {
    // new Light(new Vector(600, 300, 0), 1, new Vector(1, 1, 1)),
    new Light(new Vector(0, 20, -30), 3, new Vector(1, 1, 1)) };

    public Raytracer(int w, int h) {

	width = w;
	height = h;

	frame = new Frame("Coffee Ray");
	frame.setSize(width + 16, height + 39);

	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	renderer = new Renderer(width, height);

	frame.add(renderer);

	height = h;
	width = w;

	castRays();
	renderer.repaint();

	while (running) {

	}

    }

    public Vector trace(Ray currentRay, int reflections) {

	float minDistance = MIN_DISTANCE;

	Sphere sphere = null;

	Vector hitVector;

	for (Sphere s : spheres) {

	    // Check if the ray hits a sphere
	    float distance = intersect(currentRay, s);

	    if (distance > 0 && distance < minDistance) {
		minDistance = distance;
		sphere = s;
	    }
	}

	if (sphere == null) {
	    return Vector.zero();

	}

	Vector color = Vector.zero();

	// Point of intersection

	hitVector = Vector.add(currentRay.position,
		currentRay.direction.scale(minDistance));

	// Normal at intersection point
	Vector normal = Vector.sub(hitVector, sphere.center).normalize();
	if (reflections < MAX_REFLECTIONS
		&& (sphere.transparency > 0 || sphere.reflectivity > 0)) {
	    


	    Vector reflectDir = Vector.sub(currentRay.direction,
		    normal.scale(2 * Vector.dot(currentRay.direction, normal)))
		    .normalize();

	    Vector reflection = trace(new Ray(Vector.add(hitVector, normal.scale(OFFSET)),
		    reflectDir), reflections + 1);

	    // if the sphere is transparent make a refraction calculation
	   
	

	    color = reflection;
	    



	} else {

	    for (Light l : lights) {

		Vector lightDir = Vector.sub(l.sphere.center, hitVector)
			.normalize();

		Boolean inShadow = false;

		Ray shadowRay = new Ray(hitVector, lightDir);
//		Ray shadowRay = new Ray(Vector.add(hitVector, normal.scale(BIAS)), lightDir);

		for (Sphere s : spheres) {

		    if (intersect(shadowRay, s) != 0) {
			inShadow = true;
			break;
		    }

		}

		if (!inShadow) {
		    float lambert = Vector.dot(normal, shadowRay.direction);
		    if (lambert < 0)
			continue;

		    color = Vector.mult(sphere.color, l.color.scale(lambert));
		}

	    }

	}

	if (color.x > 1)
	    color.x = 1;
	if (color.y > 1)
	    color.y = 1;
	if (color.z > 1)
	    color.z = 1;

	return color;
    }

    public void castRays() {

	// Vector camera = new Vector(400, 300, -500);

	float invWidth = 1 / (float) width;
	float invHeight = 1 / (float) height;

	float fov = 60.0f;

	float aspectratio = (float) width / (float) height;

	float angle = (float) Math.tan(Math.PI * 0.5 * fov / 180.0f);

	// Loop through all the coordinates/pixels
	// and cast a ray straight forward
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {

		// Create a ray originating from the screen
		// going straight forward to negative-z space

		// Ray ray = new Ray(camera, Vector.sub(new Vector(x, y, 10),
		// camera));

		float xx = (2 * ((x + 0.5f) * invWidth) - 1) * angle
			* aspectratio;
		float yy = (1 - 2 * ((y + 0.5f) * invHeight)) * angle;

		Vector rayDir = new Vector(xx, yy, -1).normalize();

		Ray ray = new Ray(Vector.zero(), rayDir);

		Vector color = trace(ray, 0);

		int rgb = ((int) (color.x * 255) << 16
			| (int) (color.y * 255) << 8 | (int) (color.z * 255) & 0xFF);
		renderer.setPixel(x, y, rgb);

	    }
	}

	System.out.println("Done!");
    }

    public float mix(float a, float b, float mix) {

	return b * mix + a * (1 - mix);
    }

    public float intersect(Ray ray, Sphere sph) {

	float t;
	Vector d = ray.direction;
	float r = sph.radius;
	// v = s - c
	// where s is the direction of the ray
	// and c is the center of the sphere
	Vector v = Vector.sub(sph.center, ray.position);

	float vd = Vector.dot(v, d);
	if (vd < 0)
	    return 0;

	/* t = (v*d)^2 - (v^2+r^2) */
	// t = vd * vd - Vector.dot(v, v) + r * r;
	t = Vector.dot(v, v) - vd * vd;
	if (t > r * r)
	    return 0;
	t = (float) Math.sqrt(r * r - t);

	if (t < 0)
	    return 0;

	t = (float) Math.sqrt(t);

	float t1 = Vector.dot(v, d) + t;
	float t2 = Vector.dot(v, v) - t;

	if (t1 > 0.0f && t1 <= t2) {
	    return t1;
	} else if (t2 > 0.0f) {
	    return t2;
	}

	return 0;

	// t = Math.min(t1, t2);
	// return t;

    }

    // public float intersect(Ray ray, Sphere sph) {
    //
    // float t;
    // Vector d = ray.direction;
    // float r = sph.radius;
    // // v = s - c
    // // where s is the direction of the ray
    // // and c is the center of the sphere
    // Vector v = Vector.sub(ray.position, sph.center);
    //
    // float vd = Vector.dot(v, d);
    //
    // /* t = (v*d)^2 - (v^2+r^2) */
    // t = vd * vd - Vector.dot(v, v) + r * r;
    //
    // if (t < 0)
    // return 0;
    //
    // t = (float) Math.sqrt(t);
    //
    // float t1 = Vector.dot(v, d) + t;
    // float t2 = Vector.dot(v, v) - t;
    //
    // if (t1 > 0.0f && t1 <= t2) {
    // return t1;
    // } else if (t2 > 0.0f) {
    // return t2;
    // }
    //
    // return 0;
    //
    // // t = Math.min(t1, t2);
    // // return t;
    //
    // }

    public static void main(String[] args) {
	new Raytracer(800, 600);

    }

}
