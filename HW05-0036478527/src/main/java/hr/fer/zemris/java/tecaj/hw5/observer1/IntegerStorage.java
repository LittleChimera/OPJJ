package hr.fer.zemris.java.tecaj.hw5.observer1;

import java.util.LinkedList;
import java.util.List;

/**
 * Stores a single integer. It can be observed and all observers are notified
 * when value changes.
 * 
 * @author Luka Škugor
 *
 */
public class IntegerStorage {
	/**
	 * Held value.
	 */
	private int value;
	/**
	 * List of observers.
	 */
	private List<IntegerStorageObserver> observers;

	/**
	 * Creates a new IntegerStorage with given initial value.
	 * @param initialValue initially stored value
	 */
	public IntegerStorage(int initialValue) {
		observers = new LinkedList<IntegerStorageObserver>();
		this.value = initialValue;
	}

	/**
	 * Adds an observer to the list of observers which are watching the object.
	 * @param observer observer watching this object
	 * @throws IllegalArgumentException if observer has null value
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observer != null) {
			observers.add(observer);
		} else {
			throw new IllegalArgumentException("Observer is null.");
		}
	}

	/**
	 * Removes an observer from the list of observers which are watching the object.
	 * @param observer removing observer
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Removes all observers from this object.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Gets stored value.
	 * @return stored value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets a new value of stored integer and notifies all observers.
	 * @param value new store value
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			// Update current value
			this.value = value;
			// Notify all registered observers
			if (observers != null) {
				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(this);
				}
			}
		}
	}
}
