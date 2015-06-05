package hr.fer.zemris.java.hw12.jvdraw.colors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * JColorArea is color picker tool. It's a 15x15 square component colored in
 * picked color. Picked color can be get with {@link #getCurrentColor()}.
 * 
 * @author Luka Skugor
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Selected color.
	 */
	private Color color;
	/**
	 * List of listeners which observe color change.
	 */
	private Set<ColorChangeListener> colorListeners;

	/**
	 * Creates a new JColorArea with given initial color and name.
	 * 
	 * @param initColor
	 *            initial color
	 * @param name
	 *            name of the area
	 * @param parent
	 *            top-level container of the GUI where action is instanced
	 */
	public JColorArea(Color initColor, String name, Component parent) {
		colorListeners = new HashSet<ColorChangeListener>();
		color = initColor;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color choosen = JColorChooser.showDialog(parent, "Choose "
						+ name + " color", color);
				if (choosen != null && !choosen.equals(color)) {
					setColor(choosen);
					repaint();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw12.jvdraw.colors.IColorProvider#getCurrentColor()
	 */
	@Override
	public Color getCurrentColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		final Dimension squareDimension = new Dimension(15, 15);
		return squareDimension;
	}

	/**
	 * Adds a color change listener.
	 * @param ccl listener
	 */
	public void addColorChangeListener(ColorChangeListener ccl) {
		colorListeners.add(ccl);
	}

	/**
	 * Removes a color change listener
	 * @param ccl listener
	 */
	public void removeColorChangeListener(ColorChangeListener ccl) {
		colorListeners.remove(ccl);
	}

	/**
	 * Sets current color.
	 * @param color set color
	 */
	private void setColor(Color color) {
		Color old = this.color;
		this.color = color;
		colorListeners.forEach(l -> {
			l.newColorSelected(JColorArea.this, old, color);
		});
	}

	/**
	 * Gets selected RGB color as string in format "({r}, {g}, {b})".
	 * @return current color as string
	 */
	public String rgbToString() {
		return String.format("(%d, %d, %d)", color.getRed(), color.getGreen(),
				color.getBlue());
	}

}
