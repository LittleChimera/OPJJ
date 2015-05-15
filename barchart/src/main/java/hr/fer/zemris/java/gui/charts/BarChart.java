package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.List;

/**
 * BarChart holds information of a discrete chart. Held values are axis names,
 * range of y-axis values which should be displayed, spacing between y-axis
 * values and pairs of (x, x-value) for each x on x-axis.
 * 
 * @author Luka Skugor
 *
 */
public class BarChart {

	/**
	 * List of value pairs (x, x-value).
	 */
	private List<XYValue> values;
	/**
	 * x-axis name.
	 */
	private String xAxisName;
	/**
	 * y-axis name.
	 */
	private String yAxisName;
	/**
	 * Minimum y coordinate which is displayed.
	 */
	private int minY;
	/**
	 * Maximum y coordinate which is displayed.
	 */
	private int maxY;
	/**
	 * Spacing between y-axis values.
	 */
	private int spacingY;

	/**
	 * Creates a new BarChart with given information.
	 * 
	 * @param values
	 *            list of value pairs (x, x-value)
	 * @param xAxisName
	 *            x-axis name
	 * @param yAxisName
	 *            y-axis name
	 * @param minY
	 *            minimum y coordinate which is displayed
	 * @param maxY
	 *            maximum y coordinate which is displayed
	 * @param spacingY
	 *            spacing between y-axis values
	 */
	public BarChart(List<XYValue> values, String xAxisName, String yAxisName,
			int minY, int maxY, int spacingY) {
		this.values = values;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.minY = Math.min(minY, maxY);
		this.maxY = Math.max(minY, maxY);
		if (spacingY <= 0) {
			throw new IllegalArgumentException("Spacing needs to be positive");
		}
		this.spacingY = spacingY;
	}

	/**
	 * Gets maximum y-coordinate which is displayed.
	 * 
	 * @return maximum displayed y-coordinate.
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Gets minimum y-coordinate which is displayed.
	 * 
	 * @return minimum displayed y-coordinate.
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets spacing between y-axis values.
	 * 
	 * @return spacing between y-axis value
	 */
	public int getSpacingY() {
		return spacingY;
	}

	/**
	 * Gets unmodifiable list of chart values pairs (x, x-value).
	 * 
	 * @return unmodifiable list of chart values pairs
	 */
	public List<XYValue> getValues() {
		return Collections.unmodifiableList(values);
	}

	/**
	 * Gets x-axis name.
	 * 
	 * @return x-axis name
	 */
	public String getxAxisName() {
		return xAxisName;
	}

	/**
	 * Gets y-axis name.
	 * 
	 * @return y-axis name
	 */
	public String getyAxisName() {
		return yAxisName;
	}

	/**
	 * Gets maximum x-value on x-axis.
	 * 
	 * @return maximum x-value on x-axis
	 */
	public int maxX() {
		return values.stream().max((xy1, xy2) -> xy1.getX() - xy2.getX()).get()
				.getX();
	}

}
