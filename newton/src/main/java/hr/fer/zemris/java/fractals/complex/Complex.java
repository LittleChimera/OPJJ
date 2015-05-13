package hr.fer.zemris.java.fractals.complex;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a simple implementation of complex numbers. It provides basic
 * functionalities for doing operations with complex numbers. Class instances
 * are immutable.
 * 
 * @author Luka Skugor
 *
 */
public final class Complex {

	/**
	 * Complex number: 0 + i0
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * Complex number: 1 + i0
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * Complex number: -1 + i0
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * Complex number: 0 + i1
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * Complex number: 0 + -i1
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Real part of complex number.
	 */
	private double re;
	/**
	 * Imaginary part of complex number
	 */
	private double im;

	/**
	 * Creates a new complex number with given real and imaginary part.
	 * 
	 * @param re
	 *            real part of the complex number
	 * @param im
	 *            imaginary part of the complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Returns the module of the complex number.
	 * 
	 * @return module of the complex number
	 */
	public double module() {
		return Math.sqrt(im * im + re * re);
	}

	/**
	 * Multiplies this complex number with another one.
	 * 
	 * @param c
	 *            multiplier
	 * @return result of multiplication
	 */
	public Complex multiply(Complex c) {
		double re = this.re * c.re - this.im * c.im;
		double im = this.re * c.im + this.im * c.re;
		return new Complex(re, im);
	}

	/**
	 * Divides this complex number with another one.
	 * 
	 * @param c
	 *            divisor
	 * @return result of division
	 */
	public Complex divide(Complex c) {
		double denominator = c.im * c.im + c.re * c.re;
		if (denominator == 0) {
			throw new ArithmeticException("Division by zero.");
		}
		double re = this.re * c.re + this.im * c.im;
		re /= denominator;
		double im = this.im * c.re - this.re * c.im;
		im /= denominator;

		return new Complex(re, im);
	}

	/**
	 * Adds a complex number to this one.
	 * 
	 * @param c
	 *            added complex number
	 * @return result of addition
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}

	/**
	 * Subtracts a complex number from this one.
	 * 
	 * @param c
	 *            subtracted complex number
	 * @return result of subtraction
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}

	/**
	 * Returns a new complex number which is a negated value of this complex
	 * number (i.e. <code>-this</code>).
	 * 
	 * @return negated value of this complex number
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		double precision = 1e-6;
		boolean reZero = (Math.abs(re) < precision);
		boolean imZero = (Math.abs(im) < precision);
		if (reZero && imZero) {
			return "0";
		}

		StringBuilder stringBuilder = new StringBuilder();
		DecimalFormat firstRealFormatter = new DecimalFormat("#.####;-#.####");
		DecimalFormat firstImaginaryFormatter = new DecimalFormat(
				"i#.####;-i#.####");
		DecimalFormat imaginaryFormatter = new DecimalFormat(
				" + i#.####; - i#.####");

		if (!reZero) {
			stringBuilder.append(firstRealFormatter.format(re));
		}
		if (!imZero) {
			if (reZero) {
				stringBuilder.append(firstImaginaryFormatter.format(im));
			} else {
				stringBuilder.append(imaginaryFormatter.format(im));
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Gets imaginary part of this complex number.
	 * 
	 * @return imaginary part
	 */
	public double getImaginary() {
		return im;
	}

	/**
	 * Real part of this complex number.
	 * 
	 * @return real part
	 */
	public double getReal() {
		return re;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Complex)) {
			return false;
		}

		final double THETA = 1e-9;
		Complex comp = (Complex) obj;
		if (Math.abs(comp.re - this.re) < THETA
				&& Math.abs(comp.im - this.im) < THETA) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Parses a string for a complex number. General syntax for complex numbers
	 * is of form a+ib or a-ib where parts that are zero can be dropped, but not
	 * both (empty string is not legal complex number); for example, zero can be
	 * given as 0, i0, 0+i0, 0-i0. If there is 'i' present but no b is given, it
	 * is assumed that b=1. Additional spaces between elements are allowed
	 * except between 'i' and a number in the imaginary part of the complex
	 * number.
	 * 
	 * @param s parsed string
	 * @return parsed complex number
	 */
	public static Complex parseComplex(String s) {

		if (s.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty string given.");
		}

		/**
		 * real part of the complex number followed by spaces:
		 * ^([+\-]?\s*?(\d+\.?\d+|\d+))?\s* imaginary part of the complex
		 * number: ($|((^[+\-]?|[+\-])\s*?(\d+\.?\d+i|\d+i))?)$
		 */
		final Pattern complexPattern = Pattern
				.compile("^([+\\-]?\\s*?(\\d+\\.?\\d+|\\d+))?\\s*($|((^[+\\-]?|[+\\-])\\s*?(i\\d+\\.?\\d+|i\\d*))?)$");
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
				String noSpaces = matcher.group(3).replaceAll("[\\t\\r\\n ]",
						"");
				if (matcher.group(6).equals("i")) {
					imaginary = Double.parseDouble(noSpaces.replace("i", "1"));
				} else {
					imaginary = Double.parseDouble(noSpaces.replace("i", ""));
				}
			} catch (Exception noImaginaryPart) {
				imaginary = 0;
			}
		} else {
			throw new NumberFormatException(
					"Given string is not a standard complex number");
		}

		return new Complex(real, imaginary);
	}

}
