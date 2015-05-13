package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Wraps a value and allows some basic operations (addition, subtraction,
 * multiplication, division) with values on which these operations can be
 * executed. These operations can executed on Integers, Doubles and Strings
 * which have a double or integer format. Additionally operations can be
 * executed on null values which are treated as integer of value 0.
 * 
 * @author Luka Skugor
 *
 */
public class ValueWrapper {

	/**
	 * Wrapped value.
	 */
	private Object value;

	/**
	 * Creates a new wrapper for the given value.
	 * 
	 * @param value
	 *            wrapped value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Increments wrapped value with the given value.
	 * 
	 * @param incValue
	 *            increment value
	 * @throws RuntimeException
	 *             if operation can't be executed
	 */
	public void increment(Object incValue) {
		value = doOperation(value, incValue, '+');
	}

	/**
	 * Decrements wrapped value with the given value.
	 * 
	 * @param decValue
	 *            decrement value
	 * @throws RuntimeException
	 *             if operation can't be executed
	 */
	public void decrement(Object decValue) {
		value = doOperation(value, decValue, '-');
	}

	/**
	 * Multiplies wrapped value with the given value.
	 * 
	 * @param mulValue
	 *            multiply value
	 * @throws RuntimeException
	 *             if operation can't be executed
	 */
	public void multiply(Object mulValue) {
		value = doOperation(value, mulValue, '*');
	}

	/**
	 * Divides wrapped value with the given value.
	 * 
	 * @param divValue
	 *            divisor value
	 * @throws RuntimeException
	 *             if operation can't be executed
	 * @throws ArithmeticException if divisor is 0.
	 */
	public void divide(Object divValue) {
		value = doOperation(value, divValue, '/');
	}

	/**
	 * Compares wrapped number value with the given number value.
	 * 
	 * @param withValue
	 *            value with which wrapped value will be compared
	 * @return the value 0 if withValue is numerically equal to this value; a
	 *         value less than 0 if this value is numerically less than
	 *         withValue; and a value greater than 0 if this value is
	 *         numerically greater than withValue.
	 * @throws RuntimeException
	 *             if any of compared values aren't numbers
	 */
	public int numCompare(Object withValue) {
		Object value1 = prepareValueForArithmetics(value);
		Object value2 = prepareValueForArithmetics(withValue);

		Double doubleValue1 = ((Number) value1).doubleValue();
		Double doubleValue2 = ((Number) value2).doubleValue();

		return doubleValue1.compareTo(doubleValue2);
	}

	/**
	 * Gets wrapped value.
	 * 
	 * @return wrapped value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets wrapped value.
	 * 
	 * @param value
	 *            set value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Tries to parse the number: if number has decimal point or 'e' number will
	 * be tried to parsed as double, else as integer.
	 * 
	 * @param s
	 *            string which will be attempted to parse as number
	 * @return parsed number
	 * @throws RuntimeException
	 *             if string can't be parsed as number
	 */
	private Object parseStringToValue(String s) {
		if (s.indexOf('.') != -1 || s.indexOf('e') != -1) {
			try {
				return Double.valueOf(s);
			} catch (NumberFormatException e) {
				// exception will be thrown
			}
		} else {
			try {
				return Integer.valueOf(s);
			} catch (NumberFormatException e) {
				// exception will be thrown
			}
		}

		throw new RuntimeException(String.format(
				"\"%s\" can't be parsed as number", s));
	}

	/**
	 * Prepares an Object for an arithmetic operation by the rules of this
	 * class. Converts a string to a number and throws an exception if operation
	 * can't be executed on the object.
	 * 
	 * @param o
	 *            Object to prepare
	 * @return prepared Object
	 * @throws RuntimeException if arithmetic operation can't be executed on the object
	 */
	private Object prepareValueForArithmetics(Object o) {
		if (o == null) {
			return Integer.valueOf(0);
		}
		if (!(o instanceof Integer) && !(o instanceof Double)
				&& !(o instanceof String)) {
			throw new RuntimeException(String.format(
					"Cant't do arithmetic operations with %s", o.getClass()
							.toString()));
		}
		if (o instanceof String) {
			return parseStringToValue((String) o);
		}

		return o;
	}

	/**
	 * Executes an operation on two Objects.
	 * @param value1 first value
	 * @param value2 second value
	 * @param operator operation's operator
	 * @return result of the operation
	 * @throws RuntimeException if operation can't be executed by the rules of this class
	 */
	private Object doOperation(Object value1, Object value2, char operator) {
		value1 = prepareValueForArithmetics(value1);
		value2 = prepareValueForArithmetics(value2);

		double primtiveValue1 = ((Number) value1).doubleValue();
		double primtiveValue2 = ((Number) value2).doubleValue();

		double result = 0;
		switch (operator) {
		case '+':
			result = primtiveValue1 + primtiveValue2;
			break;
		case '-':
			result = primtiveValue1 - primtiveValue2;
			break;
		case '*':
			result = primtiveValue1 * primtiveValue2;
			break;
		case '/':
			if (primtiveValue2 == 0) {
				throw new ArithmeticException("Can't divide with zero");
			}
			result = primtiveValue1 / primtiveValue2;
			break;

		}
		if (value1 instanceof Double || value2 instanceof Double) {
			return Double.valueOf(result);
		} else {
			return Integer.valueOf((int) result);
		}
	}

}
