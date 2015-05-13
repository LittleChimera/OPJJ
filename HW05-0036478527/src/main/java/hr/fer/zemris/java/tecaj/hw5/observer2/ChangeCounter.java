package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * Implementation of IntegerStorageObserver which counts how many times watched
 * IntegerStorage's value has been changed. Each time value changes prints tracking counter.
 * 
 * @author Luka Škugor
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Counts how many times has IntegerStorageChanged
	 */
	private int counter;

	/**
	 * Instances a new ChangeCounter which counts from zero.
	 */
	public ChangeCounter() {
		counter = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorageObserver#valueChanged
	 * (hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorage)
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		System.out.format("Number of value change since tracking: %d%n",
				++counter);
	}

}
