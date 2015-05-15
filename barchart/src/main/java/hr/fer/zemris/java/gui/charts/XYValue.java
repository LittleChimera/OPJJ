package hr.fer.zemris.java.gui.charts;

/**
 * This class represents a pair of unmodifiable integers which indicate a
 * discrete location on a map.
 * 
 * @author Luka Skugor
 *
 */
public class XYValue {

	/**
	 * x-axis value.
	 */
	private int x;
	/**
	 * y-axis value.
	 */
	private int y;

	/**
	 * Creates new XYValue for given integers.
	 * 
	 * @param x
	 *            x-axis value
	 * @param y
	 *            y-axis value
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets x-axis value.
	 * 
	 * @return x-axis value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets y-axis value.
	 * 
	 * @return y-axis value
	 */
	public int getY() {
		return y;
	}

}
