package hr.fer.zemris.java.tecaj.hw1;

import java.text.DecimalFormat;

/**
 * The program accepts three command-line arguments: real part of complex
 * number, imaginary part of complex number, and required root to calculate
 * (natural number greater than 1). The program computes and prints all
 * requested roots of given complex number (also in form: real part plus
 * imaginary part).
 * 
 * @author Luka Skugor
 * @version 1.0
 */
public class Roots {

	/**
	 * Called on program start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Invalid number of arguments!");
			System.exit(1);
		}

		double realZ = Double.parseDouble(args[0]);
		double imaginaryZ = Double.parseDouble(args[1]);
		int root = Integer.parseInt(args[2]);

		if (root <= 1) {
			System.out.println("Root needs to be greater than 1.");
			System.exit(1);
		}

		double angleZ;
		if (realZ != 0) {
			angleZ = Math.atan(imaginaryZ / realZ);
		} else {
			angleZ = Math.PI/2;
		}
		double rootRadius = Math.pow(
				Math.pow(realZ * realZ + imaginaryZ * imaginaryZ, 1. / 2),
				1. / root);

		DecimalFormat realZFormat = new DecimalFormat("#0.##;-#0.##");
		DecimalFormat imaginaryZFormat = new DecimalFormat("+ #0.##i;- #0.##i");

		for (int nthRoot = 0; nthRoot < root; nthRoot++) {
			double rootAngle = (angleZ + 2 * nthRoot * Math.PI) / root;
			double rootRealZ = rootRadius * Math.cos(rootAngle);
			double rootImaginaryZ = rootRadius * Math.sin(rootAngle);

			System.out.format("%d) %s %s%n", nthRoot + 1,
					realZFormat.format(rootRealZ),
					imaginaryZFormat.format(rootImaginaryZ));
		}
	}

}
