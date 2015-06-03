package hr.fer.zemris.java.hw12.jvdraw.colors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private Color color;
	private Set<ColorChangeListener> colorListeners;
	
	public JColorArea(Color initColor) {
		colorListeners = new HashSet<ColorChangeListener>();
		color = initColor;
		addMouseListener(new MouseAdapter() {
			
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
	

}
