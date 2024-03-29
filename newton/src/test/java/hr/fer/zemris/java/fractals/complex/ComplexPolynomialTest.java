package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;
import hr.fer.zemris.java.fractals.complex.Complex;

import org.junit.Test;

public class ComplexPolynomialTest {
	private static final double THETA = 1e-9;

	private final static ComplexPolynomial cp = new ComplexPolynomial(
			Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO,
			Complex.ONE_NEG);
	private final static ComplexPolynomial cp1 = new ComplexPolynomial(
			Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO,
			Complex.ONE_NEG);

	@Test
	public void deriveTest() {
		ComplexPolynomial derivative = cp.derive();
		assertEquals("4*z^3", derivative.toString());
	}

	@Test
	public void applyTest() {
		Complex result = cp.apply(new Complex(1, 1));
		assertEquals(0, result.sub(new Complex(-5, 0)).module(), THETA);
	}

	@Test
	public void toStringTest() {
		assertEquals("1*z^4-1", cp.toString());
	}
	
	@Test
	public void toStringTestStartingWithNegativeRealWithoutComplexPart() {
		assertEquals("-3*z^2+1*z^1+i1", new ComplexPolynomial(new Complex(-3, 0), Complex.ONE, Complex.IM).toString());
	}
	
	@Test
	public void toStringTestStartingWithNegativeImaginaryWithoutRealPart() {
		assertEquals("-i3*z^2+1*z^1+i1", new ComplexPolynomial(new Complex(0, -3), Complex.ONE, Complex.IM).toString());
	}
	
	@Test
	public void toStringTestFullComplexNumbers() {
		assertEquals("-(1 + i3)*z^2+(1 - i1)*z^1-(1 - i1)", new ComplexPolynomial(new Complex(-1, -3), Complex.ONE.add(Complex.IM_NEG), Complex.IM.add(Complex.ONE_NEG)).toString());
	}

	@Test
	public void normalizeTest() {
		assertEquals(new ComplexPolynomial(Complex.ONE, Complex.ZERO,
				Complex.ONE_NEG).toString(), cp1.toString());
	}

	@Test
	public void multiplyTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(Complex.ONE, new Complex(
				3, 0), new Complex(5, 0));
		ComplexPolynomial cp2 = new ComplexPolynomial(new Complex(3, 0),
				new Complex(5, 0), new Complex(4, 0));
		ComplexPolynomial expected = new ComplexPolynomial(new Complex(3, 0),
				new Complex(14, 0), new Complex(34, 0), new Complex(37, 0),
				new Complex(20, 0));
		assertEquals(expected.toString(), cp1.multiply(cp2).toString());
	}

	@Test
	public void orderTestAfterNormalization() {
		assertEquals(2, cp1.order());
	}

	@Test
	public void orderTestWihtoutNormalization() {
		assertEquals(4, cp.order());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructFailNoFactors() {
		new ComplexPolynomial();
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructFailNoFactorsAfterNormalization() {
		new ComplexPolynomial(Complex.ZERO, Complex.ZERO, Complex.ZERO,
				Complex.ZERO);
	}

}
