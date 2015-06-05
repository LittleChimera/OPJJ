package hr.fer.zemris.java.hw12.jvdraw.buttons;

import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;

import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 * Does the same thing as {@link SaveAction} except save path is reset each time
 * documents is saved. Consequently, user will be prompted each time to set save
 * path.
 * 
 * @see SaveAction
 * 
 * @author Luka Skugor
 *
 */
public class SaveAsAction extends SaveAction {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SaveAsAction for the given {@link DrawingModel}.
	 * 
	 * @param model
	 *            model to save
	 * @param parent
	 *            top-level container of the GUI where action is instanced
	 */
	public SaveAsAction(DrawingModel model, Component parent) {
		super(null, model, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw12.jvdraw.buttons.SaveAction#actionPerformed(java
	 * .awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		savePath = null;
	}

}
