package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class GreaterThanComparatorTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new GreaterThanComparator()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertFalse((new GreaterThanComparator()).satisfied("abcd", "defg"));
	}
	
	@Test
	public void satisfiedEquals() {
		assertFalse((new GreaterThanComparator()).satisfied("abcd", "abcd"));
	}

}
