package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw12.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ExportAction extends AbstractAction {

	private DrawingModel model;
	private Component parent;

	public ExportAction(DrawingModel model, Component parent) {
		this.model = model;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (model.getSize() == 0) {
			JOptionPane.showMessageDialog(parent, "No elements are drawn",
					"Empty canvas", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Rectangle minBounds = getMinimumBounds();
		BufferedImage image = new BufferedImage(minBounds.width,
				minBounds.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		
		g.fillRect(0, 0, minBounds.width, minBounds.height);
		
		for (int i = 0, count = model.getSize(); i < count; i++) {
			GeometricalObject object = model.getObject(i);
			Rectangle origBounds = object.getBounds();

			object.setLocation(origBounds.x - minBounds.x, origBounds.y
					- minBounds.y);
			object.paint(g);

			object.setBounds(origBounds);
		}
		g.dispose();

		/*
		 * JFrame frame = new JFrame();
		 * frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 * frame.setSize(minBounds.width, minBounds.height);
		 * frame.getContentPane().setLayout(new BorderLayout());
		 * frame.getContentPane().add(new JComponent() {
		 * 
		 * @Override public void paint(Graphics g) { g.drawImage(image, 0, 0,
		 * frame); } }, BorderLayout.CENTER); frame.setVisible(true);
		 */

		JFileChooser fileChooser = new JFileChooser();
		int choosen = fileChooser.showSaveDialog(parent);
		if (choosen == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				ImageIO.write(image, "png", file);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(parent, "Unable to export image",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private Rectangle getMinimumBounds() {
		Rectangle minimumBounds = model.getObject(0).getBounds();
		for (int i = 0, count = model.getSize(); i < count; i++) {
			GeometricalObject object = model.getObject(i);
			Rectangle bounds = object.getBounds();

			int minX2 = minimumBounds.x + minimumBounds.width;
			int x2 = bounds.x + bounds.width;
			if (minX2 < x2) {
				minX2 = x2;
			}
			int minY2 = minimumBounds.y + minimumBounds.height;
			int y2 = bounds.y + bounds.height;
			if (minY2 < y2) {
				minY2 = y2;
			}

			if (minimumBounds.x > bounds.x) {
				minimumBounds.x = bounds.x;
			}
			if (minimumBounds.y > bounds.y) {
				minimumBounds.y = bounds.y;
			}

			minimumBounds.width = minX2 - minimumBounds.x;
			minimumBounds.height = minY2 - minimumBounds.y;
		}
		return minimumBounds;
	}
}
