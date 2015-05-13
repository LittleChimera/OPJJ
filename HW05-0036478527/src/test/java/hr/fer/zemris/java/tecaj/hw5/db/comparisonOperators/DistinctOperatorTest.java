package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistinctOperatorTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new DistinctOperator()).satisfied("abcd", "defg"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertTrue((new DistinctOperator()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedEquals() {
		assertFalse((new DistinctOperator()).satisfied("abcd", "abcd"));
	}


}
