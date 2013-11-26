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
	
	
//	public Canvas canvas;
	
	public BufferedImage image;

	public Renderer(int w, int h){
		width = w;
		height = h;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		
		
		
	}
	
	public void setPixel(int x, int y, int rgb){
		image.setRGB(x, y, rgb);
	}

	@Override
	public void paint(Graphics g){
		
		g.drawImage(image, 0, 0, width, height, null);
		
	}
	
	



}
