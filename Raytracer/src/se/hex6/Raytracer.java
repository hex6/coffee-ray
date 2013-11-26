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
	final float MIN_DISTANCE = 10000.0f;
	final float BIAS = 1e-4f;
	
	//Creates new Spheres with the parameters (Position, radius, color, transparency, reflectivity)
	public Sphere[] spheres = { 
			new Sphere(new Vector(400, 300, 0), 8, new Vector(1, 1, 1), 0f, 0f),
			new Sphere(new Vector(100, 100, 0), 8, new Vector(1, 1, 1), 0f, 0f),
			new Sphere(new Vector(600, 500, 0), 8, new Vector(1, 1, 1), 0f, 0f) 
	};

	
	public Light[] lights = { 
			new Light(new Vector(400, 50, 100), 1, new Vector(1, 1, 1)),
//			new Light(new Vector(100, 300, 0), 100, new Vector(1, 1, 1)) 
	};

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

		Vector color = Vector.zero();

		for (Sphere s : spheres) {

			// Check if the ray hits a sphere
			float distance = intersecting(currentRay, s);

			if (distance > 0 && distance < minDistance) {
				minDistance = distance;
				sphere = s;
			}
		}

		if (sphere == null) {
			return Vector.zero();
			
		}



		// Point of intersection
		
	 hitVector = Vector.add(currentRay.position,
	 currentRay.direction.scale(minDistance));

		// Normal at intersection point
		Vector normal = Vector.sub(hitVector, sphere.center).normalize();


			
			for (Light l : lights) {
				
				
				Vector lightDir = Vector.sub(l.sphere.center, hitVector).normalize();

				Boolean inShadow = false;

				Ray shadowRay = new Ray(hitVector, lightDir);

				for (Sphere s : spheres) {
					
					
					if (intersecting(shadowRay, s) != 0) {
						inShadow = true;
						break;
					}

				}

				if (!inShadow) {
					float lambert = -Vector.dot(normal, shadowRay.direction);
					if (lambert < 0) continue;
					
					color = Vector.add(color, (l.color.scale(lambert)));
				}

			}
			
//		}

		if(color.x > 1) color.x = 1;
		if(color.y > 1) color.y = 1;
		if(color.z > 1) color.z = 1;
		
		return color;
	}

	public void castRays() {

		
		

		
		
		Vector camera = new Vector(400,300, -500);
		
		
		// Loop through all the coordinates/pixels
		// and cast a ray straight forward
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {


				// Create a ray originating from the screen
				// going straight forward to negative-z space

				
				
				Ray ray = new Ray(camera, Vector.sub(new Vector(x,y, 10), camera));

				Vector color = trace(ray, 0);
				
				int rgb = ((int)(color.x*255) << 16 | (int)(color.y*255) << 8 | (int)(color.z*255)&0xFF);
				renderer.setPixel(x, y, rgb);

				
			}
		}
		
		System.out.println("Done!");
	}

	public float mix(float a, float b, float mix) {

		return b * mix + a * (1 - mix);
	}

	public float intersecting(Ray ray, Sphere sph) {

		float t;
		Vector d = ray.direction;
		float r = sph.radius;
		// v = s - c
		// where s is the direction of the ray
		// and c is the center of the sphere
		Vector v = Vector.sub(ray.position, sph.center);

		float vd = Vector.dot(v, d);

		/* t = (v*d)^2 - (v^2+r^2) */
		t = vd * vd - Vector.dot(v, v) + r * r;

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

	public static void main(String[] args) {
		new Raytracer(800, 600);

	}

}
