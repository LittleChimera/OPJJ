package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 * DrawingObjectListModel is a {@link ListModel} which stores
 * {@link GeometricalObject}s. It pulls data from a {@link DrawingModel}.
 * 
 * @author Luka Skugor
 *
 */
public class DrawingObjectListModel extends
		AbstractListModel<GeometricalObject> {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * {@link DrawingModel} from which data is pulled.
	 */
	private DrawingModel model;

	/**
	 * Creates a new DrawingObjectListModel with given model from which data is
	 * pulled.
	 * 
	 * @param model
	 *            model from which data is pulled
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addListener(new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0,
					int index1) {
				fireIntervalRemoved(source, index0, index1);
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0,
					int index1) {
				fireContentsChanged(source, index0, index1);
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				fireIntervalAdded(source, index0, index1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return model.getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

}
