package hr.fer.zemris.java.hw12.jvdraw.drawing;

import hr.fer.zemris.java.hw12.jvdraw.objects.EmptyCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.FullCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw12.jvdraw.objects.LineDrawing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DrawingModel {

	private boolean modified = false;

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
		modified = true;
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

	public static void fireObjectsChanged(DrawingModel source, int index0,
			int index1) {
		source.listeners.forEach(l -> {
			l.objectsChanged(source, index0, index1);
			source.modified = true;
		});
	}

	public void save() {
		modified = false;
	}

	public String getAsJvd() {
		StringBuilder jvdBuilder = new StringBuilder();
		for (GeometricalObject geometricalObject : drawings) {
			jvdBuilder.append(geometricalObject.getSaveFormat()).append("\n");
		}

		return jvdBuilder.toString();
	}

	public static DrawingModel fromJvdFormat(List<String> lines) {
		List<GeometricalObject> objects = new ArrayList<GeometricalObject>(
				lines.size());
		for (String line : lines) {
			switch (line.split(" ", 2)[0]) {
			case "CIRCLE":
				objects.add(EmptyCircleDrawing.fromJvd(line));
				break;
				
			case "FCIRCLE":
				objects.add(FullCircleDrawing.fromJvd(line));
				break;

			case "LINE":
				objects.add(LineDrawing.fromJvd(line));
				break;
			}
		}
		
		DrawingModel model = new DrawingModel();
		for (GeometricalObject geometricalObject : objects) {
			model.addObject(geometricalObject);
		}
		model.modified = false;
		
		return model;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModel(DrawingModel model) {
		this.drawings = model.drawings;
		this.modified = false;
		fireObjectsChanged(this, 0, drawings.size() - 1);
	}
}
