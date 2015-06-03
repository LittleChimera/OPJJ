package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class JDrawingCanvas extends JComponent{
	
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	private DrawingModel drawingModel;

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
		Rectangle bounds = getBounds();
		g.setColor(Color.WHITE);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		Graphics gc = g.create();
		for (int i = 0, count = drawingModel.getSize(); i < count; i++) {
			GeometricalObject object = drawingModel.getObject(i);
			object.paint(gc);
		}
		
		gc.dispose();
	}
	

}
