package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class LessOrEqualsComparatorTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new LessOrEqualsComparator()).satisfied("abcd", "defg"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertFalse((new LessOrEqualsComparator()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedEquals() {
		assertTrue((new LessOrEqualsComparator()).satisfied("abcd", "abcd"));
	}


}
