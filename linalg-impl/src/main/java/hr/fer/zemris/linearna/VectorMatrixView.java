package hr.fer.zemris.linearna;

/**
 * VectorMatrixView is a vector live view on one dimensional matrix.
 * 
 * @author Luka Skugor
 *
 */
public class VectorMatrixView extends AbstractVector {

	/**
	 * Viewed matrix.
	 */
	private IMatrix original;
	/**
	 * Vector dimension.
	 */
	private int dimension;
	/**
	 * If true given matrix is a one row matrix, else it's a one column matrix.
	 */
	private boolean rowMatrix;

	/**
	 * Creates a new VectorMatrixView. If provided matrix isn't one dimensional exception will be thrown.
	 * 
	 * @param original viewed matrix
	 * @throws IllegalArgumentException if viewed matrix isn't one dimensional
	 */
	public VectorMatrixView(IMatrix original) {
		this.original = original;
		if (original.getRowsCount() == 1) {
			rowMatrix = true;
			dimension = original.getColsCount();
		} else if (original.getColsCount() == 1) {
			rowMatrix = false;
			dimension = original.getRowsCount();
		} else {
			throw new IllegalArgumentException(
					"Matrix isn't one dimensional.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#get(int)
	 */
	@Override
	public double get(int index) {
		if (rowMatrix) {
			return original.get(0, index);
		} else {
			return original.get(index, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#set(int, double)
	 */
	@Override
	public IVector set(int index, double value) {
		if (rowMatrix) {
			original.set(0, index, value);
		} else {
			original.set(index, 0, value);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#getDimension()
	 */
	@Override
	public int getDimension() {
		return dimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#copy()
	 */
	@Override
	public IVector copy() {
		return new VectorMatrixView(original.copy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#newInstance(int)
	 */
	@Override
	public IVector newInstance(int dimension) {
		return LinAlgDefaults.defaultVector(dimension);
	}
}
