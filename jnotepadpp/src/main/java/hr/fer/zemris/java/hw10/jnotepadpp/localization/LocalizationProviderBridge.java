package hr.fer.zemris.java.hw10.jnotepadpp.localization;

/**
 * This class provider bridge between frame's parent's localization provider and
 * child frame. If bridge is connected child frame will notify all it's
 * listeners once the localization change has been made in parent's localization
 * provider.
 * 
 * @author Luka Skugor
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Parent localization.
	 */
	private ILocalizationProvider parent;
	/**
	 * Indicates if bridge is connected.
	 */
	private boolean connected;
	/**
	 * Listener which is added to parent's localization when the bridge
	 * connects. It's removed when bridge disconnects.
	 */
	private ILocalizationListener registeredListener;

	/**
	 * Creates a new LocalizationProviderBridge. Bridge is disconnected by
	 * default.
	 * 
	 * @param parent
	 *            parent's localization provider.
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		connected = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationProvider
	 * #getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return (connected) ? parent.getString(key) : null;
	}

	/**
	 * Connects bridge.
	 */
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

	/**
	 * Disconnect bridge.
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}
		connected = false;
		parent.removeLocalizationListener(registeredListener);
		registeredListener = null;
	}
}
