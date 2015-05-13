package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

import java.lang.String;

/**
 * Class accepts single line argument command.Class supports basic operators
 * "+", "-", "*", "/", "%". Each operator takes two precceding numbers and
 * replaces them with operation result.
 * 
 * @author Borna Feldšar
 *
 */
public class Demo {
	/**
	 * Main method that executes class.
	 * @param args program arguments
	 * @throws NumberFormatException if number on stack isn't integer
	 * @throws EmptyStackException if stack is empty
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("You need to enter one argument");
			System.exit(1);
		}

		ObjectStack stack = new ObjectStack();
		String[] elements = args[0].split(" +");
		for (String element : elements) {

			try {
				int number = Integer.parseInt(element);
				stack.push(number);
				continue;
			} catch (NumberFormatException e) {
				if (!(element.equals("+") || element.equals("-")
						|| element.equals("/") || element.equals("*") || element
							.equals("%"))) {
					System.err.println("Illegal argument");
					System.exit(1);
				}
			}

			try {
				int number1 = (int) stack.pop();
				int number2 = (int) stack.pop();

				int result = 0;
				switch (element) {

				case "+": {
					result = number1 + number2;
					break;
				}
				case "-": {
					result = number2 - number1;
					break;
				}
				case "/": {
					if (number1 == 0) {
						System.out.println("You cant divide by zero");
						System.exit(1);
					}
					result = number2 / number1;
					break;
				}
				case "*": {
					result = number1 * number2;
					break;
				}
				case "%": {
					if (number1 == 0) {
						System.out.println("You cant divide by zero");
						System.exit(1);
					}
					result = number2 % number1;
					break;
				}
				default: {
					System.err.println("Illegal argument");
					System.exit(1);

				}
				}
				stack.push(result);
			} catch (NumberFormatException e1) {
				System.err.println("Object on stack isn't integer!");
				System.exit(1);
			} catch (EmptyStackException e2) {
				System.err.println("Stack is empty!");
				System.exit(1);
			}
		}

		if (stack.size() != 1) {
			System.err.println("Invalid entered number of entered numbers!");
		} else {
			System.out.println("Expression evaluates to " + stack.pop());
		}
	}

}
