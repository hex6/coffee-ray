package se.hex6;

public class Light {
	Sphere sphere;
	int color;

	Light(Vector pos, int r, int c) {

		this.sphere = new Sphere(pos, r);

		this.color = c;
	}

	Light(Sphere s, int c) {

		this.sphere = s;

		this.color = c;
	}
}
