package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Test;

public class ObjectMultistackTest {
	

	@Test
	public void peekTest() {
		ObjectMultistack multistack = new ObjectMultistack();
		insertNValuesOnKey("test", multistack, 5);
		assertEquals(generateString(VALUE_PATTERN, 5), multistack.peek("test").getValue());
	}
	
	@Test
	public void popTest() {
		ObjectMultistack multistack = new ObjectMultistack();
		insertNValuesOnKey("test", multistack, 5);
		String generatedValueString = generateString(VALUE_PATTERN, 5);
		
		for (int i = 5; i > 0; i--) {
			assertEquals(generatedValueString.substring(0, i*VALUE_PATTERN.length()), multistack.pop("test").getValue());
		}
		
		assertTrue(multistack.isEmpty("test"));
	}
	
	@Test
	public void popSingle() {
		ObjectMultistack multistack = new ObjectMultistack();
		insertNValuesOnKey("test", multistack, 5);
		String generatedValueString = generateString(VALUE_PATTERN, 5);
		
		
		assertEquals(generatedValueString, multistack.pop("test").getValue());
	}
	
	@Test
	public void emptyTest() {
		ObjectMultistack multistack = new ObjectMultistack();
		assertTrue(multistack.isEmpty("asdfasdfasdf"));
	}
	
	@Test
	public void notEmptyTest() {
		String insert = "test string";
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push(insert, new ValueWrapper(insert));
		assertFalse(multistack.isEmpty(insert));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void peekNullKey() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.peek(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void popNullKey() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.pop(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void pushNullKey() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push(null, new ValueWrapper("a"));
	}
	
	@Test(expected=EmptyStackException.class)
	public void retrieveFromEmptyStack() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.peek("asdfasdf");
	}
	
	private static final String VALUE_PATTERN = "ASDF";
	private static void insertNValuesOnKey(String key, ObjectMultistack multistack, int n) {
		StringBuilder asdfBuilder = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			asdfBuilder.append(VALUE_PATTERN);
			multistack.push(key, new ValueWrapper(asdfBuilder.toString()));
		}
	}
	
	private static String generateString(String pattern, int n) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			stringBuilder.append(VALUE_PATTERN);
		}
		return stringBuilder.toString();
	}



}
