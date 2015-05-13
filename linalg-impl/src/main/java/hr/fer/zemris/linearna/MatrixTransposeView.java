package hr.fer.zemris.linearna;

/**
 * MatrixTransposeView is a live view of a transposed matrix. Viewed matrix isn't changed.
 * 
 * @author Luka Skugor
 *
 */
public class MatrixTransposeView extends AbstractMatrix {

	/**
	 * Viewed matrix.
	 */
	private IMatrix viewMatrix;

	/**
	 * Creates a new transposed view of a matrix.
	 * @param liveMatrix viewed matrix
	 */
	public MatrixTransposeView(IMatrix liveMatrix) {
		viewMatrix = liveMatrix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getRowsCount()
	 */
	@Override
	public int getRowsCount() {
		return viewMatrix.getColsCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getColsCount()
	 */
	@Override
	public int getColsCount() {
		return viewMatrix.getRowsCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#get(int, int)
	 */
	@Override
	public double get(int row, int col) {
		return viewMatrix.get(col, row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#set(int, int, double)
	 */
	@Override
	public IMatrix set(int row, int col, double value) {
		return viewMatrix.set(col, row, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#copy()
	 */
	@Override
	public IMatrix copy() {
		return new MatrixTransposeView(viewMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#newInstance(int, int)
	 */
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixTransposeView(viewMatrix.newInstance(rows, cols));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#toArray()
	 */
	@Override
	public double[][] toArray() {
		double[][] transpose = new double[viewMatrix.getColsCount()][viewMatrix
				.getRowsCount()];
		for (int i = 0; i < transpose.length; i++) {
			for (int j = 0; j < transpose[i].length; j++) {
				transpose[i][j] = get(i, j);
			}
		}

		return transpose;
	}

}
