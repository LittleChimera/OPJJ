package hr.fer.zemris.java.hw12.jvdraw.objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * GeometricalObject represents a drawable geometric object. It can also be
 * saved as JVF format.
 * 
 * @author Luka Skugor
 *
 */
public abstract class GeometricalObject extends JComponent {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Filling color of the object.
	 */
	protected Color fillColor;
	/**
	 * Outline color of the object.
	 */
	protected Color outlineColor;

	/**
	 * Creates a new GeometricalObject for given bounding box and object's colors.
	 * @param x x-coordinate of the starting point of the bounding box
	 * @param y y-coordinate of the starting point of the bounding box
	 * @param width width of the bounding box
	 * @param height height of the bounding box
	 * @param fill filling color of the object
	 * @param outline outline color of the object
	 */
	public GeometricalObject(int x, int y, int width, int height, Color fill,
			Color outline) {
		changeObject(x, y, width, height, fill, outline);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public abstract void paint(Graphics g);

	/**
	 * Gets JVD save format.
	 * @return JVD save format
	 */
	public abstract String getSaveFormat();

	/**
	 * Converts color to save format as "{r} {g} {b}"
	 * @param c color to convert
	 * @return color's save format
	 */
	protected String rgbToSaveFormat(Color c) {
		return String.format("%d %d %d", c.getRed(), c.getGreen(), c.getBlue());
	}

	/**
	 * Changes parameters of the object.
	 * @param x x-coordinate of the starting point of the bounding box
	 * @param y y-coordinate of the starting point of the bounding box
	 * @param width width of the bounding box
	 * @param height height of the bounding box
	 * @param fill filling color of the object
	 * @param outline outline color of the object
	 */
	protected void changeObject(int x, int y, int width, int height,
			Color fill, Color outline) {
		setBounds(x, y, width, height);
		fillColor = fill;
		outlineColor = outline;
	}

	/**
	 * Shows a dialog for changing object's parameters.
	 * @param parent top-level container of the GUI where object is instanced
	 */
	public abstract void showChangeDialog(Component parent);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#getName()
	 */
	@Override
	public abstract String getName();

}
