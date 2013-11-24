package se.hex6;

public class Light {
	Sphere sphere;
	
	byte r;
	byte g;
	byte b;
	
	int color;

	Light(Vector pos, int r, int c) {

		this.sphere = new Sphere(pos, r, c);

		this.color = c;
		
		r = (byte) (color >> 16);
		g = (byte) (color >> 8);
		b = (byte) (color);
		
	}

	Light(Sphere s, int c) {

		this.sphere = s;

		this.color = c;
	}
}
