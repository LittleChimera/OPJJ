/**
 * 
 */
package hr.fer.zemris.java.tecaj.hw1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Program takes two arguments. First is width and second is height of a
 * rectangle. Arguments can be given through command line or through standard
 * system input. Both arguments need to be positive numbers. Program prints area
 * and perimeter for given rectangle. If any error occurs, appropriate message
 * is printed.
 * 
 * @author Luka Skugor
 * @version 1.0
 */
public class Rectangle {

	/**
	 * The method is called on program start
	 * 
	 * @param args
	 *            arguments from command line
	 * @throws IOException -
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(System.in)));

		double width = 0.0, height = 0.0;

		switch (args.length) {
		case 0:
			width = readDimension(reader, "width");
			height = readDimension(reader, "height");
			break;
		case 2:
			width = parseDimension(args[0], "width");
			height = parseDimension(args[1], "height");
			if (height < 0 || width < 0) {
				System.exit(1);
			}
			break;
		default:
			System.out.println("Invalid number of arguments");
			System.exit(1);
		}

		System.out
				.format("You have specified a rectangle with width of %.1f and height of %.1f. Its area is %.1f and its perimeter is %.1f%n",
						width, height, width * height, 2 * width + 2 * height);

	}

	/**
	 * Reads non-negative dimension from standard input and parses it as double.
	 * Prints error message if input is empty or if the value of dimension is
	 * negative.
	 * 
	 * @param reader
	 *            reads from standard input
	 * @param dimensionName
	 *            name of dimension which user needs to input
	 * @return double representation of read value
	 * @throws IOException -
	 */
	public static double readDimension(BufferedReader reader,
			String dimensionName) throws IOException {

		double readValue = -1;

		while (true) {
			System.out.print("Please provide " + dimensionName + ": ");
			String inputLine = reader.readLine();

			readValue = parseDimension(inputLine, dimensionName);

			if (readValue >= 0) {
				break;
			}
		}

		return readValue;
	}

	/**
	 * Parses non-negative dimension as value. Prints error message if input is
	 * empty or if the value of dimension is negative.
	 * 
	 * @param dimension
	 *            value as a String
	 * @param dimensionName
	 *            name of parsing dimension
	 * @return dimension parsed as double or -1 if error occurred
	 */
	public static double parseDimension(String dimension, String dimensionName) {

		if (dimension == null || dimension.trim().isEmpty()) {
			System.out.println("Nothing was given.");
			return -1;
		}

		double dimensionValue = Double.parseDouble(dimension.trim());

		if (dimensionValue < 0) {
			String negativeValueMsg = dimensionName + " is negative.";
			// capitalize message
			negativeValueMsg = negativeValueMsg.substring(0, 1).toUpperCase()
					+ negativeValueMsg.substring(1);
			System.out.println(negativeValueMsg);
		}

		return dimensionValue;
	}

}
