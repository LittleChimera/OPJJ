package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {
	
	private String language;
	private ResourceBundle bundle;
	
	private static LocalizationProvider instance = new LocalizationProvider();
	
	private LocalizationProvider() {
	}

	@Override
	public String getString(String key) {
		return (bundle.containsKey(key))?bundle.getString(key):"?" + key + "?";
	}

	public static LocalizationProvider getInstance() {
		return instance;
	}

	public void setLanguage(String language) {
		if (language == this.language) {
			return;
		}
		
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw10.jnotepadpp.localization.prijevodi", locale);
		fire();
	}

}
