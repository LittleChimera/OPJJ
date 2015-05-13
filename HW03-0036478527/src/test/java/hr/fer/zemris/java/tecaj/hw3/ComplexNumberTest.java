package hr.fer.zemris.java.tecaj.hw3;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the ComplexNumber class.
 * @author luka
 *
 */
public class ComplexNumberTest {
	
	private static final double DELTA = 10E-6;
	
	@Test
	public void parserTestIntInt() {
		ComplexNumber cn1 = ComplexNumber.parse("3 + 2i");
		assertTrue(cn1.getImaginary() == 2 && cn1.getReal() == 3);
	}
	
	@Test
	public void parserTestIntDouble() {
		ComplexNumber cn1 = ComplexNumber.parse("3 + 2.22i");
		assertTrue(cn1.getImaginary() == 2.22 && cn1.getReal() == 3);
	}
	
	@Test
	public void parserTestDoubleInt() {
		ComplexNumber cn1 = ComplexNumber.parse("33.33 + 2i");
		assertTrue(cn1.getImaginary() == 2 && cn1.getReal() == 33.33);
	}
	
	@Test
	public void parserTestDoubleDouble() {
		ComplexNumber cn1 = ComplexNumber.parse("3.76 + 2.14i");
		assertTrue(cn1.getImaginary() == 2.14 && cn1.getReal() == 3.76);
	}
	
	@Test
	public void parserTestNoneDouble() {
		ComplexNumber cn1 = ComplexNumber.parse(" + 2.56i");
		assertTrue(cn1.getImaginary() == 2.56 && cn1.getReal() == 0);
	}
	
	@Test
	public void parserTestDoubleNone() {
		ComplexNumber cn1 = ComplexNumber.parse("-3.66 ");
		assertTrue(cn1.getImaginary() == 0 && cn1.getReal() == -3.66);
	}
	
	@Test
	public void parserTestNoneNone() {
		ComplexNumber cn1 = ComplexNumber.parse("   ");
		assertTrue(cn1.getImaginary() == 0 && cn1.getReal() == 0);
	}
	
	@Test(expected=NumberFormatException.class)
	public void parserTestJustAnOperator() {
		ComplexNumber.parse(" -  ");
	}
	
	@Test
	public void fromReal() {
		ComplexNumber cn1 = ComplexNumber.fromReal(3.33);
		assertTrue(Math.abs(3.33 - cn1.getReal()) < DELTA);
	}
	
	@Test
	public void fromImaginary() {
		ComplexNumber cn1 = ComplexNumber.fromImaginary(3.33);
		assertTrue(Math.abs(3.33 - cn1.getImaginary()) < DELTA);
	}
	
	@Test
	public void fromMagnitudeAndAngle1stQuadrant() {
		ComplexNumber cn1 = ComplexNumber.fromMagnitudeAndAngle(5, 1./4*Math.PI);
		assertTrue(Math.abs(cn1.getImaginary() - 5*Math.sqrt(2)/2) < DELTA 
				&& Math.abs(cn1.getReal() - 5*Math.sqrt(2)/2) < DELTA);
	}
	
	@Test
	public void fromMagnitudeAndAngle2ndQuadrant() {
		ComplexNumber cn1 = ComplexNumber.fromMagnitudeAndAngle(5, 3./4*Math.PI);
		assertTrue(Math.abs(cn1.getImaginary() - 5*Math.sqrt(2)/2) < DELTA 
				&& Math.abs(cn1.getReal() + 5*Math.sqrt(2)/2) < DELTA);
	}
	
	@Test
	public void fromMagnitudeAndAngle3rdQuadrant() {
		ComplexNumber cn1 = ComplexNumber.fromMagnitudeAndAngle(5, 5./4*Math.PI);
		assertTrue(Math.abs(cn1.getImaginary() + 5*Math.sqrt(2)/2) < DELTA 
				&& Math.abs(cn1.getReal() + 5*Math.sqrt(2)/2) < DELTA);
	}
	
	@Test
	public void fromMagnitudeAndAngle4thQuadrant() {
		ComplexNumber cn1 = ComplexNumber.fromMagnitudeAndAngle(5, 7./4*Math.PI);
		assertTrue(Math.abs(cn1.getImaginary() + 5*Math.sqrt(2)/2) < DELTA 
				&& Math.abs(cn1.getReal() - 5*Math.sqrt(2)/2) < DELTA);
	}
	
	@Test
	public void getAngle1stQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(2, 2);
		assertTrue(Math.abs(cn1.getAngle() - 1./4*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle2ndQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(-2, 2);
		assertTrue(Math.abs(cn1.getAngle() - 3./4*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle3rdQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(-2, -2);
		assertTrue(Math.abs(cn1.getAngle() + 3./4*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle4thQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(2, -2);
		assertTrue(Math.abs(cn1.getAngle() + 1./4*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle1st2ndQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(0, 2);
		assertTrue(Math.abs(cn1.getAngle() - 1./2*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle2nd3rdQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(-2, 0);
		assertTrue(Math.abs(cn1.getAngle() + Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle3rd4thQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(0, -2);
		assertTrue(Math.abs(cn1.getAngle() + 1./2*Math.PI) < DELTA);
	}
	
	@Test
	public void getAngle4th1stQuadrant() {
		ComplexNumber cn1 = new ComplexNumber(2, 0);
		assertTrue(Math.abs(cn1.getAngle() + 0) < DELTA);
	}
	
	@Test
	public void addTest() {
		ComplexNumber cn1 = new ComplexNumber(2, 3);
		ComplexNumber cn2 = new ComplexNumber(-4, 6);
		ComplexNumber result = cn1.add(cn2);
		assertTrue(result.getImaginary() == 9 && result.getReal() == -2);
	}
	
	@Test
	public void subTest() {
		ComplexNumber cn1 = new ComplexNumber(2, 3);
		ComplexNumber cn2 = new ComplexNumber(-4, 6);
		ComplexNumber result = cn1.sub(cn2);
		assertTrue(result.getImaginary() == -3 && result.getReal() == 6);
	}
	
	@Test
	public void multiplyTest() {
		ComplexNumber cn1 = new ComplexNumber(2.3, 3.7);
		ComplexNumber cn2 = new ComplexNumber(-4, 7);
		ComplexNumber result = cn1.mul(cn2);
		assertTrue(Math.abs(result.getImaginary() - 1.3) < DELTA
				&& Math.abs(result.getReal() + 35.1) < DELTA);
	}
	
	@Test
	public void divideTest() {
		ComplexNumber cn1 = new ComplexNumber(2.3, 3.7);
		ComplexNumber cn2 = new ComplexNumber(-4, 7);
		ComplexNumber result = cn1.div(cn2);
		assertTrue(Math.abs(result.getImaginary() - 0.47538461538) < DELTA
				&& Math.abs(result.getReal() - 0.2569230769) < DELTA);
	}
	
	@Test(expected=ArithmeticException.class)
	public void divideWithZero() {
		ComplexNumber cn1 = new ComplexNumber(2, 3);
		ComplexNumber cn2 = new ComplexNumber(0, 0);
		cn1.div(cn2);
	}
	
	@Test
	public void getMagnitude() {
		ComplexNumber cn1 = new ComplexNumber(2.3, 3.7);
		assertTrue(Math.abs(cn1.getMagnitude() - 4.35660418215) < DELTA);
	}
	
	@Test
	public void getAsString() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		assertEquals(cn1.toString(), "-2.3 + 3.7i");
	}
	
	@Test
	public void toPower() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		ComplexNumber powered = cn1.power(7);
		assertEquals(powered.getReal(), -20338.6539736, DELTA);
		assertEquals(powered.getImaginary(), 21763.3920376, DELTA);
	}
	
	@Test
	public void nthRoot() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		double[] reals = {1.17745 , 0.445473 , -0.621953 , -1.22104 , -0.900654 , 0.0979392 , 1.02278 };
		double[] imaginarys = {+0.369203, +1.15076, +1.06577, +0.178236, -0.843517, -1.23008, -0.690373 };
		
		ComplexNumber[] roots = cn1.root(7);
		for (int i = 0; i < roots.length; i++) {
			assertEquals(roots[i].getReal(), reals[i], DELTA);
			assertEquals(roots[i].getImaginary(), imaginarys[i], DELTA);			
		}
	}
	
	@Test
	public void toPowerZero() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		ComplexNumber powered = cn1.power(0);
		assertEquals(powered.getReal(), 1, DELTA);
		assertEquals(powered.getImaginary(), 0, DELTA);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void toPowerNegative() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		cn1.power(-2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void toRootZero() {
		ComplexNumber cn1 = new ComplexNumber(-2.3, 3.7);
		cn1.root(0);
	}
	
	
	
	
	
}
