package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DrawingModel {

	private List<GeometricalObject> drawings;
	private Set<DrawingModelListener> listeners;
	
	public DrawingModel() {
		drawings = new LinkedList<GeometricalObject>();
		listeners = new HashSet<DrawingModelListener>();
	}
	
	public int getSize() {
		return drawings.size();
	}
	
	public void addObject(GeometricalObject object) {
		drawings.add(object);
		int size = getSize();
		listeners.forEach(l -> {
			l.objectsAdded(this, size, size);
		});
	}
	
	public GeometricalObject getObject(int index) {
		if (index >= getSize() || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return drawings.get(index);
	}
	
	public void addListener(DrawingModelListener dml) {
		listeners.add(dml);
	}
	
	public void removeListener(DrawingModelListener dml) {
		listeners.remove(dml);
	}
}