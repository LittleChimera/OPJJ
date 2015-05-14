package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame {

	public BarChartDemo() {
		setLocation(200, 100);
		setSize(500, 300);
		setTitle("Bar Chart Demo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		JLabel pathLabel = new JLabel("no file");
		pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		getContentPane().add(pathLabel, BorderLayout.PAGE_START);

		getContentPane().add(
				new BarChartComponent(new BarChart(Arrays.asList(new XYValue(1,
						8), new XYValue(2, 20), new XYValue(3, 22),
						new XYValue(4, 10), new XYValue(5, 4)),
						"Number of people in the car", "Frequency", 0, 22, 2)));
		
	}

	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo();
			frame.setVisible(true);
		});
	}

}
