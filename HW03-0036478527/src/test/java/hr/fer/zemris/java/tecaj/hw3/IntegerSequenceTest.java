package hr.fer.zemris.java.tecaj.hw3;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

/**
 * Unit tests for the IntegerSequence class.
 * @author Luka Skugor
 *
 */
public class IntegerSequenceTest {

	@Test(expected=IllegalArgumentException.class)
	public void rangeGoingtoPositiveInvalid() {
		new IntegerSequence(10, 2, 2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rangeGoingtoNegativeInvalid() {
		new IntegerSequence(-5, 2, -2);
	}
	
	@Test
	public void rangeGoingtoNegativeIterate() {
		IntegerSequence is = new IntegerSequence(2, -5, -2);
		Iterator<Integer> iterator = is.iterator();
		for (int counter = 0; counter < 3; counter++) {
			if(iterator.hasNext()) {
				iterator.next();
			}
		}
		assertEquals(iterator.next().intValue(), -4);
	}
	
	@Test
	public void rangeGoingtoNegativeIterateEndValue() {
		IntegerSequence is = new IntegerSequence(2, -5, -2);
		Iterator<Integer> iterator = is.iterator();
		for (int counter = 0; counter < 4; counter++) {
			if(iterator.hasNext()) {
				iterator.next();
			}
		}
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void rangeGoingtoPositiveIterate() {
		IntegerSequence is = new IntegerSequence(2, 9, 3);
		Iterator<Integer> iterator = is.iterator();
		for (int counter = 0; counter < 2; counter++) {
			if(iterator.hasNext()) {
				iterator.next();
			}
		}
		assertEquals(iterator.next().intValue(), 8);
	}
	
	@Test
	public void rangeGoingtoPositiveIterateEndValue() {
		IntegerSequence is = new IntegerSequence(2, 9, 3);
		Iterator<Integer> iterator = is.iterator();
		for (int counter = 0; counter < 3; counter++) {
			if(iterator.hasNext()) {
				iterator.next();
			}
		}
		assertFalse(iterator.hasNext());
	}
	

}
