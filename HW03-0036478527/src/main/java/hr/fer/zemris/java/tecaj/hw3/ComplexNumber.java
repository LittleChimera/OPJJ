package hr.fer.zemris.java.tecaj.hw3;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ComplexNumber allows calculations with complex numbers. It supports basic
 * operations like addition, subtraction, multiplication, division, powering,
 * rooting.
 * 
 * It also offers some additional methods for creating a complex number from
 * various Objects.
 * 
 * Note: ComplexNumber is immutable.
 * 
 * @author Luka Skugor
 *
 */
public class ComplexNumber {

	/**
	 * Value of the real part of the complex number.
	 */
	private final double realComponent;
	/**
	 * Value of the imaginary part of the complex number.
	 */
	private final double imaginaryComponent;

	/**
	 * Creates a new ComplexNumber.
	 * 
	 * @param realComponent
	 *            value of the real part of the complex number
	 * @param imaginaryComponent
	 *            value of the imaginary part of the complex number
	 */
	public ComplexNumber(double realComponent, double imaginaryComponent) {
		this.realComponent = realComponent;
		this.imaginaryComponent = imaginaryComponent;
	}

	/**
	 * Creates a new complex number from a real part of the complex number.
	 * Imaginary part is set to zero.
	 * 
	 * @param realComponent
	 *            value of the real part of the complex number
	 * @return new complex number with ReZ = realComponent, ImZ = 0
	 */
	public static ComplexNumber fromReal(double realComponent) {
		return new ComplexNumber(realComponent, 0.0);
	}

	/**
	 * Creates a new complex number from an imaginary part of the complex
	 * number. Real part is set to zero.
	 * 
	 * @param imaginaryComponent
	 *            value of the imaginary part of the complex number
	 * @return new complex number with ReZ = 0, ImZ = imaginaryComponent
	 */
	public static ComplexNumber fromImaginary(double imaginaryComponent) {
		return new ComplexNumber(0.0, imaginaryComponent);
	}

	/**
	 * Creates a new complex number from trigonometric format of a complex
	 * number.
	 * 
	 * @param magnitude
	 *            magnitude of the complex number in a trigonometric format
	 * @param angle
	 *            angle of the complex number in a trigonometric format
	 * @return created ComplexNumber
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude,
			double angle) {

		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);

		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Parses the String and creates a ComplexNumber from parsed variables.
	 * 
	 * Allowed string are in following format:
	 * 
	 * (+|-)(realPartOfComplexNumber)(+|-)(imaginaryPartOfComplexNumber)i
	 * 
	 * Spaces between groups [()] are allowed.
	 * 
	 * 
	 * @param s String representation of a complex number
	 * @return parsed ComplexNumber
	 * @throws NumberFormatException if the String s can't be parsed to a ComplexNumber
	 */
	public static ComplexNumber parse(String s) {
		/**
		 * real part of the complex number followed by spaces:
		 * ^([+\-]?\s*?(\d+\.?\d+|\d+))?\s*
		 * imaginary part of the complex number:
		 * ($|((^[+\-]?|[+\-])\s*?(\d+\.?\d+i|\d+i))?)$
		 */
		final Pattern complexPattern = Pattern
				.compile("^([+\\-]?\\s*?(\\d+\\.?\\d+|\\d+))?\\s*($|((^[+\\-]?|[+\\-])\\s*?(\\d+\\.?\\d+i|\\d+i))?)$");
		Matcher matcher = complexPattern.matcher(s.trim());

		double real, imaginary;
		if (matcher.matches()) {
			try {
				real = Double.parseDouble(matcher.group(1).replaceAll(
						"[\\t\\r\\n ]", ""));
			} catch (Exception noRealPart) {
				real = 0;
			}
			try {
				imaginary = Double.parseDouble(matcher.group(3)
						.replaceAll("[\\t\\r\\n ]", "").replace("i", ""));
			} catch (Exception noImaginaryPart) {
				imaginary = 0;
			}
		} else {
			throw new NumberFormatException(
					"Given string is not a standard complex number");
		}

		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Gets the value of the real part of the complex number.
	 * @return ReZ
	 */
	public double getReal() {
		return realComponent;
	}
	
	/**
	 * Gets the value of the imaginary part of the complex number.
	 * @return ImZ
	 */
	public double getImaginary() {
		return imaginaryComponent;
	}
	
	/**
	 * Gets the magnitude of the trigonometric expression of the complex number.
	 * @return magnitude
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(realComponent, 2)
				+ Math.pow(imaginaryComponent, 2));
	}
	
	/**
	 * Gets the angle of the trigonometric expression of the complex number.
	 * @return angle
	 */
	public double getAngle() {
		if (realComponent == 0) {
			return Math.signum(imaginaryComponent) * Math.PI / 2;
		}

		double angle = Math.atan(imaginaryComponent / realComponent);
		if (realComponent < 0) {
			if (angle < 0) {
				angle += Math.PI;
			} else {
				angle -= Math.PI;
			}
		}

		return angle;
	}
	
	/**
	 * Adds two complex numbers.
	 * @param c complex number to add to this ComplexNumber
	 * @return result
	 */
	public ComplexNumber add(ComplexNumber c) {
		double realAdd = this.realComponent + c.getReal();
		double imaginaryAdd = this.imaginaryComponent + c.getImaginary();

		return new ComplexNumber(realAdd, imaginaryAdd);
	}
	
	/**
	 * Subtracts another complex number from this complex number.
	 * @param c complex number to subtract from this ComplexNumber
	 * @return result
	 */
	public ComplexNumber sub(ComplexNumber c) {
		double realSub = this.realComponent - c.getReal();
		double imaginarySub = this.imaginaryComponent - c.getImaginary();

		return new ComplexNumber(realSub, imaginarySub);
	}
	
	/**
	 * Multiplies this complex number with another one.
	 * @param c complex number with which this Complex number is multiplied
	 * @return result
	 */
	public ComplexNumber mul(ComplexNumber c) {
		double realMul = this.realComponent * c.realComponent
				- this.imaginaryComponent * c.imaginaryComponent;
		double imaginaryMul = this.realComponent * c.imaginaryComponent
				+ this.imaginaryComponent * c.realComponent;

		return new ComplexNumber(realMul, imaginaryMul);
	}
	
	/**
	 * Divides this complex number with another one.
	 * @param c complex number with which this Complex number is divided
	 * @return result
	 * @throws ArithmeticException if c is zero
	 */
	public ComplexNumber div(ComplexNumber c) {
		if (c.getReal() == 0 && c.getImaginary() == 0) {
			throw new ArithmeticException("Can't divide with zero!");
		}
		double realDiv = (this.realComponent * c.getReal() + this.imaginaryComponent
				* c.getImaginary())
				/ (Math.pow(c.realComponent, 2) + Math.pow(
						c.imaginaryComponent, 2));
		double imaginaryDiv = (this.realComponent * c.getImaginary() - this.imaginaryComponent
				* c.getReal())
				/ (Math.pow(c.realComponent, 2) + Math.pow(
						c.imaginaryComponent, 2));

		return new ComplexNumber(realDiv, imaginaryDiv);
	}
	
	/**
	 * Calculates nth power of this complex number.
	 * @param n level of power
	 * @return result
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Power must be at least 0");
		}
		double angle = getAngle();
		double magnitude = getMagnitude();

		double magnitudePow = Math.pow(magnitude, n);
		double anglePow = n * angle;
		double realPow = magnitudePow * Math.cos(anglePow);
		double imaginaryPow = magnitudePow * Math.sin(anglePow);

		return new ComplexNumber(realPow, imaginaryPow);
	}
	
	/**
	 * Calculates nth roots of this complex number.
	 * @param n level of root
	 * @return array of results
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(
					"Can't require less than 1st root");
		}

		double angle = getAngle();
		double magnitude = getMagnitude();

		double magnitudeRoot = Math.pow(magnitude, 1. / n);

		ComplexNumber[] result = new ComplexNumber[n];
		for (int ithRoot = 0; ithRoot < n; ithRoot++) {
			double angleRoot = (angle + 2 * ithRoot * Math.PI) / n;
			double realRoot = magnitudeRoot * Math.cos(angleRoot);
			double imaginaryRoot = magnitudeRoot * Math.sin(angleRoot);

			result[ithRoot] = new ComplexNumber(realRoot, imaginaryRoot);
		}

		return result;
	}

	@Override
	public String toString() {
		DecimalFormat imaginaryFormatter = new DecimalFormat(
				" + #.####i; - #.####i");
		return realComponent + imaginaryFormatter.format(imaginaryComponent);
	}
}
