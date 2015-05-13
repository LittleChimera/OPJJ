package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class LessThanComparatorTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new LessThanComparator()).satisfied("abcd", "defg"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertFalse((new LessThanComparator()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedEquals() {
		assertFalse((new LessThanComparator()).satisfied("abcd", "abcd"));
	}

}
