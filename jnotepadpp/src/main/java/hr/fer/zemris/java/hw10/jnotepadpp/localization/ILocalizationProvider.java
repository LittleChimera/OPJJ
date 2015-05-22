package hr.fer.zemris.java.hw10.jnotepadpp.localization;

/**
 * Provides localization of the application.
 * @author Luka Skugor
 *
 */
public interface ILocalizationProvider {
	/**
	 * Adds a listener which will be notified when the localization changes.
	 * @param l listener
	 */
	void addLocalizationListener(ILocalizationListener l);
	/**
	 * Removes a listener.
	 * @param l listener
	 */
	void removeLocalizationListener(ILocalizationListener l);
	/**
	 * Gets localized string of the current localization for the given key.
	 * @param key key of the localization
	 * @return localized string
	 */
	String getString(String key);
}
