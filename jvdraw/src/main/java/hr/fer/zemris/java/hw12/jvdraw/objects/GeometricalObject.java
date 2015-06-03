package hr.fer.zemris.java.hw12.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public abstract class GeometricalObject extends JComponent {
	
	protected Color fillColor;
	protected Color outlineColor;
	
	public GeometricalObject(int x, int y, int width, int height, Color fill, Color outline) {
		setBounds(x, y, width, height);
		fillColor = fill;
		outlineColor = outline;
	}
	
	@Override
	public abstract void paint(Graphics g);
	
}
