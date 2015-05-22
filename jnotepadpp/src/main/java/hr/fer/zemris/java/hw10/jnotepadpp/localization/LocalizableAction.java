package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * LocalizableAction is an action which localizes action's {@link javax.swing.Action#NAME}
 * and {@link javax.swing.Action#SHORT_DESCRIPTION}.
 * 
 * @author Luka Skugor
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * Creates a new LocalizableAction which determines name and description of
	 * the action by the provided key and localization provider. Keys are
	 * defined as key_[Action.{PROPERTY}] (all lower case).
	 * 
	 * @param key
	 *            key of the action
	 * @param localizationProvider
	 *            localization provider
	 */
	public LocalizableAction(String key,
			ILocalizationProvider localizationProvider) {
		super();
		
		localizationProvider.addLocalizationListener(() -> {
			putValue(Action.NAME, localizationProvider.getString(key + "_name"));
			putValue(Action.SHORT_DESCRIPTION, localizationProvider.getString(key + "_short_description"));
		});
	}

}
