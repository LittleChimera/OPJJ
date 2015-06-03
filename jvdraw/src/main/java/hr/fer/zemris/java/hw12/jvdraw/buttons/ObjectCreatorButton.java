package hr.fer.zemris.java.hw12.jvdraw.buttons;

import java.awt.Color;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import javax.swing.JButton;
import javax.swing.JToggleButton;

public abstract class ObjectCreatorButton extends JToggleButton {

	public abstract GeometricalObject createObject(int x, int y, int width, int height,
			Color bg, Color fg);

}
