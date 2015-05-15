package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

public class BarChartComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BarChart chart;

	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		setOpaque(true);

		FontMetrics fm = g2d.getFontMetrics();
		final int textPadding = (int) (fm.getHeight() * 0.8);
		int textSpaceHeight = fm.getAscent()
				+ Math.max(fm.getDescent(), textPadding);

		Insets insets = getInsets();

		// reduce chart's space by sizes of x/y-axis names
		Rectangle chartSpace = new Rectangle(getSize());
		chartSpace.x = insets.left + textSpaceHeight;
		chartSpace.y = insets.top;
		chartSpace.height -= (insets.bottom + insets.top + textSpaceHeight);
		chartSpace.width -= (insets.left + insets.right + textSpaceHeight);
		
		// drawing string of x-axis name
		String xAxisName = chart.getxAxisName();
		g2d.drawString(xAxisName,
				chartSpace.x + insets.left + (chartSpace.width - fm.stringWidth(xAxisName))
						/ 2, getSize().height - insets.bottom);

		// drawing vertical string of y-axis name
		String yAxisName = chart.getyAxisName();
		AffineTransform defaultAt = g2d.getTransform();
		AffineTransform at90 = new AffineTransform();
		at90.rotate(-Math.PI / 2);

		g2d.setTransform(at90);
		g2d.drawString(
				yAxisName,
				(insets.top + (chartSpace.height - fm.stringWidth(yAxisName)) / 2)
						- getSize().height, fm.getAscent() + insets.left);
		// reset rotation
		g2d.setTransform(defaultAt);

		drawChartGrid(g2d, chartSpace);

		g2d.dispose();
	}

	private void drawChartGrid(Graphics2D g2d, Rectangle chartSpace) {
		FontMetrics fm = g2d.getFontMetrics();
		
		// calculate maximum string width of all values on y-axis
		int verticalValuesWidth = 0;
		int ySpacing = chart.getSpacingY();
		for (int y = chart.getMinY(), maxY = chart.getMaxY(); y <= maxY; y += ySpacing) {
			verticalValuesWidth = Math.max(verticalValuesWidth,
					fm.stringWidth(Integer.toString(y)));
		}
		
		// make space for value numbers and grid overflow
		final int gridOverflow = 5;
		final int valuesPadding = (int) (fm.getAscent() * 0.5 + fm.getDescent());
		int horizontalValuesTotalHeight = fm.getAscent() + valuesPadding;
		chartSpace.height -= (gridOverflow + horizontalValuesTotalHeight);
		int verticalValuesTotalWidth = verticalValuesWidth + valuesPadding;
		chartSpace.width -= (gridOverflow + verticalValuesTotalWidth);
		chartSpace.x += verticalValuesTotalWidth;

		final int lineDistance = chartSpace.width / chart.maxX(); //chartValues.size();
		
		Color gridColor = new Color(180, 80, 60, 100);
		g2d.setColor(gridColor);
		
		// draw y-axis grid
		int valueCounter = 1;
		int valueSpacing = 1;
		for (int x = chartSpace.x, maxWidth = chartSpace.width + chartSpace.x; x <= maxWidth; x += lineDistance) {
			
			g2d.setColor(gridColor);
			g2d.drawLine(x, 0 + chartSpace.y - gridOverflow, x,
					chartSpace.height + chartSpace.y);
			g2d.setColor(Color.BLACK);
			String valueString = Integer.toString(valueCounter);
			g2d.drawString(valueString, x + (lineDistance - fm.stringWidth(valueString))/2, chartSpace.y + chartSpace.height + horizontalValuesTotalHeight);
			valueCounter += valueSpacing;
		}

		int spacingCount = (int) ((chart.getMaxY() - chart.getMinY()) / (double) chart
				.getSpacingY());
		//distance of x-lines
		double spacing = chartSpace.height / (double) spacingCount;
		
		valueCounter = chart.getMinY() + ((chart.getMaxY() - chart.getMinY())/ chart.getSpacingY()) * chart.getSpacingY();
		valueSpacing = chart.getSpacingY();
		// draw x-axis grid
		for (double y = chartSpace.y, maxHeight = chartSpace.height
				+ chartSpace.y; y <= maxHeight + 1; y += spacing) {
			
			g2d.setColor(gridColor);
			int yi = (int) y;
			g2d.drawLine(0 + chartSpace.x, yi, chartSpace.width + chartSpace.x
					+ gridOverflow, yi);
			
			g2d.setColor(Color.BLACK);
			String valueString = Integer.toString(valueCounter);
			g2d.drawString(valueString, chartSpace.x - verticalValuesTotalWidth, yi + fm.getAscent()/2);
			valueCounter -= valueSpacing;
		}

		drawBars(g2d, chartSpace, spacing);
	}

	private void drawBars(Graphics2D g2d, Rectangle chartSpace,
			double cellHeight) {
		Dimension size = chartSpace.getSize();

		final int maxBarHeight = chart.getMaxY() - chart.getMinY();
		final int barWidth = size.width / chart.maxX();
		final int minYValue = chart.getMinY();

		for (XYValue value : chart.getValues()) {
			
			// bar height calculated with number of cells and single cell height
			int barHeight = (int) (cellHeight * (Math.min(maxBarHeight,
					value.getY() - minYValue) / (double) chart.getSpacingY()));

			Rectangle bar = new Rectangle((value.getX() - 1) * barWidth
					+ chartSpace.x, size.height - barHeight + chartSpace.y,
					barWidth - 1, barHeight);
			
			// casting shadow
			final int shadowOffset = (int) (0.05 * Math.min(barWidth,
					size.height));
			g2d.setColor(new Color(150, 150, 150, 150));
			g2d.fillRect(bar.x + shadowOffset, bar.y + shadowOffset, bar.width,
					bar.height - shadowOffset);
			
			// drawing bar
			g2d.setColor(new Color(220, 120, 80));
			g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
		}

	}

}
