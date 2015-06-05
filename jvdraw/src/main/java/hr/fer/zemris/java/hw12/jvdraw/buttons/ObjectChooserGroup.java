package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 * Group of buttons which are used for selecting a drawing tool. They're
 * mutually exclusive so only one tool can be selected at a time.
 * 
 * @author Luka Skugor
 *
 */
public class ObjectChooserGroup extends ButtonGroup {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Selected creator button.
	 */
	private ObjectCreatorButton creator;

	/**
	 * Creates a new ObjectChooserGroup from given creator buttons. Creator
	 * buttons are used for selecting drawing tool. First creator in the array
	 * is set as selected tool.
	 * 
	 * @param creators
	 *            creator buttons
	 */
	public ObjectChooserGroup(ObjectCreatorButton... creators) {
		if (creators.length < 1) {
			throw new IllegalArgumentException(
					"Excepected at least one graphical object creator.");
		}

		creator = creators[0];
		creator.setSelected(true);

		for (ObjectCreatorButton objectCreator : creators) {
			super.add(objectCreator);
			objectCreator.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					creator = objectCreator;
				}
			});
		}
	}

	/**
	 * Creates a {@link GeometricalObject} using currently selected creator
	 * button (i.e. tool).
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
	public GeometricalObject createSelectedObject(int x, int y, int width,
			int height, Color bg, Color fg) {
		return creator.createObject(x, y, width, height, bg, fg);
	}

	/**
	 * Operation not supported. ObjectChooserGroup can only contain
	 * {@link ObjectCreatorButton}s which are given through constructor.
	 * 
	 * @throws UnsupportedOperationException
	 *             because operation is not supported
	 */
	@Override
	public void add(AbstractButton b) {
		throw new UnsupportedOperationException();
	}

}
