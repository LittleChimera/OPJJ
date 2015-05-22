package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * LocalizableAction is an action which localizes action's {@link Action.NAME}
 * and {@link Action.SHORT_DESCRIPTION}.
 * 
 * @author Luka Skugor
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * Creates a new LocalizableAction which determines name and description of the action by the provided key and localization provider. Keys are defined as key_Action.{PROPERTY}.
	 * @param key key of the action
	 * @param localizationProvider localization provider
	 */
	public LocalizableAction(String key,
			ILocalizationProvider localizationProvider) {
		super();

		localizationProvider.addLocalizationListener(() -> {
			putValue(Action.NAME, localizationProvider.getString(key));
		});
	}

}
