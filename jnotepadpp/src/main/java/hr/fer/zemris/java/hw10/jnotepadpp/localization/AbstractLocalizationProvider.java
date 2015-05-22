package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides default functionalities of {@link ILocalizationProvider}.
 * @author Luka Skugor
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * Provider's listeners
	 */
	private List<ILocalizationListener> listeners;
	
	/**
	 * Creates a new AbstractLocalizationProvider.
	 */
	public AbstractLocalizationProvider() {
		listeners = new LinkedList<ILocalizationListener>();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationProvider#addLocalizationListener(hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationListener)
	 */
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationProvider#removeLocalizationListener(hr.fer.zemris.java.hw10.jnotepadpp.localization.ILocalizationListener)
	 */
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners for a localization change.
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
