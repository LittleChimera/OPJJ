package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.FullCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class JDrawingCanvas extends JComponent{
	
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	private DrawingModel drawingModel;
	private Point startPoint;

	public JDrawingCanvas(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		
		this.drawingModel.addListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				repaint();
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				repaint();
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				repaint();
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		Dimension size = getSize();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size.width, size.height);
		Graphics gc = g.create();
		for (int i = 0, count = drawingModel.getSize(); i < count; i++) {
			GeometricalObject object = drawingModel.getObject(i);
			object.paint(gc);
		}
		
		gc.dispose();
	}

	public Point getStartPoint() {
		return startPoint;
	}
	
	public void resetStartPoint() {
		startPoint = null;
	}
	
	public void setStartPoint(int x, int y) {
		this.startPoint = new Point(x, y);
	}

	public void paintDrawingObject(GeometricalObject object) {
		Graphics g = getGraphics();
		paint(g);
		object.paint(g);
	}
	

}
