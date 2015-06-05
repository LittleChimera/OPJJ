package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Action which saves {@link DrawingModel} in JVD format.
 * 
 * @author Luka Skugor
 *
 */
public class SaveAction extends AbstractAction {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Document save path.
	 */
	protected Path savePath;
	/**
	 * Drawing model to save.
	 */
	private DrawingModel model;
	/**
	 * Parent component used for displaying prompt dialogs.
	 */
	private Component parent;

	/**
	 * Creates a new SaveAction for given save path and {@link DrawingModel}.
	 * 
	 * @param savePath
	 *            path where document will be saved
	 * @param model
	 *            drawing model to save
	 * @param parent
	 *            top-level container of the GUI where action is instanced
	 */
	public SaveAction(Path savePath, DrawingModel model, Component parent) {
		this.savePath = savePath;
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
		saveModel();
	}

	/**
	 * Saves model at current path. Prompts user to set save path if it's not
	 * set. Saving can be interrupted by the user.
	 * 
	 * @return true if model was saved, otherwise false
	 */
	public boolean saveModel() {
		if (savePath == null) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"JVD", "jvd"));

			int result = fileChooser.showSaveDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				String extension = ((FileNameExtensionFilter) fileChooser
						.getFileFilter()).getExtensions()[0];
				if (!file.getName().endsWith("." + extension)) {
					file = new File(file.getPath() + "." + extension);
				}

				savePath = file.toPath();
			} else {
				return false;
			}
		}

		String jvdString = model.getAsJvd();
		try {
			if (!Files.exists(savePath)) {
				Files.createFile(savePath);
			}
			Files.write(savePath, jvdString.getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.WRITE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(parent, "Unable to save the file: "
					+ e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		model.save();
		JOptionPane.showMessageDialog(parent, "Saved!", "Info",
				JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Updates action's save path and drawing model.
	 * @param path updated save path
	 * @param drawingModel updated drawing model
	 */
	public void update(Path path, DrawingModel drawingModel) {
		this.savePath = path;
		this.model = drawingModel;
	}

}
