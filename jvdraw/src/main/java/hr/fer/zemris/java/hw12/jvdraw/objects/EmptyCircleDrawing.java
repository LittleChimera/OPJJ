package hr.fer.zemris.java.hw12.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EmptyCircleDrawing extends GeometricalObject {
	
	private int x;
	private int y;
	private int r;
	
	public EmptyCircleDrawing(int x, int y, int r, Color outline) {
		super(x - r, y - r, 2*r, 2*r, null, outline);
		this.x = x;
		this.y = y;
		this.r = r;
	}

	@Override
	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		
		g.setColor(outlineColor);
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public String getSaveFormat() {
		return x + " " + y + " " + r + " " + outlineColor.toString();
	}

}
