package hr.fer.zemris.java.hw12.jvdraw.colors;

import java.awt.Color;

/**
 * ColorChangeListener is a listener which is notified when color of the source
 * {@link IColorProvider} changes.
 * 
 * @author Luka Skugor
 *
 */
public interface ColorChangeListener {

	/**
	 * Notifies the listener that color has been changed.
	 * 
	 * @param source
	 *            color provider which changed color
	 * @param oldColor
	 *            old color
	 * @param newColor
	 *            new color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor);

}
