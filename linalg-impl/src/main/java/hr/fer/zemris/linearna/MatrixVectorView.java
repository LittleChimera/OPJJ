package hr.fer.zemris.linearna;

/**
 * MatrixVectorView creates a new one dimensional matrix live view from a
 * vector.
 * 
 * @author Luka Skugor
 *
 */
public class MatrixVectorView extends AbstractMatrix {

	/**
	 * Viewed vector.
	 */
	private IVector original;
	/**
	 * If true matrix will have one row and otherwise it'll have one column.
	 */
	private boolean asRowMatrix;

	/**
	 * Creates a new MatrixVectorView from a vector. If asRowMatrix is true
	 * creates matrix will have one row and otherwise it'll have on column.
	 * 
	 * @param original
	 *            viewed vector
	 * @param asRowMatrix
	 *            indicates if matrix will have one column or one row
	 */
	public MatrixVectorView(IVector original, boolean asRowMatrix) {
		this.original = original;
		this.asRowMatrix = asRowMatrix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getRowsCount()
	 */
	@Override
	public int getRowsCount() {
		if (asRowMatrix) {
			return 1;
		} else {
			return original.getDimension();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getColsCount()
	 */
	@Override
	public int getColsCount() {
		if (asRowMatrix) {
			return original.getDimension();
		} else {
			return 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#get(int, int)
	 */
	@Override
	public double get(int row, int col) {
		validateField(row, col);

		return (row > col) ? original.get(row) : original.get(col);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#set(int, int, double)
	 */
	@Override
	public IMatrix set(int row, int col, double value) {
		validateField(row, col);
		original.set((row > col) ? row : col, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#copy()
	 */
	@Override
	public IMatrix copy() {
		return new MatrixVectorView(original.copy(), asRowMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#newInstance(int, int)
	 */
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return LinAlgDefaults.defaultMatrix(rows, cols);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#toArray()
	 */
	@Override
	public double[][] toArray() {
		return new double[][] { original.toArray() };
	}

	/**
	 * Validates existence of accessed field of the matrix.
	 * 
	 * @param row
	 *            accessed row
	 * @param col
	 *            accessed column
	 * @throws IllegalArgumentException
	 *             if requested field doesn't exist
	 */
	private void validateField(int row, int col) {
		int rowsCount = getRowsCount();
		int colsCount = getColsCount();
		if ((row < 0 || row >= rowsCount) || (col < 0 || col >= colsCount)) {
			throw new IllegalArgumentException("Unexisting field.");
		}
	}

}
