package se.hex6;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Raytracer {

	public Frame frame;

	Renderer renderer;

	public int height;
	public int width;

	int cameraPos;

	public boolean running = true;

	public Sphere[] spheres;
	public Light[] lights;

	public float vectordist;

	public Raytracer(int w, int h) {

		width = w;
		height = h;

		cameraPos = -10;

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

		createTestObjects();

		castRays();
		renderer.repaint();

		while (running) {

		}

	}

	public void castRays() {

		// Loop through all the coordinates/pixels
		// and cast a ray straight forward
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int color = 0;
				int reflections = 0;

				while (reflections < 1) {

					// Create a ray originating from the screen
					// going straight forward to negative-z space
					Ray ray = new Ray(new Vector(x, y, cameraPos), new Vector(0, 0, 1));

					Vector hitVector;
					Ray reflection = null;

					for (Sphere s : spheres) {
						// Check if the ray hits a sphere
						if (intersecting(ray, s)) {

							hitVector = Vector.add(ray.position, ray.direction.scale(vectordist));

							// Vector hitVector = new Vector(x,y,vectordist);

							Vector normal = Vector.sub(hitVector, spheres[0].center).scale(
									hitVector.magnitude() - spheres[0].center.magnitude());

							// Vector normal = new
							// Vector(x,y,vectordist).normalize();

							Vector refVec = Vector.sub(ray.direction, normal.scale(2 * Vector.dot(normal, ray.direction)));

							reflection = new Ray(hitVector, refVec);
							// color = 0x00FFFFFF;
						}
					}
					

					if (reflection == null) {
						break;
					}

					if (intersecting(reflection, lights[0].sphere)) {

						hitVector = Vector.add(reflection.position, reflection.direction.normalize());

						Vector centerDist = Vector.sub(lights[0].sphere.center, hitVector);

						float intensity = centerDist.magnitude() /  lights[0].sphere.radius;


						color = (int) (0xFF * intensity)&0xFF;
						color = (color << 16) + (color << 8) + (color);
						
					}
					
					if (intersecting(ray, lights[0].sphere)) {
						 color = 0x00FFFFFF;		
					}

					// color = color << 16 + color << 8 + color;
					renderer.setPixel(x, y, color);
					reflections++;
				}

			}
		}

	}

	public boolean intersecting(Ray ray, Sphere sph) {

		float t;
		Vector d = ray.direction;
		float r = sph.radius;
		// v = s - c
		// where s is the direction of the ray
		// and c is the center of the sphere
		Vector v = Vector.sub(ray.position, sph.center);

		t = Vector.dot(v, d) * Vector.dot(v, d) - Vector.dot(v, v) + r * r;

		// System.out.println(t);

		if (t < 0)
			return false;

		t = (float) Math.sqrt(t);

		float t1 = Vector.dot(v, d) + t;
		float t2 = Vector.dot(v, v) - t;

		t = Math.min(t1, t2);
		vectordist = t;
		return true;
	}

	// public float

	public void createTestObjects() {

		spheres = new Sphere[2];

		spheres[0] = new Sphere(new Vector(300, 300, 0), 50);
		spheres[1] = new Sphere(new Vector(100, 100, 0), 50);

		lights = new Light[2];

		lights[0] = new Light(new Vector(500, 300, 0), 100, 0x00FFFFFF);
		lights[1] = new Light(new Vector(500, 600, 0), 100, 0x00FFFFFF);

	}

	public static void main(String[] args) {
		new Raytracer(600, 600);

	}

}
