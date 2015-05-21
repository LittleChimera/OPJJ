package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	private List<ILocalizationListener> listeners;
	
	public AbstractLocalizationProvider() {
		listeners = new LinkedList<ILocalizationListener>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
