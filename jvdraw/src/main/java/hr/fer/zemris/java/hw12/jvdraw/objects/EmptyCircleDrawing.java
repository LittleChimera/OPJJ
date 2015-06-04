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

public class EmptyCircleDrawing extends GeometricalObject {
	
	private int x;
	private int y;
	private int r;
	
	public EmptyCircleDrawing(int x, int y, int r, Color outline) {
		super(x - r, y - r, 2*r, 2*r, null, outline);
		this.x = x;
		this.y = y;
		this.r = r;
	}

	@Override
	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		
		g.setColor(outlineColor);
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public String getSaveFormat() {
		return "CIRCLE " + x + " " + y + " " + r + " " + rgbToSaveFormat(outlineColor);
	}

	@Override
	public String toString() {
		return "Circle";
	}

	@Override
	public void showChangeDialog(Component parent) {
		JPanel changePanel = new JPanel(new GridLayout(4, 2));
		JTextField xCoordinate = new JTextField(Integer.toString(x));
		JTextField yCoordinate = new JTextField(Integer.toString(y));
		JTextField radius = new JTextField(Integer.toString(r));
		JColorArea fillColor = new JColorArea(this.fillColor, "fill color", parent);
		JColorArea outlineColor = new JColorArea(this.outlineColor, "outline color", parent);
		
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
	
	protected void changeObject(int x, int y, int r, Color outline) {
		this.x = x;
		this.y = y;
		this.r = r;
		changeObject(x - r, y - r, 2 * r, 2 * r, null, outline);
	}
	
}
