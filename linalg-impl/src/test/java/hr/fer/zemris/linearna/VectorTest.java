package hr.fer.zemris.linearna;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class VectorTest {
	
	private static double FI = 1E-9;

	private static final double[] elements1 = { -1.086, 2.493, 5.559, 2.904 };
	
	
	@Test
	public void getValueTest() {
		assertEquals(2.493, new Vector(elements1).get(1), FI);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getValueTestIllegalIndex() {
		assertEquals(2.493, new Vector(elements1).get(-1), FI);
	}
	
	@Test
	public void setTest() {
		Vector v1 = new Vector(elements1);
		v1.set(2, 3.555);
		assertEquals(3.555, v1.get(2), FI);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void setTestIllegalIndex() {
		new Vector(elements1).set(4, 3.555);
	}
	
	@Test(expected=UnmodifiableObjectException.class)
	public void setTestUnmodifiableVector() {
		new Vector(true, false, elements1).set(2, 3.555);
	}
	
	@Test
	public void getDimensionTest() {
		assertEquals(4, new Vector(elements1).getDimension(), FI);
	}
	
	@Test
	public void copyVectorTest() {
		Vector v1 = new Vector(elements1);
		double[] old = v1.toArray();
		IVector v2 = v1.copy();
		assertArrayEquals(old, v2.toArray(), FI);
		v2.scalarMultiply(5);
		assertArrayEquals(old, v1.toArray(), FI);
	}
	
	@Test
	public void newInstanceTest() {
		int n = 5;
		double[] result = new double[n];
		Arrays.fill(result, 0);
		assertArrayEquals(result, new Vector(elements1).newInstance(n).toArray(), FI);
	}
	
	@Test
	public void parseVectorTest1() {
		Vector v1 = Vector.parseSimple(" 3.1 4.123   -4   12  ");
		double[] result = {3.1, 4.123, -4, 12};
		assertArrayEquals(result, v1.toArray(), FI);
	}
	
	@Test(expected=NumberFormatException.class)
	public void parseVectorTestContainsLetters() {
		Vector.parseSimple(" 3.1 4.12d3   -4   12  ");
	}

}
