package hr.fer.zemris.linearna;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractMatrixTest {

	private static final double FI = 1E-9;

	private static final double[][] elements1 = {
			{ 7.606, 3.22, 3.961, 4.043 }, { 2.182, 6.79, 8.1, 0.496 },
			{ 0.785, 7.318, 6.689, 7.414 }, { 9.154, 6.122, 7.406, 9.112 } };

	private static final double[][] elements2 = {
			{ 3.418, 5.489, 6.606, 5.453 }, { 7.549, 3.612, 3.688, 1.511 },
			{ 0.2, 4.007, 6.19, 8.677 }, { 7.299, 3.421, 1.982, 0.082 } };
	private static final double[][] elements3 = {
			{ 3.418, 5.489, 6.606, 5.453 }, { 7.549, 3.612, 3.688, 1.511 },
			{ 0.2, 4.007, 6.19, 8.677 } };

	@Test
	public void constructorTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 4, elements1, true);
		double[][] expected = m2.toArray();
		double[][] result = m1.toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void transposeTest() {
		IMatrix m = new Matrix(4, 4, elements1, false);
		double[][] expected = {
				{ 7.60600000000000, 2.18200000000000, 0.785000000000000,
						9.15400000000000, },
				{ 3.22000000000000, 6.79000000000000, 7.31800000000000,
						6.12200000000000, },
				{ 3.96100000000000, 8.10000000000000, 6.68900000000000,
						7.40600000000000, },
				{ 4.04300000000000, 0.496000000000000, 7.41400000000000,
						9.11200000000000, } };
		IMatrix m2 = m.nTranspose(false);
		m.set(0, 0, 1000);
		assertEquals(7.606, m2.get(0, 0), FI);
		double[][] result = m2.toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void transposeLiveTest() {
		IMatrix m = new Matrix(4, 4, elements1, false);
		double[][] expected = { { 7.606, 2.182, 0.785, 9.154, },
				{ 3.22, 6.79, 7.318, 1000, }, { 3.961, 8.1, 6.689, 7.406, },
				{ 4.043, 0.496, 7.414, 9.112, } };
		IMatrix m2 = m.nTranspose(true);
		m.set(3, 1, 1000);
		assertEquals(1000, m2.get(1, 3), FI);
		double[][] result = m2.toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void addTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 4, elements2, false);
		double[][] expected = { { 11.024, 8.709, 10.567, 9.496 },
				{ 9.731, 10.402, 11.788, 2.007 },
				{ 0.985, 11.325, 12.879, 16.091 },
				{ 16.453, 9.543, 9.388, 9.194 } };

		double[][] result = m1.add(m2).toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void nAddTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 4, elements2, false);
		double[][] expected = { { 11.024, 8.709, 10.567, 9.496 },
				{ 9.731, 10.402, 11.788, 2.007 },
				{ 0.985, 11.325, 12.879, 16.091 },
				{ 16.453, 9.543, 9.388, 9.194 } };

		IMatrix m3 = m1.nAdd(m2);
		m1.set(1, 1, 1000);
		double[][] result = m3.toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void subTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 4, elements2, false);
		double[][] expected = { { 4.188, -2.269, -2.645, -1.41 },
				{ -5.367, 3.178, 4.412, -1.015 },
				{ 0.585, 3.311, 0.499, -1.263 }, { 1.855, 2.701, 5.424, 9.03 } };

		double[][] result = m1.sub(m2).toArray();
		assert2DArrays(expected, result);
	}

	@Test
	public void nSubTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 4, elements2, false);
		double[][] expected = { { 4.188, -2.269, -2.645, -1.41 },
				{ -5.367, 3.178, 4.412, -1.015 },
				{ 0.585, 3.311, 0.499, -1.263 }, { 1.855, 2.701, 5.424, 9.03 } };

		IMatrix m3 = m1.nSub(m2);
		m1.set(1, 1, 1000);
		double[][] result = m3.toArray();
		assert2DArrays(expected, result);
	}

	@Test(expected = IncompatibleOperandException.class)
	public void addTestUnequalRows() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(3, 4, elements1, false);

		m1.add(m2);
	}

	@Test(expected = IncompatibleOperandException.class)
	public void addTestUnequalColumns() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(4, 3, elements1, false);

		m1.add(m2);
	}

	@Test(expected = IncompatibleOperandException.class)
	public void subTestIllegalOperand() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = new Matrix(3, 4, elements3, false);

		m1.sub(m2);
	}

	@Test
	public void subMatrixTestLive() {
		IMatrix m1 = new Matrix(4, 4, elements1, false).subMatrix(2, 2, true);

		double[][] expected = { { 7.606, 3.22, 4.043 }, { 2.182, 6.79, 0.496 },
				{ 9.154, 6.122, 9.112 } };

		double[][] result = m1.toArray();
		assert2DArrays(expected, result);

	}

	@Test
	public void subMatrixTestNonLive() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		IMatrix m2 = m1.subMatrix(2, 2, false);
		m1.set(1, 1, 1000);

		double[][] expected = { { 7.606, 3.22, 4.043 }, { 2.182, 6.79, 0.496 },
				{ 9.154, 6.122, 9.112 } };

		double[][] result = m2.toArray();
		assert2DArrays(expected, result);

	}

	@Test
	public void nMultiplyTest() {
		IMatrix m1 = new Matrix(3, 4, elements3, false);
		IMatrix m2 = new Matrix(3, 4, elements3, false).nTranspose(false);
		double[][] expected = { { 115.18629, 78.231161, 110.884844 },
				{ 78.231161, 85.91841, 51.922751 },
				{ 110.884844, 51.922751, 129.702478 } };
		assert2DArrays(expected, m1.nMultiply(m2).toArray());
	}

	@Test(expected = IncompatibleOperandException.class)
	public void nMultiplyWithIncopatibleMatrixTest() {
		IMatrix m1 = new Matrix(3, 4, elements3, false);
		m1.nMultiply(m1);
	}

	@Test
	public void determminantTest() {
		IMatrix m = new Matrix(4, 4, elements1, false);
		assertEquals(-394.312271986768, m.determinant(), FI);
	}

	@Test(expected = IncompatibleOperandException.class)
	public void determminantNonSquareMatrixTest() {
		new Matrix(3, 4, elements1, true).determinant();
	}

	@Test
	public void scalarMultiplyTest() {
		IMatrix m1 = new Matrix(3, 4, elements3, false);
		double[][] expected = { { 11.7921, 18.93705, 22.7907, 18.81285 },
				{ 26.04405, 12.4614, 12.7236, 5.21295 },
				{ 0.69, 13.82415, 21.3555, 29.93565 } };
		assert2DArrays(expected, m1.scalarMultiply(3.45).toArray());
	}

	@Test
	public void nScalarMultiplyTest() {
		IMatrix m1 = new Matrix(3, 4, elements3, false);
		double[][] expected = { { 11.7921, 18.93705, 22.7907, 18.81285 },
				{ 26.04405, 12.4614, 12.7236, 5.21295 },
				{ 0.69, 13.82415, 21.3555, 29.93565 } };

		IMatrix m2 = m1.nScalarMultiply(3.45);
		m1.set(1, 1, 1000);
		assert2DArrays(expected, m2.toArray());
	}

	@Test
	public void makeIdentityTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false).makeIdentity();
		double[][] expected = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 },
				{ 0, 0, 0, 1 } };

		assert2DArrays(expected, m1.toArray());
	}

	@Test(expected = IncompatibleOperandException.class)
	public void makeIdentityTestNonSquareMatrix() {
		new Matrix(3, 4, elements3, false).makeIdentity();
	}

	@Test
	public void matrixRowtoVectorTestNonLive() {
		IMatrix m1 = new Matrix(1, 4, elements3, false);
		IVector v1 = m1.toVector(false);
		m1.set(0, 3, 1000);
		double[] expected = elements3[0];

		assertArrayEquals(expected, v1.toArray(), FI);
	}

	@Test(expected = IncompatibleOperandException.class)
	public void nonOneDimensionalMatrixToVector() {
		new Matrix(4, 2, elements1, false).toVector(true);
	}

	@Test
	public void matrixRowtoVectorTestLive() {
		IMatrix m1 = new Matrix(1, 4, elements3, false);
		IVector v1 = m1.toVector(true);
		m1.set(0, 3, 1000);
		double[] expected = m1.toArray()[0];

		assertArrayEquals(expected, v1.toArray(), FI);
	}

	@Test
	public void matrixColumntoVectorTestLive() {
		IMatrix m1 = new Matrix(3, 1, elements3, false);
		IVector v1 = m1.toVector(true);
		double[] expected = { 3.418, 7.549, 1000 };
		m1.set(2, 0, 1000);

		assertArrayEquals(expected, v1.toArray(), FI);
	}

	@Test
	public void nInvertTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		assertEquals(0, m1.nMultiply(m1.nInvert())
				.sub(m1.copy().makeIdentity()).determinant(), FI);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void nInvertNonSquareMatrixTest() {
		new Matrix(3, 4, elements1, false).nInvert();
	}

	@Test
	public void toStringTest() {
		IMatrix m1 = new Matrix(4, 4, elements1, false);
		assertEquals(
				"7.606 3.220 3.961 4.043\n2.182 6.790 8.100 0.496\n0.785 7.318 6.689 7.414\n9.154 6.122 7.406 9.112\n",
				m1.toString());
	}

	protected static void assert2DArrays(double[][] expected, double[][] result) {
		assertEquals(expected.length, result.length);
		for (int i = 0; i < result.length; i++) {
			assertArrayEquals(expected[i], result[i], FI);
		}
	}

}
