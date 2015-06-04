package hr.fer.zemris.java.hw12.jvdraw.colors;

import hr.fer.zemris.java.hw12.jvdraw.JVDraw;

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

public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private Color color;
	private Set<ColorChangeListener> colorListeners;
	
	public JColorArea(Color initColor, String name, Component parent) {
		colorListeners = new HashSet<ColorChangeListener>();
		color = initColor;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color choosen = JColorChooser.showDialog(parent, "Choose "
						+ name + " color", color);
				if (choosen != color) {
					setColor(choosen);
					repaint();
				}
			}
		});
	}
	

	public Color getCurrentColor() {
		return color;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public Dimension getPreferredSize() {
		final Dimension squareDimension = new Dimension(15, 15);
		return squareDimension;
	}
	
	public void addColorChangeListener(ColorChangeListener ccl) {
		colorListeners.add(ccl);
	}
	
	public void removeColorChangeListener(ColorChangeListener ccl) {
		colorListeners.remove(ccl);
	}
	
	public void setColor(Color color) {
		colorListeners.forEach(l -> {
			l.newColorSelected(JColorArea.this, this.color, color);
		});
		this.color = color;
	}
	
	public String rgbToString() {
		return String.format("color: (%d, %d, %d)", color.getRed(), color.getGreen(),
				color.getBlue());
	}

}
