package hr.fer.zemris.linearna;

/**
 * Abstract implementation of {@link IMatrix}.
 * 
 * @author Luka Skugor
 *
 */
public abstract class AbstractMatrix implements IMatrix {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nTranspose(boolean)
	 */
	@Override
	public IMatrix nTranspose(boolean liveView) {
		IMatrix transpose = new MatrixTransposeView(this);

		return (liveView) ? transpose : new Matrix(transpose.getRowsCount(),
				transpose.getColsCount(), transpose.toArray(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#add(hr.fer.zemris.linearna.IMatrix)
	 */
	@Override
	public IMatrix add(IMatrix other) {
		return calc(other, (d1, d2) -> d1 + d2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nAdd(hr.fer.zemris.linearna.IMatrix)
	 */
	@Override
	public IMatrix nAdd(IMatrix other) {
		return copy().add(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#sub(hr.fer.zemris.linearna.IMatrix)
	 */
	@Override
	public IMatrix sub(IMatrix other) {
		return calc(other, (d1, d2) -> d1 - d2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nSub(hr.fer.zemris.linearna.IMatrix)
	 */
	@Override
	public IMatrix nSub(IMatrix other) {
		return copy().sub(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.linearna.IMatrix#nMultiply(hr.fer.zemris.linearna.IMatrix)
	 */
	@Override
	public IMatrix nMultiply(IMatrix other) {
		if (this.getColsCount() != other.getRowsCount()) {
			throw new IncompatibleOperandException(
					"First matrix has to have column count equal to the row count of other matrix.");
		}
		int nMulRowCount = this.getRowsCount();
		int nMulColCount = other.getColsCount();
		IMatrix nMul = newInstance(nMulRowCount, nMulColCount);
		// number of multiplication on calculation of each cell
		int m = getColsCount();

		for (int i = 0; i < nMulRowCount; i++) {
			for (int j = 0; j < nMulColCount; j++) {
				double cellValue = 0;
				for (int k = 0; k < m; k++) {
					cellValue += this.get(i, k) * other.get(k, j);
				}
				nMul.set(i, j, cellValue);
			}
		}
		return nMul;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#determinant()
	 */
	@Override
	public double determinant() throws IncompatibleOperandException {
		if (getRowsCount() != getColsCount()) {
			throw new IncompatibleOperandException();
		}
		if (getRowsCount() == 1) {
			return get(0, 0);
		}

		double result = 0;
		for (int i = 0, cols = getColsCount(); i < cols; i++) {
			// column multiply value
			double colMulValue = get(0, i) * Math.signum((i + 1) % 2 - 0.5);
			result += colMulValue * subMatrix(0, i, true).determinant();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#subMatrix(int, int, boolean)
	 */
	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		IMatrix subMatrix = new MatrixSubMatrixView(this, row, col);
		return liveView ? subMatrix : subMatrix.copy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nInvert()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nInvert()
	 */
	@Override
	public IMatrix nInvert() {
		int rowsCount = getRowsCount();
		int colsCount = getColsCount();
		if (rowsCount != colsCount) {
			throw new UnsupportedOperationException("Inverse of non-square matrix doesn't exist.");
		}

		IMatrix cofactor = newInstance(getRowsCount(), getColsCount());
		for (int row = 0; row < rowsCount; row++) {
			for (int col = 0; col < colsCount; col++) {
				cofactor.set(row, col, this.subMatrix(row, col, true).determinant()*Math.pow(-1, row + col));
			}
		}
		
		return cofactor.nTranspose(false).scalarMultiply(1./this.determinant());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#toVector(boolean)
	 */
	@Override
	public IVector toVector(boolean liveView) {
		if (getColsCount() != 1 && getRowsCount() != 1) {
			throw new IncompatibleOperandException();
		}

		if (liveView) {
			return new VectorMatrixView(this);
		} else {
			return new VectorMatrixView(this.copy());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#nScalarMultiply(double)
	 */
	@Override
	public IMatrix nScalarMultiply(double value) {
		return copy().scalarMultiply(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#scalarMultiply(double)
	 */
	@Override
	public IMatrix scalarMultiply(double value) {
		return calc(this, (d1, d2) -> d1 * value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#makeIdentity()
	 */
	@Override
	public IMatrix makeIdentity() {
		if (getColsCount() != getRowsCount()) {
			throw new IncompatibleOperandException(
					"Cannot make identity from non-square matrix.");
		}

		// set all to zero and then fill diagonally with 1's
		sub(this);
		for (int i = 0, diagonal = getColsCount(); i < diagonal; i++) {
			set(i, i, 1);
		}
		return this;
	}

	/**
	 * Performs {@link DoubleBinaryOperator.calc} on between each responding
	 * cell from this matrix and other matrix.
	 * 
	 * @param other
	 *            matrix with which operation will be performed
	 * @param op
	 *            operator
	 * @return resulting matrix
	 * @throws IncompatibleOperandException
	 *             if matrixes don't have the same dimensions
	 */
	private IMatrix calc(IMatrix other, DoubleBinaryOperator op)
			throws IncompatibleOperandException {
		if (other.getColsCount() != getColsCount()
				|| other.getRowsCount() != getRowsCount()) {
			throw new IncompatibleOperandException();
		}

		for (int i = 0, rows = getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = getColsCount(); j < cols; j++) {
				set(i, j, op.calc(this.get(i, j), other.get(i, j)));
			}
		}

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(3);
	}

	/**
	 * Formats matrix as a string with given precision of values.
	 * 
	 * @param precision
	 *            number of decimal places in each value
	 * @return formatted string
	 */
	public String toString(int precision) {
		StringBuilder stringBuilder = new StringBuilder();
		double[][] values = toArray();
		for (double[] ds : values) {
			StringBuilder lineBuilder = new StringBuilder();
			for (double d : ds) {
				lineBuilder.append(String.format("%." + precision + "f ", d));
			}
			stringBuilder.append(lineBuilder.toString().trim()).append(
					String.format("%n"));
		}
		return stringBuilder.toString();
	}

}
