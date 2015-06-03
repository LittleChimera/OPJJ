package hr.fer.zemris.java.hw12.jvdraw.colors;

import java.awt.Color;

public interface ColorChangeListener {

	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
	
}
