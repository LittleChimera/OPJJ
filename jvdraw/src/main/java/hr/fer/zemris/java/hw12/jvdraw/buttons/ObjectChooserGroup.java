package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class ObjectChooserGroup extends ButtonGroup {

	private ObjectCreatorButton creator;

	public ObjectChooserGroup(ObjectCreatorButton... creators) {
		if (creators.length < 1) {
			throw new IllegalArgumentException("Excepected at least one graphical object creator.");
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

	public GeometricalObject createSelectedObject(int x, int y, int width,
			int height, Color bg, Color fg) {
		return creator.createObject(x, y, width, height, bg, fg);
	}

	/**
	 * Operation not supported.
	 * 
	 * @throws UnsupportedOperationException
	 *             because operation is not supported
	 */
	@Override
	public void add(AbstractButton b) {
		throw new UnsupportedOperationException();
	}

}
