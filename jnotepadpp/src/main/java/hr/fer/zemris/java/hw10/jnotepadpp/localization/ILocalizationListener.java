package hr.fer.zemris.java.hw10.jnotepadpp.localization;

/**
 * Listener for {@link ILocalizationProvider} which is notified when
 * localization provider changes localization.
 * 
 * @author Luka Skugor
 *
 */
public interface ILocalizationListener {
	/**
	 * Performs an action when localization provider changes localization.
	 */
	void localizationChanged();
}
