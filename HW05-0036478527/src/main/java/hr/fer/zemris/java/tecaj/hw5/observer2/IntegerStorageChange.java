package hr.fer.zemris.java.tecaj.hw5.observer2;

import java.util.Optional;

/**
 * IntegerStorageChange encapsulates data of a single IntegerStorage and
 * remembers its last and new value.
 * 
 * @author Luka Škugor
 *
 */
public class IntegerStorageChange {

	/**
	 * Reference to encapsulated IntegerStorage.
	 */
	private IntegerStorage iStorage;
	/**
	 * Last value stored in IntegerStorage.
	 */
	private Integer lastValue;
	/**
	 * New value stored in IntegerStorage.
	 */
	private Integer newValue;

	/**
	 * Creates a new IntegerStorageChange with current state of a IntegerStorage
	 * and its last stored value.
	 * 
	 * @param iStorage IntegerStorage which will be encapsulated 
	 * @param lastValue last value of the stored integer in the IntegerStorage
	 */
	public IntegerStorageChange(IntegerStorage iStorage, int lastValue) {
		this.iStorage = iStorage;
		this.newValue = iStorage.getValue();
		this.lastValue = lastValue;
	}

	/**
	 * Gets last value of encapsulated IntegerStorage if it exists.
	 * 
	 * @return last value of encapsulated IntegerStorage
	 */
	public Optional<Integer> getLastValue() {
		return Optional.ofNullable(lastValue);
	}

	/**
	 * Gets new value stored by IntegerStorage.
	 * 
	 * @return new value stored by Integer storage
	 */
	public int getNewValue() {
		return newValue;
	}

	/**
	 * Gets the reference to the encapsulated IntegerStorage.
	 * 
	 * @return reference to the encapsulated IntegerStorage
	 */
	public IntegerStorage getIntegerStorage() {
		return iStorage;
	}

}
