package hr.fer.zemris.java.hw12.jvdraw.objects;

import hr.fer.zemris.java.hw12.jvdraw.colors.JColorArea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * FullCircleDrawing is a drawing of full circle (i.e. outline and filling of
 * the circle are painted).
 * 
 * @see GeometricalObject
 * 
 * @author Luka Skugor
 *
 */
public class FullCircleDrawing extends GeometricalObject {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * x-coordinate of circle's center.
	 */
	private int x;
	/**
	 * y-coordinate of circle's center.
	 */
	private int y;
	/**
	 * Radius of the circle.
	 */
	private int r;

	/**
	 * Creates a new EmptyCircleDrawing with given center point, radius and
	 * outline color.
	 * 
	 * @param x
	 *            x-coordinate of circle's center
	 * @param y
	 *            y-coordinate of circle's center
	 * @param r
	 *            radius of the circle
	 * @param fill
	 *            filling color of the circle
	 * @param outline
	 *            outline color of the circle
	 */
	public FullCircleDrawing(int x, int y, int r, Color fill, Color outline) {
		super(x - r, y - r, 2 * r, 2 * r, fill, outline);
		this.x = x;
		this.y = y;
		this.r = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#paint(java.awt
	 * .Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		g.setColor(fillColor);
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);

		g.setColor(outlineColor);
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#getSaveFormat()
	 */
	@Override
	public String getSaveFormat() {
		return "FCIRCLE " + x + " " + y + " " + r + " "
				+ rgbToSaveFormat(fillColor) + " "
				+ rgbToSaveFormat(outlineColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#getName()
	 */
	@Override
	public String getName() {
		return "Full Circle";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#showChangeDialog
	 * (java.awt.Component)
	 */
	@Override
	public void showChangeDialog(Component parent) {
		JPanel changePanel = new JPanel(new GridLayout(5, 2));
		JTextField xCoordinate = new JTextField(Integer.toString(x));
		JTextField yCoordinate = new JTextField(Integer.toString(y));
		JTextField radius = new JTextField(Integer.toString(r));
		JColorArea fillColor = new JColorArea(this.fillColor, "fill color",
				parent);
		JColorArea outlineColor = new JColorArea(this.outlineColor,
				"outline color", parent);

		changePanel.add(new JLabel("Center x-coordinate"));
		changePanel.add(xCoordinate);
		changePanel.add(new JLabel("Center y-coordinate"));
		changePanel.add(yCoordinate);
		changePanel.add(new JLabel("Radius"));
		changePanel.add(radius);
		changePanel.add(new JLabel("Fill color"));
		changePanel.add(fillColor);
		changePanel.add(new JLabel("Outline color"));
		changePanel.add(outlineColor);

		if (JOptionPane.showConfirmDialog(parent, changePanel,
				"Set new circle values", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			try {
				int x = Integer.parseInt(xCoordinate.getText());
				int y = Integer.parseInt(yCoordinate.getText());
				int r = Integer.parseInt(radius.getText());

				changeObject(x, y, r, fillColor.getCurrentColor(),
						outlineColor.getCurrentColor());

			} catch (NumberFormatException invalidNumber) {
				JOptionPane.showMessageDialog(parent, "Invalid number",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * Changes parameters of the circle.
	 * 
	 * @param x
	 *            x-coordinate of circle's center
	 * @param y
	 *            y-coordinate of circle's center
	 * @param r
	 *            radius of the circle
	 * @param fill
	 *            filling color of the circle
	 * @param outline
	 *            outline color of the circle
	 */
	private void changeObject(int x, int y, int r, Color fill, Color outline) {
		this.x = x;
		this.y = y;
		this.r = r;
		changeObject(x - r, y - r, 2 * r, 2 * r, fill, outline);
	}

	/**
	 * Creates FullCircleDrawing from a JVD format.
	 * 
	 * @param jvd
	 *            JVD format of FullCircleDrawing
	 * @return created object
	 */
	public static FullCircleDrawing fromJvd(String jvd) {
		try {
			String[] params = jvd.split(" ", 2)[1].split(" ");
			int[] values = new int[params.length];
			for (int i = 0; i < params.length; i++) {
				values[i] = Integer.parseInt(params[i]);
			}
			return new FullCircleDrawing(values[0], values[1], values[2],
					new Color(values[3], values[4], values[5]), new Color(
							values[6], values[7], values[8]));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid format");
		}
	}
}
