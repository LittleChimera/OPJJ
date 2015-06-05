package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Exports a {@link DrawingModel} as an image file.
 * 
 * @author Luka Skugor
 *
 */
public class ExportAction extends AbstractAction {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Model which is exported.
	 */
	private DrawingModel model;
	/**
	 * Parent component used for displaying prompt dialogs.
	 */
	private Component parent;

	/**
	 * Creates a new ExportAction for the given model.
	 * 
	 * @param model
	 *            model which is exported
	 * @param parent
	 *            top-level element of the GUI where action is instanced
	 */
	public ExportAction(DrawingModel model, Component parent) {
		this.model = model;
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
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

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG",
				"jpg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF",
				"gif"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG",
				"png"));
		int choosen = fileChooser.showSaveDialog(parent);
		if (choosen == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String extension = ((FileNameExtensionFilter) fileChooser
					.getFileFilter()).getExtensions()[0];
			if (!file.getName().endsWith("." + extension)) {
				file = new File(file.getPath() + "." + extension);
			}

			try {
				ImageIO.write(image, extension, file);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(parent,
						"Unable to export image: " + e1.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * Gets model's minimum bounds so that all elements are displayed.
	 * 
	 * @return model's minimum bounds
	 */
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
