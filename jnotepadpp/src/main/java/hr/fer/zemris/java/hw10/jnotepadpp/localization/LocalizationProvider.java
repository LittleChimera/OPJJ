package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides content localization. 
 * 
 * @author Luka Skugor
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/**
	 * Currently set language's tag.
	 */
	private String language;
	/**
	 * Currently set language's bundle.
	 */
	private ResourceBundle bundle;
	
	/**
	 * Single instance of LocalizationProvider.
	 */
	private static LocalizationProvider instance = new LocalizationProvider();
	
	/**
	 * Creates a new Localization provider.
	 */
	private LocalizationProvider() {
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationProvider#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return (bundle.containsKey(key))?bundle.getString(key):"?" + key + "?";
	}

	/**
	 * Get's one and only instance of LocalizationProvider.
	 * @return localization provider
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets language of the localization provider.
	 * @param language set language
	 */
	public void setLanguage(String language) {
		if (language == this.language) {
			return;
		}
		
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw10.jnotepadpp.localization.prijevodi", locale);
		fire();
	}

	/**
	 * Get's active language of localization provider.
	 * @return active language
	 */
	public String getLanguage() {
		return language;
	}

}
