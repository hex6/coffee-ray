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

	int cameraPos;

	final int MAX_REFLECTIONS = 1;

	public Sphere[] spheres = {
			new Sphere(new Vector(300, 300, 0), 50, 0x00FFFFFF),
			new Sphere(new Vector(100, 100, 0), 50, 0x000000FF),
			new Sphere(new Vector(500, 500, 0), 50, 0x00FFFFFF)
			};
	
	public Light[] lights = {
			new Light(new Vector(500, 300, 0), 100, 0x00FFFFFF)
	};

	// public float vectordist;

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
				byte r, g, b;
				int reflections = 0;

				Vector hitVector;

				Ray currentRay = new Ray(new Vector(x, y, cameraPos),
						new Vector(0, 0, 1));

				Sphere sphere = null;
				float minDistance = 10000.0f;
				float distance;

				// float depthLimit = 1;

				while (reflections < MAX_REFLECTIONS) {

					// Create a ray originating from the screen
					// going straight forward to negative-z space

					for (Sphere s : spheres) {							if(color > 0x00FFFFFF)
						System.out.println(color);

						// Check if the ray hits a sphere
						distance = intersecting(currentRay, s);
						if (distance > 0 && distance < minDistance) {

							sphere = s;
							minDistance = distance;
						}
					}
					
					if (sphere == null) {
						break;
					}

					// hitVector = Vector.add(ray.position,
					// ray.direction.scale(minDistance));

					hitVector = new Vector(x, y, (minDistance));

					// color += sphere.color;

					// Vector normal = Vector.sub(hitVector,
					// sphere.center).scale(1/Vector.sub(hitVector,
					// sphere.center).magnitude());

					Vector normal = Vector.sub(hitVector, sphere.center)
							.normalize();

					// Vector normal = new Vector(x, y, minDistance);

					Vector refVec = Vector.sub(currentRay.direction,
							normal.scale(2 * Vector.dot(normal,
									currentRay.direction)));

					currentRay = new Ray(hitVector, refVec);

					// r = sphere.r;
					// r = sphere.r;
					// g = sphere.g;
					// b = sphere.b;

					for (Light l : lights) {
						Vector dir = Vector.sub(l.sphere.center, hitVector)
								.normalize();

						Boolean inShadow = false;

						Ray shadowRay = new Ray(hitVector, dir);

						for (Sphere s : spheres) {

							if (intersecting(shadowRay, s) != 0 && s != sphere) {
								inShadow = true;
								break;
							}

						}

						if (!inShadow) {
							float lambert = Vector.dot(shadowRay.direction,
									normal);

							if (lambert < 0)
								continue;

							// color = (int) (0x00FFFFFF*lambert);
							// color = sphere.color;
							// color = (int) (sphere.color*lambert);

							r = sphere.r;
							g = sphere.g;
							b = sphere.b;

							r &= (byte) (0xFF * lambert);
							g &= (byte) (0xFF * lambert);
							b &= (byte) (0xFF * lambert);

							color += (r << 16) + (g << 8) + (b);
							// color = r << 16 | g << 8 | b;

						}

						// distance = intersecting(currentRay, l.sphere);
						// if (distance > 0) {
						//
						// // hitVector = Vector.add(currentRay.position,
						// currentRay.direction.normalize());
						// Vector reflection = Vector.add(currentRay.position,
						// currentRay.direction.scale(distance));
						//
						// Vector centerDir = Vector.sub(l.sphere.center,
						// hitVector);
						// Vector centerDist = Vector.sub(centerDir,
						// reflection);
						//
						// // float intensity =
						// Vector.dot(hitVector.normalize(),normal.normalize())*sphere.color;
						// float intensity = centerDir.magnitude() /
						// l.sphere.radius;
						//
						// color = (int) (0xFF * intensity)&0xFF;
						// // color = (int) intensity;
						// color = (color << 16) | (color << 8) | (color);
						//
						// }

					}

					// color = color << 16 + color << 8 + color;

					reflections++;
				}

				// ray = reflection;
				renderer.setPixel(x, y, color);

			}
		}

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
		/* t = (v�d)� - (v�+r�) */
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
		new Raytracer(600, 600);

	}

}
