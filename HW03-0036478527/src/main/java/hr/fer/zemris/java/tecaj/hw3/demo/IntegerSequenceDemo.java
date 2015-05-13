package hr.fer.zemris.java.tecaj.hw3.demo;

import hr.fer.zemris.java.tecaj.hw3.IntegerSequence;

/**
 * Demonstration for class IntegerSequence.
 * @author Luka Skugor
 *
 */
public class IntegerSequenceDemo {
	
	/**
	 * Runs on program start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		IntegerSequence range = new IntegerSequence(1, 11, 2);
		for (int i : range) {
			for (int j : range) {
				System.out.println("i=" + i + ", j=" + j);
			}
		}
	}

}
