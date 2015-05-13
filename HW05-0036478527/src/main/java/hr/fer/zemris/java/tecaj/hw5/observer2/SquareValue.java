package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * Implementation of IntegerStorageObserver which prints value of observing
 * object and its square when value changes.
 * 
 * @author Luka Škugor
 *
 */
public class SquareValue implements IntegerStorageObserver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorageObserver#valueChanged
	 * (hr.fer.zemris.java.tecaj.hw5.observer1.IntegerStorage)
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		int value = istorage.getNewValue();
		System.out.format("Provided new value: %d, square is %d%n", value,
				value * value);
	}

}
