package se.hex6;

public class Vector {

	float x;
	float y;
	float z;
	
	public Vector(float xPos,float yPos,float zPos){
		this.x = xPos;
		this.y = yPos;
		this.z = zPos;
	}
	
	public static Vector zero(){
		return new Vector(0, 0 ,0);
	}
	
	public static Vector add(Vector v1, Vector v2){
		
		return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		
	}
	
	public static Vector sub(Vector v1, Vector v2){
		
		return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		
	}
	
	public Vector scale(float factor){
		
		return new Vector(x*factor, y*factor, z*factor);
	}
	
	public static Vector cross(Vector v1, Vector v2){
		
//		| x |   | v.x |
//		| y | x | v.y |
//		| z |   | v.z |
		
		return new Vector(
				v1.y*v2.z - v1.z*v2.y,
				v1.z*v2.x - v1.x*v2.z,
				v1.x*v2.y - v1.y*v2.x
				);
		
	}
	
	public Vector normalize(){
		
		float len = 1/this.magnitude();
		
		return this.scale(len);
	}
	public static float dot(Vector v1, Vector v2){
		
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	
	
	public float magnitude(){
		
		return (float) Math.sqrt((x*x + y*y + z*z));
	}
}
