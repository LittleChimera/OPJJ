package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Shows basic functionality of a stack, by evaluating a single command-line string
 * in post-fix notation.
 * 
 * <p> The command line string should be enclosed in "". 
 * If there are more than one command-line arguments, program does nothing.
 * 
 * @author Erik Banek
 */
public class StackDemo {
	
	/**
	 * Handles incorrect input, and outputs the result of evaluating string.
	 * Handles some errors with messages.
	 * 
	 * <p> Uses implemented ObjectStack to evaluate given post-fix expression. 
	 * 
	 * @param args array with a single string to be evaluated.
	 */
	public static void main(String[] args) {
		if(!incorrectInputCheck(args)) {
			return;
		}
		
		String inputString = args[0];
		String[] splitPartsOfInput = inputString.split("[\\s\"]");
		for (int i=0; i<splitPartsOfInput.length; i++) {
			splitPartsOfInput[i].trim();
		}

		ObjectStack stack = new ObjectStack();
		
		int result = 0;
		try {
			
			for (int i=0; i<splitPartsOfInput.length; i++) {
				if (splitPartsOfInput[i].isEmpty()) continue;
				if (!isInteger(splitPartsOfInput[i])) {
					int first = (int) stack.pop();
					int second = (int) stack.pop();
					
					//currently no better way than just hack-and-slash
					if (splitPartsOfInput[i].startsWith("/")) {
						stack.push(second/first);
					} else if (splitPartsOfInput[i].startsWith("*")) {
						stack.push(second*first);
					} else if (splitPartsOfInput[i].startsWith("+")) {
						stack.push(second+first);
					} else if (splitPartsOfInput[i].startsWith("-")) {
						stack.push(second-first);
					}
				} else {
					int toBeOnStack = Integer.parseInt(splitPartsOfInput[i]);
					stack.push(toBeOnStack);
				}
			}
			if (stack.size() > 1) {
				System.err.println("The expression is not validA.");
				System.exit(1);
			}
			result = (int) stack.pop();
			
		} catch (EmptyStackException e) {
			System.err.println("The expression is not validB.");
			System.exit(1);
		} catch (ArithmeticException e) {
			System.err.println("The expression divides by zero.");
			System.exit(1);
		} 
		
		System.out.println("The evaluated expression is: " + result);
	}
	
	/**
	 * Checks if given string contains an integer by going through every
	 * char in string and checking if it is a digit. Also correctly handles
	 * negative integers.
	 * 
	 * <p> Found on: 
	 * http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
	 * 
	 * @param forParse string to check.
	 * @return true if string contains a number.
	 */
	static boolean isInteger(String forParse) {
		if (forParse.isEmpty()) return false;
		for(int i=0; i<forParse.length(); i++) {
			//case when number is negative
		    if(i == 0 && forParse.charAt(i) == '-') {
		        if(forParse.length() == 1) {
		        	return false;
		        }
		        else continue;
		    }
		        if(Character.digit(forParse.charAt(i),10) < 0) {
		        	return false;
		        }
		}
		return true;
	}
	
	/**
	 * Checks the validity of input.
	 * 
	 * @param args command line input.
	 * @return false if input is incorrect, true otherwise.
	 */
	static boolean incorrectInputCheck(String[] args) {
		if (args.length != 1) {
			System.err.println("Wrong number of command line arguments! "
					+ "Please restart the program with a single string "
					+ "written as follows:%n \"{text of string}\"");
			return false;
		}
		
		String inputString = args[0];
		
		if (inputString.length() < 2) {
			System.err.println("Please restart the program with a single string "
					+ "written as follows:%n \"{text of string}\"");
			return false;
		}
		return true;
	}
}
