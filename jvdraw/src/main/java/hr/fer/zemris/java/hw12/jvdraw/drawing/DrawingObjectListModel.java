package hr.fer.zemris.java.hw12.jvdraw.drawing;

import java.awt.Graphics;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import javax.swing.AbstractListModel;

public class DrawingObjectListModel extends
		AbstractListModel<GeometricalObject> {

	private DrawingModel model;

	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				fireIntervalRemoved(source, index0, index1);
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				fireContentsChanged(source, index0, index1);
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				fireIntervalAdded(source, index0, index1);
			}
		});
	}
	
	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}
	
}
