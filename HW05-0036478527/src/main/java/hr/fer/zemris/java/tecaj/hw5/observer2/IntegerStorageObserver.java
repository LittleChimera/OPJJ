package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * Interface which is the definition of all observers watching IntegerStorage.
 * @author Luka Škugor
 *
 */
public interface IntegerStorageObserver {
	/**
	 * Completes an action depending on implementation of the observer when value changes.
	 * @param iStorageChange observing object
	 */
	public void valueChanged(IntegerStorageChange iStorageChange);
}
