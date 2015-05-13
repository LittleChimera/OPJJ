package hr.fer.zemris.java.tecaj.hw5.observer1;

/**
 * Interface which is the definition of all observers watching IntegerStorage.
 * @author Luka Škugor
 *
 */
public interface IntegerStorageObserver {
	/**
	 * Completes an action depending on implementation of the observer when value changes.
	 * @param istorage observing object
	 */
	public void valueChanged(IntegerStorage istorage);
}
