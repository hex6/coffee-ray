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
	
	public Raytracer(int h, int w) {

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

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int color = 0;

				Ray ray = new Ray(new Vector(x, y, cameraPos), new Vector(0, 0,
						1));

				if (intersecting(ray, spheres[0])) {
					renderer.setPixel(x, y, 0x00FFFFFF);
				} else {
					renderer.setPixel(x, y, 0x00000000);
				}

				// color = color << 16 + color << 8 + color;

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

		
		
		//System.out.println(t);
		
		
		if (t < 0)
			return false;

		t = (float) Math.sqrt(t);

		float t1 = Vector.dot(v, d) + t;
		float t2 = Vector.dot(v, v) - t;

		t = Math.min(t1, t2);
		return true;
	}

	public void createTestObjects() {

		spheres = new Sphere[1];

		spheres[0] = new Sphere(new Vector(150,150,0), 10);

		lights = new Light[1];

		lights[0] = new Light(new Vector(10, 0, 0), 1, 0x00FFFFFF);

	}

	public static void main(String[] args) {
		new Raytracer(300, 300);

	}

}
