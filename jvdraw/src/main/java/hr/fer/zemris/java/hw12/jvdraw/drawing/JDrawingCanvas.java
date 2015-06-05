package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Drawing canvas for drawing {@link GeometricalObject}s. All drawn objects are
 * pulled from a {@link DrawingModel}.
 * 
 * @author Luka Skugor
 *
 */
public class JDrawingCanvas extends JComponent {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Drawing model from which objects are pulled.
	 */
	private DrawingModel drawingModel;
	/**
	 * Starting point of a started drawing.
	 */
	private Point startPoint;
	/**
	 * Cached image.
	 */
	private BufferedImage cache;
	/**
	 * Listener which observers changes on {@link #drawingModel}.
	 */
	private DrawingModelListener listener;

	/**
	 * Creates a new JDrawingCanvas with given {@link DrawingModel}.
	 * @param drawingModel drawing model which objects are drawn
	 */
	public JDrawingCanvas(DrawingModel drawingModel) {
		setDrawingModel(drawingModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		if (cache == null
				|| (cache.getWidth() != getWidth() || cache.getHeight() != getHeight())) {
			updateCache();
		}

		g.drawImage(cache, 0, 0, Color.WHITE, null);

	}

	/**
	 * Updates cached image.
	 */
	private void updateCache() {
		Dimension size = getSize();

		cache = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics gc = cache.getGraphics();

		gc.setColor(Color.WHITE);
		gc.fillRect(0, 0, size.width, size.height);

		for (int i = 0, count = drawingModel.getSize(); i < count; i++) {
			GeometricalObject object = drawingModel.getObject(i);
			object.paint(gc);
		}
		gc.dispose();
	}

	/**
	 * Gets starting point of a creating drawing.
	 * @return starting point of a creating drawing
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Resets starting point of creating drawing.
	 */
	public void resetStartPoint() {
		startPoint = null;
	}

	/**
	 * Sets starting point of creating drawing.
	 * @param x x-coordinate of starting point
	 * @param y y-coordinate of starting point
	 */
	public void setStartPoint(int x, int y) {
		this.startPoint = new Point(x, y);
	}

	/**
	 * Temporarily paints currently drawing object on top of other objects.
	 * @param object currently drawing object
	 */
	public void paintDrawingObject(GeometricalObject object) {
		Graphics g = getGraphics();
		g.drawImage(cache, 0, 0, Color.WHITE, null);
		object.paint(g);
	}

	/**
	 * Sets drawing model from which objects are drawn.
	 * @param drawingModel set drawing model
	 */
	public void setDrawingModel(DrawingModel drawingModel) {
		cache = null;
		if (listener != null) {
			this.drawingModel.removeListener(listener);
		}
		this.drawingModel = drawingModel;
		addListenerToModel();
	}

	/**
	 * Adds a listener to the model.
	 */
	private void addListenerToModel() {
		
		listener = new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0,
					int index1) {
				updateCache();
				repaint();
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0,
					int index1) {
				updateCache();
				repaint();
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				updateCache();
				repaint();
			}
		};

		drawingModel.addListener(listener);
	}

}
