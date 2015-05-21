package hr.fer.zemris.java.hw10.jnotepadpp.localization;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	private ILocalizationProvider parent;
	private boolean connected;
	private ILocalizationListener registeredListener;

	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		connected = false;
	}

	@Override
	public String getString(String key) {
		return (connected) ? parent.getString(key) : null;
	}

	public void connect() {
		if (connected) {
			return;
		}
		connected = true;
		registeredListener = () -> {
			fire();
		}; 
		parent.addLocalizationListener(registeredListener);
	}

	public void disconnect() {
		if (!connected) {
			return;
		}
		connected = false;
		parent.removeLocalizationListener(registeredListener);
		registeredListener = null;
	}
}
