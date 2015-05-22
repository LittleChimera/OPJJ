package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.ActionEvent;

/**
 * DefaultLocalizableAction is a {@link LocalizableAction} which does nothing.
 * It's only use is to localize components which can be constructed with action
 * but do no perform any action.
 * 
 * @author Luka Skugor
 *
 */
public class DefaultLocalizableAction extends LocalizableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new DefaultLocalizableAction with given key and localization provider.
	 * @param key localization key
	 * @param localizationProvider localization provider
	 */
	public DefaultLocalizableAction(String key,
			ILocalizationProvider localizationProvider) {
		super(key, localizationProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
