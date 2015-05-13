package hr.fer.zemris.java.tecaj.hw3;

import java.util.Iterator;

/**
 * IntegerSequence provides iteration through range of integers.
 * Every IntegerSequence is defined with three values:
 * from	- starting value
 * to	- end value
 * step	- step of iteration
 * 
 * For example, IntegerSequence(1,9,3) iterates through numbers 1,4,7
 * @author Luka Skugor
 *
 */
public class IntegerSequence implements Iterable<Integer> {
	
	/**
	 * starting value
	 */
	private int from;
	/**
	 * end value
	 */
	private int to;
	/**
	 * step of iteration
	 */
	private int step;
	
	/**
	 * Constructs a new sequence given restrictions and step.
	 * @param from starting value
	 * @param to end value
	 * @param step step of iteration
	 */
	public IntegerSequence(int from, int to, int step) {
		if (!((step > 0 && from <= to) || (step < 0 && from >= to))) {
			throw new IllegalArgumentException("Invalid range of sequence!");
		}

		this.from = from;
		this.to = to;
		this.step = step;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			int current = from - step;

			@Override
			public boolean hasNext() {
				if ((step > 0 && current + step <= to)
						|| (step < 0 && current + step >= to)) {
					return true;
				}
				return false;
			}

			@Override
			public Integer next() {
				current += step;
				return current;
			}
		};
	}


}
