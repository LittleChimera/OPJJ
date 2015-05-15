package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BarChartDemo(BarChart chart, String path) {
		setLocation(200, 100);
		setSize(500, 300);
		setTitle("Bar Chart Demo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI(chart, path);
	}

	private void initGUI(BarChart chart, String path) {
		getContentPane().setLayout(new BorderLayout());
		
		JLabel pathLabel = new JLabel(path);
		pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		getContentPane().add(pathLabel, BorderLayout.PAGE_START);

		BarChartComponent barchart = new BarChartComponent(chart);
		barchart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		getContentPane().add(barchart);
		
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Expected one argument.");
			System.exit(1);
		}
		
		BarChart parseChart = null;
		try {
			parseChart = parseBarChart(Paths.get(args[0]));
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(2);
		}
		
		final BarChart chart = parseChart;
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo(chart, Paths.get(args[0]).toAbsolutePath().toString());
			frame.setVisible(true);
		});
	}

	private static BarChart parseBarChart(Path path) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e1) {
			throw new RuntimeException("File doesn't exist or isn't readable");
		}
		if (lines.size() != 6) {
			throw new RuntimeException("Invalid file data formatting.");
		}
		
		int minY, maxY, spacingY;
		try {
			minY = Integer.valueOf(lines.get(3));
			maxY = Integer.valueOf(lines.get(4));
			spacingY = Integer.valueOf(lines.get(5));			
		} catch (Exception e) {
			throw new RuntimeException("Some numbers can't be parsed from lines 4,5,6.");
		}
		
		String[] xyPairs = lines.get(2).split(" ");
		List<XYValue> values = new LinkedList<XYValue>();
		for (String pair : xyPairs) {
			String[] pairValues = pair.split(",");
			if (pairValues.length != 2) {
				throw new RuntimeException("Invalid xy pair: " + pair);
			}
			try {
				values.add(new XYValue(Integer.valueOf(pairValues[0]), Integer.valueOf(pairValues[1])));				
			} catch (Exception e) {
				throw new RuntimeException("Can't parse pair numbers: " + pair);
			}
		}
		
		return new BarChart(values, lines.get(0), lines.get(1), minY, maxY, spacingY);
	}

}
