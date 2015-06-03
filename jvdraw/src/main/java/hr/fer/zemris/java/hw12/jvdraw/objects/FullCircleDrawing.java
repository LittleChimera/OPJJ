package hr.fer.zemris.java.hw12.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class FullCircleDrawing extends GeometricalObject {
	
	public FullCircleDrawing(int x, int y, int r, Color fill, Color outline) {
		super(x - r, y - r, 2*r, 2*r, fill, outline);
	}

	@Override
	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		g.setColor(fillColor);
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		
		g.setColor(outlineColor);
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
