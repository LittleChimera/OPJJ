package hr.fer.zemris.java.hw12.jvdraw.drawing;

/**
 * DrawingModelListener represents a listener of the {@link DrawingModel}. It
 * performs actions when {@link DrawingModel} has been modified.
 * 
 * @author Luka Skugor
 *
 */
public interface DrawingModelListener {

	/**
	 * This method is called when objects are added to a {@link DrawingModel}.
	 * 
	 * @param source
	 *            model which has been modified
	 * @param index0
	 *            modification starting index (inclusive)
	 * @param index1
	 *            modification ending index (inclusive)
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * This method is called when objects are removed from a
	 * {@link DrawingModel}.
	 * 
	 * @param source
	 *            model which has been modified
	 * @param index0
	 *            modification starting index (inclusive)
	 * @param index1
	 *            modification ending index (inclusive)
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * This method is called when objects are changed in a {@link DrawingModel}.
	 * 
	 * @param source
	 *            model which has been modified
	 * @param index0
	 *            modification starting index (inclusive)
	 * @param index1
	 *            modification ending index (inclusive)
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
