package hr.fer.zemris.java.hw10.jnotepadpp.localization;

public interface ILocalizationProvider {
	void addLocalizationListener(ILocalizationListener l);
	void removeLocalizationListener(ILocalizationListener l);
	String getString(String key);
}
