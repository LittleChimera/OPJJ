package hr.fer.zemris.java.tecaj.hw3;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the CString class.
 * @author Luka Skugor
 *
 */
public class CStringTest {
	
	char[] testArray1 = {'a', 'b', 'c'};
	char[] testArray2 = {'a', 'b', 'a', 'b', 'a', 'b', 'a', 'b', 'a', 'b', 'd' , 'd' };
	
	
	@Test
	public void constructorFirstTestValid() {
		CString test = new CString(testArray2, 2, 8);
		assertEquals(test.toString(), "abababab");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFirstTestInvalidLength() {
		new CString(testArray2, 2, 14);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFirstTestInvalidOffsetNegative() {
		new CString(testArray2, -2, 3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFirstTestInvalidOffsetPositive() {
		new CString(testArray2, 13, 3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFirstTestNull() {
		new CString(null, 2, 8);
	}
	
	@Test
	public void constructorSecondTest() {
		CString test = new CString(testArray1);
		assertEquals(test.toString(), "abc");
	}
	
	@Test
	public void constructorThirdTest() {
		CString test = new CString(testArray2, 2, 6);
		CString copy = new CString(test);
		assertEquals(test.toString(), copy.toString());
	}
	
	@Test
	public void constructorFourthTest() {
		String test = "some text";
		CString copy = new CString(test);
		assertEquals(test.toString(), copy.toString());
	}
	
	CString c1 = new CString(testArray2, 2, 8);
	CString c2 = new CString(testArray1);
	CString c3 = new CString(c1);
	CString c4 = new CString("some text");
	
	@Test
	public void c1Length() {
		assertEquals(c1.length(), 8);
	}
	
	@Test
	public void c2Length() {
		assertEquals(c2.length(), 3);
	}
	
	@Test
	public void c3Length() {
		assertEquals(c3.length(), 8);
	}
	
	@Test
	public void c4Length() {
		assertEquals(c4.length(), 9);
	}
	
	@Test
	public void c1CharAt() {
		assertEquals(c1.charAt(7), 'b');
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void c1CharAtNegative() {
		c1.charAt(-1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void c1CharAtOverLength() {
		c1.charAt(8);
	}
	
	@Test
	public void c1ToCharArray() {
		char[] toArray = { 'a', 'b', 'a', 'b', 'a', 'b', 'a', 'b' };
		assertArrayEquals(c1.toCharArray(), toArray);
	}
	
	@Test
	public void c1IndexOf() {
		assertEquals(c1.indexOf('a'), 0);
	}
	
	@Test
	public void c2IndexOf() {
		assertEquals(c2.indexOf('c'), 2);
	}
	
	@Test
	public void c2IndexOfNotFound() {
		assertEquals(c2.indexOf('d'), -1);
	}
	
	@Test
	public void c1IndexOfNotFound() {
		assertEquals(c1.indexOf('d'), -1);
	}
	
	@Test
	public void c1StartsWithTrue() {
		assertTrue(c1.startsWith(new CString("ab")));
	}
	
	@Test
	public void c1StartsWithFalse() {
		assertFalse(c1.startsWith(new CString("b")));
	}
	
	@Test
	public void c1StartsWithStartingHasGreaterLength() {
		assertFalse(c1.startsWith(new CString("ababababab")));
	}
	
	@Test
	public void c1EndsWithTrue() {
		assertTrue(c1.endsWith(new CString("ab")));
	}
	
	@Test
	public void c1EndsWithFalse() {
		assertFalse(c1.endsWith(new CString("a")));
	}
	
	@Test
	public void c1EndsWithEndingHasGreaterLength() {
		assertFalse(c1.endsWith(new CString("ababababab")));
	}
	
	@Test
	public void c1ContainsTrue1() {
		assertTrue(c1.contains(new CString("ab")));
	}
	
	@Test
	public void c1ContainsTrue2() {
		assertTrue(c1.contains(new CString("ba")));
	}
	
	@Test
	public void c1ContainsTrue3() {
		assertTrue(c1.contains(new CString("abababab")));
	}
	
	@Test
	public void c1ContainsFalse() {
		assertFalse(c1.contains(new CString("d")));
	}
	
	@Test
	public void c1SubstringValid1() {
		assertEquals(c1.substring(0, 2).toString(), "ab");
	}
	
	@Test
	public void c1SubstringValid2() {
		assertEquals(c1.substring(0, 8).toString(), "abababab");
	}
	
	@Test
	public void c2SubstringValid1() {
		assertEquals(c2.substring(2, 3).toString(), "c");
	}
	
	@Test
	public void c2SubstringValid2() {
		assertEquals(c2.substring(0, 2).toString(), "ab");
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void c1SubstringInvalidStartIndex() {
		c1.substring(-1, 8);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void c1SubstringInvalidEndIndex() {
		c1.substring(1, 9);
	}
	
	@Test
	public void c1LeftValid() {
		assertEquals("ababa", c1.left(5).toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void c1LeftInvalidNegativeN() {
		c1.left(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void c1LeftInvalidGreaterThanLength() {
		c1.left(9);
	}
	
	@Test
	public void c1RightValid() {
		assertEquals("babab", c1.right(5).toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void c1RightInvalidNegativeN() {
		c1.right(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void c1RightInvalidGreaterThanLength() {
		c1.right(9);
	}
	
	@Test
	public void c1Addc2() {
		assertEquals("abababababc", c1.add(c2).toString());
	}
	
	@Test
	public void c4Addc1() {
		assertEquals("some textabababab", c4.add(c1).toString());
	}
	
	@Test
	public void c1replaceAwithD() {
		assertEquals("dbdbdbdb", c1.replaceAll('a', 'd').toString());
	}
	
	@Test
	public void c1replaceAwithWORD() {
		CString replacement = new CString("word");
		CString replacing = new CString("a");
		assertEquals("wordbwordbwordbwordb", c1.replaceAll(replacing, replacement).toString());
	}
	
	@Test
	public void c1replaceABwithWORD() {
		CString replacement = new CString("word");
		CString replacing = new CString("ab");
		assertEquals("wordwordwordword", c1.replaceAll(replacing, replacement).toString());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
