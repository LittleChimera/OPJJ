package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class ValueWrapperTest {
	
	//TODO dodati null testove
	
	private static final double THETA = 1E-9;
	
	//incrementing
	
	@Test
	public void incrementIntegerWithInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.increment(Integer.valueOf(3));
		assertEquals(8, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void incrementIntegerWithDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.increment(Double.valueOf(3.5));
		assertEquals(8.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.increment("3");
		assertEquals(8, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void incrementIntegerWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.increment("35.0E-1");
		assertEquals(8.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementDoubleWithDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.3));
		value.increment(Double.valueOf(3.5));
		assertEquals(8.8, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.increment("3.5");
		assertEquals(9, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.increment("3");
		assertEquals(8.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementStringDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.increment("35.0E-1");
		assertEquals(6.8, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementStringDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.increment("5");
		assertEquals(8.3, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void incrementStringIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper("5");
		value.increment("3");
		assertEquals(8, ((Integer)value.getValue()).intValue());
	}
	
	//decrementing
	
	@Test
	public void decrementIntegerWithInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.decrement(Integer.valueOf(3));
		assertEquals(2, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void decrementIntegerWithDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.decrement(Double.valueOf(3.5));
		assertEquals(1.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.decrement("3");
		assertEquals(2, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void decrementIntegerWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.decrement("35.0E-1");
		assertEquals(1.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementDoubleWithDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.3));
		value.decrement(Double.valueOf(3.5));
		assertEquals(1.8, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.decrement("3.5");
		assertEquals(2, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.decrement("3");
		assertEquals(2.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementStringDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.decrement("35.0E-1");
		assertEquals(-0.2, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementStringDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.decrement("5");
		assertEquals(-1.7, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void decrementStringIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper("5");
		value.decrement("3");
		assertEquals(2, ((Integer)value.getValue()).intValue());
	}
	
	//multiply
	
	@Test
	public void multiplyIntegerWithInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.multiply(Integer.valueOf(3));
		assertEquals(15, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void multiplyIntegerWithDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.multiply(Double.valueOf(3.5));
		assertEquals(17.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.multiply("3");
		assertEquals(15, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void multiplyIntegerWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.multiply("35.0E-1");
		assertEquals(17.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyDoubleWithDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.3));
		value.multiply(Double.valueOf(3.5));
		assertEquals(18.55, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.multiply("3.5");
		assertEquals(19.25, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.multiply("3");
		assertEquals(16.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyStringDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.multiply("35.0E-1");
		assertEquals(11.55, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyStringDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.multiply("5");
		assertEquals(16.5, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void multiplyStringIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper("5");
		value.multiply("3");
		assertEquals(15, ((Integer)value.getValue()).intValue());
	}
	
	//divide
	
	@Test
	public void divideIntegerWithInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.divide(Integer.valueOf(3));
		assertEquals(1, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void divideIntegerWithDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.divide(Double.valueOf(3.5));
		assertEquals(1.428571429, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.divide("3");
		assertEquals(1, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void divideIntegerWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Integer.valueOf(5));
		value.divide("35.0E-1");
		assertEquals(1.428571429, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideDoubleWithDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.3));
		value.divide(Double.valueOf(3.5));
		assertEquals(1.514285714, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.divide("3.5");
		assertEquals(1.571428571, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper(Double.valueOf(5.5));
		value.divide("3");
		assertEquals(1.833333333, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideStringDoubleWithStringDouble() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.divide("35.0E-1");
		assertEquals(0.9428571429, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideStringDoubleWithStringInteger() {
		ValueWrapper value = new ValueWrapper("3.3");
		value.divide("5");
		assertEquals(0.66, ((Double)value.getValue()).doubleValue(), THETA);
	}
	
	@Test
	public void divideStringIntegerWithStringInteger() {
		ValueWrapper value = new ValueWrapper("5");
		value.divide("3");
		assertEquals(1, ((Integer)value.getValue()).intValue());
	}
	
	@Test
	public void operationWithNull() {
		ValueWrapper value = new ValueWrapper(null);
		value.increment("3");
		assertEquals(3, ((Integer)value.getValue()).intValue());
	}
	
	@Test(expected=RuntimeException.class)
	public void operateWithIllegalObject() {
		ValueWrapper value = new ValueWrapper(new LinkedList<Object>());
		value.increment("3");
	}
	
	@Test(expected=ArithmeticException.class)
	public void divideWithZero() {
		ValueWrapper value = new ValueWrapper("3");
		value.divide(null);
	}
	
	@Test(expected=RuntimeException.class)
	public void failParseDouble1() {
		ValueWrapper value = new ValueWrapper("3.5.");
		value.increment("3");
	}
	
	@Test(expected=RuntimeException.class)
	public void failParseDouble2() {
		ValueWrapper value = new ValueWrapper("1e-");
		value.increment("3");
	}
	
	@Test(expected=RuntimeException.class)
	public void failParseInteger() {
		ValueWrapper value = new ValueWrapper("2as");
		value.increment("3");
	}
	
	@Test
	public void changeValue() {
		ValueWrapper value = new ValueWrapper("3");
		value.setValue("4");
		assertEquals("4", value.getValue());
	}
	
	@Test
	public void compareWithSmallerValue() {
		ValueWrapper value = new ValueWrapper("3");
		assertEquals(1, value.numCompare("2"));
	}
	
	@Test
	public void compareWithEqualValue() {
		ValueWrapper value = new ValueWrapper("3");
		assertEquals(0, value.numCompare(3));
	}
	
	@Test
	public void compareWithGreaterValue() {
		ValueWrapper value = new ValueWrapper(5);
		assertEquals(-1, value.numCompare("7.5"));
	}
	
	
	
	

}
