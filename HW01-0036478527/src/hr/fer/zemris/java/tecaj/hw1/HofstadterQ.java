package hr.fer.zemris.java.tecaj.hw1;

/**
 * Calculates i-th number of Hofstadter's Q sequence. The program accepts i as
 * command line argument. This argument must be positive – if not, error is
 * reported.
 * 
 * @author Luka Skugor
 * @version 1.0
 */
public class HofstadterQ {

	public static void main(String[] args) {
		
		if (args.length == 1) {

			int index = Integer.parseInt(args[0]);

			if (index <= 0) {
				System.out.println("Value needs to be positive");
			} else {
				System.out.format(
						"%dth number of Hofstadter's Q-sequence is %d%n",
						index, hofstadterQ(index));
			}
		} else {
			System.out.println("Invalid number of arguments");
		}

	}

	/**
	 * Finds nth element of Hofstadter's Q sequence using following equation:
	 * Q(n)=Q(n-Q(n-1))+Q(n-Q(n-2)) with initialized values Q(1)=Q(2)=1
	 * 
	 * @param n
	 *            nth element is requested
	 * @return nth element of the sequence
	 */
	public static long hofstadterQ(int n) {
		long hofstadterQSequence[] = new long[n + 2];
		hofstadterQSequence[1] = 1;
		hofstadterQSequence[2] = 1;

		for (int kth = 3; kth <= n; kth++) {
			hofstadterQSequence[kth] = hofstadterQSequence[(int) (kth - hofstadterQSequence[kth - 1])]
					+ hofstadterQSequence[(int) (kth - hofstadterQSequence[kth - 2])];
		}

		return hofstadterQSequence[n];
	}

}
