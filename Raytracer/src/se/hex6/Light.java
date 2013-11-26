package se.hex6;

public class Light {
	Sphere sphere;
	
	byte r;
	byte g;
	byte b;
	
	Vector color;
	float intensity;

	Light(Vector pos, int r, Vector c) {

		this.sphere = new Sphere(pos, r, c);

		this.color = c;
		
//		r = (byte) (color >> 16);
//		g = (byte) (color >> 8);
//		b = (byte) (color);
		
	}

	Light(Sphere s, Vector c) {

		this.sphere = s;

		this.color = c;
	}
}
