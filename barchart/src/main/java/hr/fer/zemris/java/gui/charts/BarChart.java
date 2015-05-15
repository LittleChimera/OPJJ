package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.List;

public class BarChart {

	private List<XYValue> values;
	private String xAxisName;
	private String yAxisName;
	private int minY;
	private int maxY;
	private int spacingY;

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
	
	public int getMaxY() {
		return maxY;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public int getSpacingY() {
		return spacingY;
	}
	
	public List<XYValue> getValues() {
		return Collections.unmodifiableList(values);
	}
	
	public String getxAxisName() {
		return xAxisName;
	}
	
	public String getyAxisName() {
		return yAxisName;
	}

	public int maxX() {
		return values.stream().max((xy1, xy2) -> xy1.getX() - xy2.getX()).get().getX();
	}

}
