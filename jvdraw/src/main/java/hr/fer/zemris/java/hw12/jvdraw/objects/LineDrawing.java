package hr.fer.zemris.java.hw12.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;

public class LineDrawing extends GeometricalObject {
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public LineDrawing(int x1, int y1, int x2, int y2, Color color) {
		super(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1-x2), Math.abs(y1-y2), color, null);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void paint(Graphics g) {
		
		g.setColor(fillColor);
		g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public String getSaveFormat() {
		return x1 + " " + y1 + " " + x2 + " " + y2 + " " + outlineColor.toString();
	}

}
