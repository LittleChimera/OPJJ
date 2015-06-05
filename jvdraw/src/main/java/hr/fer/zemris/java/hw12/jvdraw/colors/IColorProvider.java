package hr.fer.zemris.java.hw12.jvdraw.colors;

import java.awt.Color;

/**
 * Provides a {@link Color}. Currently active color can be get with {@link #getCurrentColor()}.
 * 
 * @author Luka Skugor
 *
 */
public interface IColorProvider {

	/**
	 * Gets currently active color.
	 * @return currently active color
	 */
	public Color getCurrentColor();

}
