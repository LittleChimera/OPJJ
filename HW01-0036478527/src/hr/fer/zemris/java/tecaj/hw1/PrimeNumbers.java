package hr.fer.zemris.java.tecaj.hw1;

/**
 * The program accepts a single command-line argument: a number n (n>0), and
 * computes and prints first n prime numbers. Considers 2 to be the first
 * prime number.
 * 
 * @author Luka Skugor
 * @version 1.0
 */
public class PrimeNumbers {
	
	/**
	 * Called on program start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments!");
			System.exit(1);
		}

		int firstN = Integer.parseInt(args[0]);
		if (firstN < 1) {
			System.out.println("Invalid argument - must be greater or equal than 1.");
			System.exit(1);
		}

		long expectedPrime = 1;
		for (int counter = 0; counter < firstN; counter++) {
			do {
				if (expectedPrime <= 2) {
					expectedPrime += 1;
				} else {
					expectedPrime += 2;
				}
			} while (!isPrime(expectedPrime));
			System.out.format("%d. %d%n", counter + 1, expectedPrime);
		}

	}

	/**
	 * Tests if the given number is prime.
	 * @param testForPrime tested value
	 * @return true if the number is prime number, else false
	 */
	public static boolean isPrime(long testForPrime) {
		if (testForPrime < 2) {
			return false;
		}

		int divider = 2;
		while (divider * divider <= testForPrime) {
			if (testForPrime % divider == 0) {
				return false;
			}

			if (divider == 2) {
				divider += 1;
			} else {
				divider += 2;
			}
		}

		return true;
	}

}
