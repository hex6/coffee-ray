package se.hex6;

public class Sphere {
	
	
	public Vector center;
	public float radius;
	
	public int color;
	
	public byte r;
	public byte g;
	public byte b;
	
	public Sphere (Vector pos, float r, int c){
		
		center = pos;
		radius = r;
		color = c;
		
		this.r = (byte) (color >> 16);
		this.g = (byte) (color >> 8);
		this.b = (byte) (color);
		
	}
	
}
