package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

import static org.junit.Assert.*;

import org.junit.Test;

public class WildCardEqualsConditionTest {

	@Test
	public void satisfiedTrue() {
		assertTrue((new WildCardEqualsCondition()).satisfied("abcd", "abcd"));
	}
	
	@Test
	public void satisfiedFalse() {
		assertFalse((new WildCardEqualsCondition()).satisfied("defg", "abcd"));
	}
	
	@Test
	public void satisfiedTrueWildCardAtBeginning() {
		assertTrue((new WildCardEqualsCondition()).satisfied("defgabcd", "*abcd"));
	}
	
	@Test
	public void satisfiedFalse1WildCardAtBeginning() {
		assertFalse((new WildCardEqualsCondition()).satisfied("defgabd", "*abcd"));
	}
	
	@Test
	public void satisfiedFalse2WildCardAtBeginning() {
		assertFalse((new WildCardEqualsCondition()).satisfied("defgabcdaaa", "*abcd"));
	}
	
	@Test
	public void satisfiedTrueWildCardAtEnd() {
		assertTrue((new WildCardEqualsCondition()).satisfied("abcdjkjg", "abcd*"));
	}
	
	@Test
	public void satisfiedFalse1WildCardAtEnd() {
		assertFalse((new WildCardEqualsCondition()).satisfied("abddefgabd", "abcd*"));
	}
	
	@Test
	public void satisfiedFalse2WildCardAtEnd() {
		assertFalse((new WildCardEqualsCondition()).satisfied("aabcddefgaaaa", "abcd*"));
	}
	
	@Test
	public void satisfiedTrueWildCardInMiddle() {
		assertTrue((new WildCardEqualsCondition()).satisfied("abdefgabcd", "ab*cd"));
	}
	
	@Test
	public void satisfiedFalse1WildCardInMiddle() {
		assertFalse((new WildCardEqualsCondition()).satisfied("aabkkkcd", "ab*cd"));
	}
	
	@Test
	public void satisfiedFalse2WildCardInMiddle() {
		assertFalse((new WildCardEqualsCondition()).satisfied("abkkkkcdd", "ab*cd"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void multipleWildCards() {
		(new WildCardEqualsCondition()).satisfied("abkkkkcdd", "ab*cd*");
	}
	

}
