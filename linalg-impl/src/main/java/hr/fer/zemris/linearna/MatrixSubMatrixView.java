package hr.fer.zemris.linearna;

/**
 * MatrixSubMatrixView is a live view of a matrix where sum rows and columns are
 * excluded.
 * 
 * @author Luka Skugor
 *
 */
public class MatrixSubMatrixView extends AbstractMatrix {

	/**
	 * Reference to the viewed matrix.
	 */
	private IMatrix original;
	/**
	 * Index of rows included in the view.
	 */
	private int[] rowIndexes;
	/**
	 * Index of columns included in the view.
	 */
	private int[] colIndexes;

	/**
	 * Creates a new view of a matrix excluding given row and column.
	 * 
	 * @param original viewed matrix
	 * @param row index of excluded row
	 * @param col index of excluded column
	 */
	public MatrixSubMatrixView(IMatrix original, int row, int col) {
		validateExluded(original, row, col);
		this.original = original;
		rowIndexes = new int[original.getRowsCount() - 1];
		colIndexes = new int[original.getColsCount() - 1];
		for (int i = 0, rowI = 0, colI = 0; i < rowIndexes.length; i++, colI++, rowI++) {
			// skip excluded row/column
			if (rowI == row) {
				rowI++;
			}
			if (colI == col) {
				colI++;
			}
			rowIndexes[i] = rowI;
			colIndexes[i] = colI;
		}

	}

	/**
	 * Creates a new view of a matrix including given rows and columns.
	 * @param original viewed matrix
	 * @param rowIndexes included row indexes
	 * @param colIndexes included column indexes
	 */
	private MatrixSubMatrixView(IMatrix original, int[] rowIndexes,
			int[] colIndexes) {
		if (rowIndexes.length == 0 || colIndexes.length == 0) {
			throw new IllegalArgumentException(
					"Can't create matrix having one dimension 0.");
		}
		this.original = original;
		this.rowIndexes = rowIndexes;
		this.colIndexes = colIndexes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.AbstractMatrix#subMatrix(int, int, boolean)
	 */
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		validateExluded(this, row, col);
		int[] rowIndexes = copyArrayExludingIndex(this.rowIndexes, row);
		int[] colIndexes = copyArrayExludingIndex(this.colIndexes, col);
		return new MatrixSubMatrixView(original, rowIndexes, colIndexes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getRowsCount()
	 */
	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getColsCount()
	 */
	@Override
	public int getColsCount() {
		return colIndexes.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#get(int, int)
	 */
	@Override
	public double get(int row, int col) {
		return original.get(rowIndexes[row], colIndexes[col]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#set(int, int, double)
	 */
	@Override
	public IMatrix set(int row, int col, double value) {
		original.set(rowIndexes[row], colIndexes[col], value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#copy()
	 */
	@Override
	public IMatrix copy() {
		return new MatrixSubMatrixView(original.copy(), rowIndexes, colIndexes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#newInstance(int, int)
	 */
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return original.newInstance(rows, cols);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#toArray()
	 */
	@Override
	public double[][] toArray() {
		double[][] result = new double[getRowsCount()][getColsCount()];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = get(i, j);
			}
		}
		return result;
	}

	/**
	 * Validates excluded row and column.
	 * @param original viewed matrix
	 * @param row excluded row index
	 * @param col excluded column index
	 * @throws IllegalArgumentException if given row or column can't be excluded
	 * @throws IncompatibleOperanException if matrix is one dimensional
	 */
	private void validateExluded(IMatrix original, int row, int col) {
		if (row < 0 || row >= original.getRowsCount()
				|| col >= original.getColsCount() || col < 0) {
			throw new IllegalArgumentException(
					"Can't exclude row/column which is not within matrix dimensions.");
		}
		if (Math.min(original.getRowsCount(), original.getColsCount()) < 2) {
			throw new IncompatibleOperandException(
					"Can't reduce matrix to dimensions less than 1.");
		}
	}

	/**
	 * Copies an array of indexes and excluded one at requested index.
	 * @param src source array of indexes
	 * @param index excluded index
	 * @return source array with remove requested index
	 */
	private int[] copyArrayExludingIndex(int[] src, int index) {
		int[] dest = new int[src.length - 1];
		System.arraycopy(src, 0, dest, 0, index);
		System.arraycopy(src, index + 1, dest, index, dest.length - index);
		return dest;
	}
}
