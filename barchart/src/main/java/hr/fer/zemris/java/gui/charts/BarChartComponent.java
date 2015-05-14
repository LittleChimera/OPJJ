package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;

public class BarChartComponent extends JComponent {

	private BarChart chart;

	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		setOpaque(true);
		Insets insets = getInsets();

		FontMetrics fm = g2d.getFontMetrics();
		final int textPadding = (int) (fm.getHeight() * 0.4);
		// int paddingBottom =

		Rectangle chartSpace = new Rectangle(getSize());
		chartSpace.x = (int) (0.1 * chartSpace.width);
		chartSpace.y = (int) (0.1 * chartSpace.height);
		chartSpace.height *= 0.8;
		chartSpace.width *= 0.8;

		drawChartGrid(g2d, chartSpace);

		g2d.dispose();
	}

	private void drawChartGrid(Graphics2D g2d, Rectangle chartSpace) {
		List<XYValue> chartValues = chart.getValues();

		Dimension size = chartSpace.getSize();
		final int lineDistance = size.width / chartValues.size();

		g2d.setColor(new Color(180, 80, 60, 100));

		final int lineOverflow = 5;

		for (int x = chartSpace.x, maxWidth = chartSpace.width + chartSpace.x;
				x <= maxWidth; x += lineDistance) {
			//int x = (value.getX() - 1) * lineDistance + chartSpace.x;
			g2d.drawLine(x, 0 + chartSpace.y - lineOverflow, x, size.height
					+ chartSpace.y);
		}

		int spacingCount = (int) ((chart.getMaxY() - chart.getMinY()) / (double) chart
				.getSpacingY());
		double spacing = size.height / (double) spacingCount;

		for (double y = chartSpace.y, maxHeight = chartSpace.height
				+ chartSpace.y; y <= maxHeight + 2; y += spacing) {
			
			int yi = (int) y;
			g2d.drawLine(0 + chartSpace.x, yi, size.width + chartSpace.x
					+ lineOverflow, yi);
		}

		drawBars(g2d, chartSpace, spacing);
	}

	private void drawBars(Graphics2D g2d, Rectangle chartSpace,
			double cellHeight) {
		Dimension size = chartSpace.getSize();

		final int maxBarHeight = chart.getMaxY() - chart.getMinY();
		final int barWidth = size.width / chart.getValues().size();
		final int minYValue = chart.getMinY();

		for (XYValue value : chart.getValues()) {

			int barHeight = (int) (cellHeight * (Math.min(maxBarHeight,
					value.getY() - minYValue) / (double) chart.getSpacingY()));

			Rectangle bar = new Rectangle((value.getX() - 1) * barWidth
					+ chartSpace.x, size.height - barHeight + chartSpace.y,
					barWidth - 1, barHeight);

			final int shadowOffset = (int) (0.05 * Math.min(barWidth,
					size.height));
			g2d.setColor(new Color(150, 150, 150, 150));
			g2d.fillRect(bar.x + shadowOffset, bar.y + shadowOffset, bar.width,
					bar.height - shadowOffset);

			g2d.setColor(new Color(220, 120, 80));
			g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
		}

	}

}
