package hr.fer.zemris.linearna;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class AbstractVectorTest {
	
	private static final double FI = 1E-9;

	private static final double[] elements1 = { -1.086, 2.493, 5.559, 2.904 };
	
	private static final double[] elements2 = { 3.273, -4.560, 3.399, 2.815 };
	
	private static final double[] elements3 = { 3.273, -4.560, 3.399 };
	
	private static final double[] elements4 = { 3, 4 };
	
	@Test
	public void addTest() {
		AbstractVector v1 = new Vector(elements1);
		AbstractVector v2 = new Vector(elements2);
		double[] result = {2.187, -2.067, 8.958, 5.719};
		assertArrayEquals(result, v1.add(v2).toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void addTestIncomptibleOperand() {
		new Vector(elements1).add(new Vector(elements3));
	}
	
	@Test
	public void nAddTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] old = v1.toArray();
		AbstractVector v2 = new Vector(elements2);
		IVector v3 = v1.nAdd(v2);
		double[] result = {2.187, -2.067, 8.958, 5.719};
		assertArrayEquals(old, v1.toArray(), FI);
		assertArrayEquals(result, v3.toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nAddTestIncomptibleOperand() {
		new Vector(elements1).nAdd(new Vector(elements3));
	}
	
	@Test
	public void subTest() {
		AbstractVector v1 = new Vector(elements1);
		AbstractVector v2 = new Vector(elements2);
		double[] result = {-4.359, 7.053, 2.16, 0.089};
		assertArrayEquals(result, v1.sub(v2).toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void subTestIncomptibleOperand() {
		new Vector(elements1).sub(new Vector(elements3));
	}
	
	@Test
	public void nSubTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] old = v1.toArray();
		AbstractVector v2 = new Vector(elements2);
		IVector v3 = v1.nSub(v2);
		double[] result = {-4.359, 7.053, 2.16, 0.089};
		assertArrayEquals(old, v1.toArray(), FI);
		assertArrayEquals(result, v3.toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nSubTestIncomptibleOperand() {
		new Vector(elements1).nSub(new Vector(elements3));
	}
	
	@Test
	public void scalarMultiplyTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] result = {-3.7467, 8.60085, 19.17855, 10.0188};
		assertArrayEquals(result, v1.scalarMultiply(3.45).toArray(), FI);
	}
	
	@Test
	public void nScalarMultiplyTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] old = v1.toArray();
		IVector v3 = v1.nScalarMultiply(3.45);
		double[] result = {-3.7467, 8.60085, 19.17855, 10.0188};
		assertArrayEquals(old, v1.toArray(), FI);
		assertArrayEquals(result, v3.toArray(), FI);
	}
	
	@Test
	public void normTest() {
		AbstractVector v1 = new Vector(elements1);
		assertEquals(6.83594485056748, v1.norm(), FI);
	}
	
	@Test
	public void normTestNaturalNumbers() {
		AbstractVector v1 = new Vector(elements4);
		assertEquals(5, v1.norm(), FI);
	}
	
	@Test
	public void normalizeTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] result = {-0.158866114888251, 0.364689893569438, 0.813201411292622, 0.424813257491235};
		assertArrayEquals(result, v1.normalize().toArray(), FI);
	}
	
	@Test
	public void nNormalizeTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] old = v1.toArray();
		double[] result = {-0.158866114888251, 0.364689893569438, 0.813201411292622, 0.424813257491235};
		assertArrayEquals(result, v1.nNormalize().toArray(), FI);
		assertArrayEquals(old, v1.toArray(), FI);
	}
	
	@Test
	public void cosineTest() {
		AbstractVector v1 = new Vector(elements1);
		AbstractVector v2 = new Vector(elements2);
		assertEquals(0.248865165764822, v1.cosine(v2), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void cosineTestIncomptibleOperand() {
		new Vector(elements1).cosine(new Vector(elements3));
	}
	
	@Test
	public void scalarProductTest() {
		AbstractVector v1 = new Vector(elements1);
		AbstractVector v2 = new Vector(elements2);
		assertEquals(12.1472430000000, v1.scalarProduct(v2), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void scalarProductTestIncomptibleOperand() {
		new Vector(elements1).scalarProduct(new Vector(elements3));
	}
	
	@Test
	public void copyPartTestCopiedLongerThanOrginal() {
		int n = 15;
		double[] result = new double[n];
		Arrays.fill(result, 0);
		AbstractVector v1 = new Vector(elements1);
		double[] v1Array = v1.toArray();
		System.arraycopy(v1Array, 0, result, 0, v1Array.length);
		assertArrayEquals(result, v1.copyPart(n).toArray(), FI);
	}
	
	@Test
	public void copyPartTestCopiedShorterThanOrginal() {
		int n = 2;
		double[] result = new double[n];
		Arrays.fill(result, 0);
		AbstractVector v1 = new Vector(elements1);
		double[] v1Array = v1.toArray();
		System.arraycopy(v1Array, 0, result, 0, n);
		assertArrayEquals(result, v1.copyPart(n).toArray(), FI);
	}
	
	@Test
	public void nFromHommogenusTest() {
		AbstractVector v1 = new Vector(elements1);
		double[] result = { -0.373966942148760, 0.858471074380165, 1.91425619834711 };
		assertArrayEquals(result, v1.nFromHomogeneus().toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nFromHommogenusTestOneDimensionalVector() {
		new Vector(elements1).copyPart(1).nFromHomogeneus();
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nFromHommogenusTestLastElementZero() {
		new Vector(elements1).set(3, 0).nFromHomogeneus();
	}
	
	@Test
	public void vectorToRowMatrixTestLiveView() {
		IVector v1 = new Vector(elements1);
		IMatrix m1 = v1.toRowMatrix(true);
		assertEquals(m1.getRowsCount(), 1);
		v1.set(3, 1000);
		double[] expected = Arrays.copyOf(elements1, elements1.length);
		expected[3] = 1000;
		assertArrayEquals(m1.toArray()[0], expected, FI);
	}
	
	@Test
	public void vectorToRowMatrixTestNonLiveView() {
		IVector v1 = new Vector(elements1);
		IMatrix m1 = v1.toRowMatrix(false);
		assertEquals(m1.getRowsCount(), 1);
		v1.set(3, 1000);
		assertArrayEquals(m1.toArray()[0], elements1, FI);
	}
	
	@Test
	public void vectorToColumnMatrixTestLiveView() {
		IVector v1 = new Vector(elements1);
		IMatrix m1 = v1.toColumnMatrix(true).nTranspose(true);
		assertEquals(m1.getRowsCount(), 1);
		v1.set(3, 1000);
		double[] expected = Arrays.copyOf(elements1, elements1.length);
		expected[3] = 1000;
		assertArrayEquals(m1.toArray()[0], expected, FI);
	}
	
	@Test
	public void vectorToColumnMatrixTestNonLiveView() {
		IVector v1 = new Vector(elements1);
		IMatrix m1 = v1.toColumnMatrix(false).nTranspose(true);
		assertEquals(m1.getRowsCount(), 1);
		v1.set(3, 1000);
		assertArrayEquals(m1.toArray()[0], elements1, FI);
	}
	
	@Test
	public void nVectorProductTest() {
		IVector v1 = new Vector(elements1).copyPart(3);
		IVector v2 = new Vector(elements2).copyPart(3);
		IVector v3 = v1.nVectorProduct(v2);
		double[] expexted = {33.8227470000000, 21.8859210000000, -3.20742900000000};
		assertArrayEquals(expexted, v3.toArray(), FI);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nVectorProductTestOnNon3DimensionalVectors() {
		IVector v1 = new Vector(elements1).copyPart(2);
		IVector v2 = new Vector(elements2).copyPart(3);
		v1.nVectorProduct(v2);
	}
	
	@Test(expected=IncompatibleOperandException.class)
	public void nVectorProductTestWtihNon3DimensionalVectors() {
		IVector v1 = new Vector(elements1).copyPart(3);
		IVector v2 = new Vector(elements2).copyPart(2);
		v1.nVectorProduct(v2);
	}
	
	@Test
	public void toStringTest() {
		assertEquals("-1.086 2.493 5.559 2.904", new Vector(elements1).toString());
	}

}
