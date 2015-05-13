package hr.fer.zemris.java.tecaj.hw1;

/**
 * The program accepts a single command-line argument: a natural number greater
 * than 1. The program calculates and prints the decomposition of this number
 * onto prime factors.
 * 
 * @author Luka Skugor
 * @version 1.0
 */
public class NumberDecomposition {

	/**
	 * Called on program start.
	 * @param args arguments from command line
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Invalid number of arguments!");
			System.exit(1);
		}

		long toDecompose = Long.parseLong(args[0]);
		if (toDecompose <= 1) {
			System.out.println("Program accepts only natural numbers greater than 1");
			System.exit(1);
		}

		System.out.format(
				"%d will be decomposed. Here are his prime factors:%n",
				toDecompose);

		long decomposed = toDecompose;
		int primeFactor = 2;
		Decomposition: for (int counter = 1; decomposed > 1; counter++) {
			while (decomposed % primeFactor != 0) {
				
				if (primeFactor == 2) {
					primeFactor += 1;
				} else {
					primeFactor += 2;
				}

				if (primeFactor > toDecompose / 2) {
					break Decomposition;
				}
			}
			
			decomposed /= primeFactor;
			printAsNth(primeFactor, counter);
		}
		
		//Given number is prime number
		if (decomposed != 1) {
			printAsNth(toDecompose, 1);
		}
	}
	
	/**
	 * Prints a line in format "%d. %d". E.g. "21. 133"
	 * @param number nth element of the sequence
	 * @param n index in sequence
	 */
	public static void printAsNth(long number, int n) {
		System.out.format("%d. %d%n", n, number);
	}

}
