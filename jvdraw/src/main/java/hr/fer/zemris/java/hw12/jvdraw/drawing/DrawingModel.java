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

/**
 * DrawingModel models a list of drawing objects (i.e. {@link GeometricalObject}
 * s). Order of elements is preserved when adding elements so that last added
 * drawing will be above all others.
 * 
 * @author Luka Skugor
 *
 */
public class DrawingModel {

	/**
	 * List of drawings in the model.
	 */
	private List<GeometricalObject> drawings;
	/**
	 * Set of model's listeners.
	 */
	private Set<DrawingModelListener> listeners;

	/**
	 * Creates a new DrawingModel.
	 */
	public DrawingModel() {
		drawings = new LinkedList<GeometricalObject>();
		listeners = new HashSet<DrawingModelListener>();
	}

	/**
	 * Get number of drawings in the model.
	 * 
	 * @return number of drawings
	 */
	public int getSize() {
		return drawings.size();
	}

	/**
	 * Adds drawing to the model.
	 * 
	 * @param object
	 *            geometric object
	 */
	public void addObject(GeometricalObject object) {
		drawings.add(object);
		int size = getSize();
		listeners.forEach(l -> {
			l.objectsAdded(this, size, size);
		});
	}

	/**
	 * Gets drawing from the model at given index.
	 * 
	 * @param index
	 *            index at which drawing has been added
	 * @return drawing at given index
	 */
	public GeometricalObject getObject(int index) {
		if (index >= getSize() || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return drawings.get(index);
	}

	/**
	 * Adds listener to the model.
	 * 
	 * @param dml
	 *            lister
	 */
	public void addListener(DrawingModelListener dml) {
		listeners.add(dml);
	}

	/**
	 * Removes listener from the model
	 * 
	 * @param dml
	 *            listener
	 */
	public void removeListener(DrawingModelListener dml) {
		listeners.remove(dml);
	}

	/**
	 * Notifies all observers that of the given model that given range of
	 * elements has been modified.
	 * 
	 * @param source
	 *            model source
	 * @param index0
	 *            starting index of the range (inclusive)
	 * @param index1
	 *            ending index of the range (inclusive)
	 */
	public static void fireObjectsChanged(DrawingModel source, int index0,
			int index1) {
		source.listeners.forEach(l -> {
			l.objectsChanged(source, index0, index1);
		});
	}

	/**
	 * Gets model as JVD format.
	 * 
	 * @return JVD format of the model
	 */
	public String getAsJvd() {
		StringBuilder jvdBuilder = new StringBuilder();
		for (GeometricalObject geometricalObject : drawings) {
			jvdBuilder.append(geometricalObject.getSaveFormat()).append("\n");
		}

		return jvdBuilder.toString();
	}

	/**
	 * Constructs a model from a JVD format.
	 * 
	 * @param lines
	 *            lines of JVD format
	 * @return created model
	 */
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

		return model;
	}

	/**
	 * Changes models drawing to the drawings of given model. Listeners stay the
	 * same.
	 * 
	 * @param model
	 *            set model
	 */
	public void setModel(DrawingModel model) {
		this.drawings = new LinkedList<GeometricalObject>(model.drawings);
		fireObjectsChanged(this, 0, drawings.size() - 1);
	}
}
