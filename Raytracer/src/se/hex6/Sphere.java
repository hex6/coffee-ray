package se.hex6;

public class Sphere {

	// Position
	public Vector center;
	public float radius;

	// Color & material properties
	public Vector color;
	public float transparency;
	public float reflectivity;

	public byte r;
	public byte g;
	public byte b;

	public Sphere(Vector pos, float r, Vector c) {

		center = pos;
		radius = r;
		color = c;
		transparency = 0;
		reflectivity = 0;

	}

	public Sphere(Vector pos, float r, Vector c, float t, float refl) {

		center = pos;
		radius = r;

		color = c;
		transparency = t;
		reflectivity = refl;

	}
	

}
