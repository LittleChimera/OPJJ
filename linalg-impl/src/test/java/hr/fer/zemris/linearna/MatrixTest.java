package hr.fer.zemris.linearna;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

	private static final double FI = 1E-9;

	private static final double[][] elements1 = {
			{ 7.606, 3.22, 3.961, 4.043 }, { 2.182, 6.79, 8.1, 0.496 },
			{ 0.785, 7.318, 6.689, 7.414 }, { 9.154, 6.122, 7.406, 9.112 } };

	@Test
	public void newInstanceTest() {
		double[][] expected = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		IMatrix m = new Matrix(5, 6).newInstance(3, 4);
		double[][] result = m.toArray();
		AbstractMatrixTest.assert2DArrays(expected, result);
	}

	@Test
	public void getTest() {
		assertEquals(0, new Matrix(5, 6).get(3, 2), FI);
	}

	@Test(expected = IllegalArgumentException.class)
	public void accessNonexistingPositiveRowTest() {
		new Matrix(5, 6).get(5, 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void accessNonexistingNegativeRowTest() {
		new Matrix(5, 6).get(-1, 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void accessNonexistingPositiveColumnTest() {
		new Matrix(5, 6).get(4, 6);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void accessNonexistingNegativeColumnTest() {
		new Matrix(5, 6).get(2, -1);
	}

	@Test
	public void parseTest() {
		IMatrix matrix = Matrix
				.parseSimple("7.606 3.22 3.961 4.043 | 2.182 6.79 8.1 0.496 | 0.785 7.318 6.689 7.414 | 9.154 6.122 7.406 9.112 ");
		double[][] result = matrix.toArray();
		AbstractMatrixTest.assert2DArrays(elements1, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseWithMissingCellsTest() {
		Matrix.parseSimple("7.606 3.22 3.961 4.043 | 2.182 6.79 8.1 0.496 | 7.318 6.689 7.414 | 9.154 6.122 7.406 9.112 ");
	}

}
