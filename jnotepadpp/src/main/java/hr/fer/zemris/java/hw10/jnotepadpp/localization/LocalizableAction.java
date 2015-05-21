package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction {
		
	public LocalizableAction(String key, ILocalizationProvider localizationProvider) {
		super();
		
		localizationProvider.addLocalizationListener(() -> {
			putValue(Action.NAME, localizationProvider.getString(key));
		});
	}

}
