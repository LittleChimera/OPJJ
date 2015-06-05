package hr.fer.zemris.java.hw12.jvdraw.buttons;

import java.awt.Color;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import javax.swing.JToggleButton;

/**
 * ObjectCreatorButton is a button which can create {@link GeometricalObject}s.
 * 
 * @author Luka Skugor
 *
 */
public abstract class ObjectCreatorButton extends JToggleButton {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link GeometricalObject} for the given parameters.
	 * 
	 * @param x
	 *            x-coordinate of the starting point of the bounding box
	 * @param y
	 *            y-coordinate of the starting point of the bounding box
	 * @param width
	 *            width of the bounding box
	 * @param height
	 *            height of the bounding box
	 * @param bg
	 *            background color of the object
	 * @param fg
	 *            foreground color of the object
	 * @return created object
	 */
	public abstract GeometricalObject createObject(int x, int y, int width,
			int height, Color bg, Color fg);

}
