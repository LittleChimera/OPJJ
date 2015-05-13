package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Demo using ObjectStack.
 * 
 * @author Luka Skugor
 *
 */
public class StackDemo {

	/**
	 * Run on demo start.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		String expression = null;
		try {
			expression = args[0].trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.exit(1);
		}

		ObjectStack stack = new ObjectStack();

		String[] expressionElements = expression.split(" +");
		for (String element : expressionElements) {
			element = element.trim();

			try {
				int number = Integer.parseInt(element);
				stack.push(Integer.valueOf(number));

			} catch (NumberFormatException notANumber) {
				// element is not a number but operation
				try {
					int a = (int) stack.pop();
					int b = (int) stack.pop();

					Integer result = executeOperation(element, b, a);

					stack.push(result);
				} catch (EmptyStackException lackOfElements) {
					System.out.println("Invalid expression sequence.");
					System.exit(2);
				} catch (IllegalArgumentException unknownOperation) {
					System.out.println("Operation \"" + element
							+ "\" is not supported.");
					System.exit(3);
				}
			}
		}

		if (stack.size() > 1) {
			System.out.println("Expression is invalid.");
			System.exit(1);
		} else {
			System.out.println("Expression evaluates to " + stack.pop() + ".");
		}
	}

	/**
	 * Executes an arithmetic operation between two integers
	 * 
	 * @param operation
	 *            operation to be performed
	 * @param a
	 *            first integer
	 * @param b
	 *            second integer
	 * @return a (operation) b
	 */
	private static int executeOperation(String operation, int a, int b) {
		if (operation.length() > 1) {
			throw new IllegalArgumentException("Unknown operation.");
		}
		char operationChar = operation.charAt(0);

		int result;
		switch (operationChar) {
		case '+':
			result = a + b;
			break;
		case '-':
			result = a - b;
			break;
		case '*':
			result = a * b;
			break;
		case '/':
			if (b == 0) {
				throw new IllegalArgumentException("Division by zero.");
			}
			result = a / b;
			break;
		case '%':
			result = a % b;
			break;
		default:
			throw new IllegalArgumentException("Operation is not supported.");
		}

		return Integer.valueOf(result);
	}
}
