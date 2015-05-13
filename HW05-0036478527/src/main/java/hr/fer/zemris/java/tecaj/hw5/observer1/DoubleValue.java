package hr.fer.zemris.java.tecaj.hw5.observer1;

/**
 * Implementation of IntegerStorageObserver which prints double value of
 * IntegerStorage whenever value of IntegerStorage changes. It automatically
 * removes itself from IntegerStorage's observers when it's used two times.
 * 
 * @author Luka Škugor
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * Definition of how many times will observer be used before removed.
	 */
	private static final int DEATH_COUNTER = 2;
	/**
	 * Counts a number of remaining uses.
	 */
	private int counter;

	/**
	 * Instances a new DoubleValue.
	 */
	public DoubleValue() {
		counter = DEATH_COUNTER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorageObserver#valueChanged
	 * (hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorage)
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.format("Double value: %d%n", istorage.getValue() * 2);
		if (--counter == 0) {
			istorage.removeObserver(this);
		}
	}

}
