package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class SaveAsAction extends SaveAction {

	public SaveAsAction(DrawingModel model, Component parent) {
		super(null, model, parent);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		savePath = null;
	}

}
