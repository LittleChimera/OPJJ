package hr.fer.zemris.java.hw12.jvdraw.objects;

import hr.fer.zemris.java.hw12.jvdraw.colors.JColorArea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LineDrawing is a drawing of a straight line painted in custom color.
 * 
 * @see GeometricalObject
 * 
 * @author Luka Skugor
 *
 */
public class LineDrawing extends GeometricalObject {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * x-coordinate of starting point of the line.
	 */
	private int x1;
	/**
	 * y-coordinate of starting point of the line.
	 */
	private int y1;
	/**
	 * x-coordinate of ending point of the line.
	 */
	private int x2;
	/**
	 * y-coordinate of ending point of the line.
	 */
	private int y2;

	/**
	 * Creates a new line drawing for given starting and ending point, and line color.
	 * @param x1 x-coordinate of starting point of the line
	 * @param y1 y-coordinate of starting point of the line
	 * @param x2 x-coordinate of ending point of the line
	 * @param y2 y-coordinate of ending point of the line
	 * @param color line's color
	 */
	public LineDrawing(int x1, int y1, int x2, int y2, Color color) {
		super(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math
				.abs(y1 - y2), color, null);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {

		g.setColor(fillColor);
		g.drawLine(x1, y1, x2, y2);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#getSaveFormat()
	 */
	@Override
	public String getSaveFormat() {
		return "LINE " + x1 + " " + y1 + " " + x2 + " " + y2 + " "
				+ rgbToSaveFormat(fillColor);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#getName()
	 */
	@Override
	public String getName() {
		return "Line";
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#showChangeDialog(java.awt.Component)
	 */
	@Override
	public void showChangeDialog(Component parent) {
		JPanel changePanel = new JPanel(new GridLayout(5, 2));
		JTextField x1Coordinate = new JTextField(Integer.toString(x1));
		JTextField y1Coordinate = new JTextField(Integer.toString(y1));
		JTextField x2Coordinate = new JTextField(Integer.toString(x2));
		JTextField y2Coordinate = new JTextField(Integer.toString(y2));
		JColorArea fillColor = new JColorArea(this.fillColor, "fill color",
				parent);

		changePanel.add(new JLabel("Start x-coordinate"));
		changePanel.add(x1Coordinate);
		changePanel.add(new JLabel("Start y-coordinate"));
		changePanel.add(y1Coordinate);
		changePanel.add(new JLabel("End x-coordinate"));
		changePanel.add(x2Coordinate);
		changePanel.add(new JLabel("End y-coordinate"));
		changePanel.add(y2Coordinate);

		changePanel.add(new JLabel("Fill color"));
		changePanel.add(fillColor);

		if (JOptionPane.showConfirmDialog(parent, changePanel,
				"Set new circle values", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			try {
				int x1 = Integer.parseInt(x1Coordinate.getText());
				int y1 = Integer.parseInt(y1Coordinate.getText());
				int x2 = Integer.parseInt(x2Coordinate.getText());
				int y2 = Integer.parseInt(y2Coordinate.getText());

				changeObject(x1, y1, x2, y2, fillColor.getCurrentColor());

			} catch (NumberFormatException invalidNumber) {
				JOptionPane.showMessageDialog(parent, "Invalid number",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Changes parameters of the line.
	 * @param x1 x-coordinate of starting point of the line
	 * @param y1 y-coordinate of starting point of the line
	 * @param x2 x-coordinate of ending point of the line
	 * @param y2 y-coordinate of ending point of the line
	 * @param color line's color
	 */
	private void changeObject(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		changeObject(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2),
				Math.abs(y1 - y2), color, null);
	}

	/**
	 * Creates LineDrawing from a JVD format.
	 * 
	 * @param jvd
	 *            JVD format of LineDrawing
	 * @return created object
	 */
	public static LineDrawing fromJvd(String jvd) {
		try {
			String[] params = jvd.split(" ", 2)[1].split(" ");
			int[] values = new int[params.length];
			for (int i = 0; i < params.length; i++) {
				values[i] = Integer.parseInt(params[i]);
			}
			return new LineDrawing(values[0], values[1], values[2], values[3],
					new Color(values[4], values[5], values[6]));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid format");
		}
	}
}
