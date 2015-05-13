package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class GreaterOrEqualsComparatorTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new GreaterOrEqualsComparator()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertFalse((new GreaterOrEqualsComparator()).satisfied("abcd", "defg"));
	}
	
	@Test
	public void satisfiedEquals() {
		assertTrue((new GreaterOrEqualsComparator()).satisfied("abcd", "abcd"));
	}


}
