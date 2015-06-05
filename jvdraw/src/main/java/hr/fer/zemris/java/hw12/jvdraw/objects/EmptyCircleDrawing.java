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
 * EmptyCircleDrawing is a drawing of empty circle (i.e. only outline is
 * painted).
 * 
 * @see GeometricalObject
 * 
 * @author Luka Skugor
 *
 */
public class EmptyCircleDrawing extends GeometricalObject {

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
	 * @param outline
	 *            outline color of the circle
	 */
	public EmptyCircleDrawing(int x, int y, int r, Color outline) {
		super(x - r, y - r, 2 * r, 2 * r, null, outline);
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
		return "CIRCLE " + x + " " + y + " " + r + " "
				+ rgbToSaveFormat(outlineColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject#getName()
	 */
	@Override
	public String getName() {
		return "Empty Circle";
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
		JPanel changePanel = new JPanel(new GridLayout(4, 2));
		JTextField xCoordinate = new JTextField(Integer.toString(x));
		JTextField yCoordinate = new JTextField(Integer.toString(y));
		JTextField radius = new JTextField(Integer.toString(r));
		JColorArea outlineColor = new JColorArea(this.outlineColor,
				"outline color", parent);

		changePanel.add(new JLabel("Center x-coordinate"));
		changePanel.add(xCoordinate);
		changePanel.add(new JLabel("Center y-coordinate"));
		changePanel.add(yCoordinate);
		changePanel.add(new JLabel("Radius"));
		changePanel.add(radius);
		changePanel.add(new JLabel("Outline color"));
		changePanel.add(outlineColor);

		if (JOptionPane.showConfirmDialog(parent, changePanel,
				"Set new circle values", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			try {
				int x = Integer.parseInt(xCoordinate.getText());
				int y = Integer.parseInt(yCoordinate.getText());
				int r = Integer.parseInt(radius.getText());

				changeObject(x, y, r, outlineColor.getCurrentColor());

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
	 * @param outline
	 *            outline color of the circle
	 */
	private void changeObject(int x, int y, int r, Color outline) {
		this.x = x;
		this.y = y;
		this.r = r;
		changeObject(x - r, y - r, 2 * r, 2 * r, null, outline);
	}

	/**
	 * Creates EmptyCircleDrawing from a JVD format.
	 * 
	 * @param jvd
	 *            JVD format of EmptyCircleDrawing
	 * @return created object
	 */
	public static EmptyCircleDrawing fromJvd(String jvd) {
		try {
			String[] params = jvd.split(" ", 2)[1].split(" ");
			int[] values = new int[params.length];
			for (int i = 0; i < params.length; i++) {
				values[i] = Integer.parseInt(params[i]);
			}
			return new EmptyCircleDrawing(values[0], values[1], values[2],
					new Color(values[3], values[4], values[5]));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid format");
		}
	}
}
