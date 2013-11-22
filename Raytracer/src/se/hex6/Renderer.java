package se.hex6;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Renderer extends Canvas{



	private int width;
	private int height;
	
	public Frame frame;
	
//	public Canvas canvas;
	
	public BufferedImage image;
	Graphics g;
	

	public Renderer(int w, int h){
		width = w;
		height = h;
		createWindow();
		
		
		
	}
	public void createWindow() {

		

		
//		canvas = new Canvas();
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		

//		tracer = new Raytracer(frame.getWidth(), frame.getHeight());



	}
	
	public void setPixel(int x, int y, int rgb){
		image.setRGB(x, y, rgb);
	}

	@Override
	public void paint(Graphics g){
		
//		BufferStrategy bs = this.getBufferStrategy();
//		image.setRGB(100, 100, 255);
		g.drawImage(image, 0, 0, width, height, null);
		
	}
	
	



}
