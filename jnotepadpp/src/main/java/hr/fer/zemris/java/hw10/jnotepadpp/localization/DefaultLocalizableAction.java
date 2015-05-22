package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.ActionEvent;

public class DefaultLocalizableAction extends LocalizableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultLocalizableAction(String key,
			ILocalizationProvider localizationProvider) {
		super(key, localizationProvider);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
