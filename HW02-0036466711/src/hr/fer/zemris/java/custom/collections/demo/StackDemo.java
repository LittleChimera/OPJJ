package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Provides a basic postfix integer calculator with +,-,/,* and % operations.
 * 
 * @author Mihovil Vinković
 *
 */
public class StackDemo {

	/**
	 * See class description.
	 * 
	 * @param args
	 *            Integers and operators to be used for calculations.
	 */
	public static void main(String[] args) throws EmptyStackException {

		ObjectStack stack = new ObjectStack();

		int length = args.length;
		if (length < 1) {
			System.err.println("No command line arguments.");
			System.exit(0);
		}
		for (int i = 0; i < length; i++) {
			try {
				int num = Integer.parseInt(args[i]);
				stack.push(num);
			} catch (NumberFormatException e) {
				Integer a = 0, b = 0;
				try {
					a = (Integer) stack.pop();
					b = (Integer) stack.pop();
				} catch (EmptyStackException e1) {
					System.err
							.println("Not enough numbers to pop. Reorder your equation.");
					System.exit(-1);
				}
				switch (args[i]) {
				case "+":
					stack.push(a + b);
					break;
				case "-":
					stack.push(b - a);
					break;
				case "/":
					try {
						stack.push(b / a);
					} catch (ArithmeticException e2) {
						System.err.println("Division by zero is not allowed.");
						System.exit(-1);
					}
					break;
				case "*":
					stack.push(a * b);
					break;
				case "%":
					try {
						stack.push(b % a);
					} catch (ArithmeticException e2) {
						System.err.println("Division by zero is not allowed.");
						System.exit(-1);
					}
					break;
				default:
					System.err
							.println("This program only supports *, /, +, - and % as operators.");
					System.exit(-1);
				}
			}
		}

		if (stack.size() != 1) {
			System.err.printf("Stack size not 1, something went wrong.");
		} else {
			System.out.println(stack.pop());
		}
	}
}
