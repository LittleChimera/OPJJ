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

public class SaveAction extends AbstractAction {

	protected Path savePath;
	private DrawingModel model;
	private Component parent;

	public SaveAction(Path savePath, DrawingModel model, Component parent) {
		this.savePath = savePath;
		this.model = model;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		saveModel();
	}

	public boolean saveModel() {
		if (savePath == null) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JVD",
					"jvd"));
			
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

	public void update(Path path, DrawingModel drawingModel) {
		this.savePath = path;
		this.model = drawingModel;
	}

}
