package hr.fer.zemris.linearna;

import java.util.Arrays;

/**
 * Simple and concrete implementation of {@link IMatrix} using primitive array for storing elements.
 * 
 * @author Luka Skugor
 *
 */
public class Matrix extends AbstractMatrix {

	/**
	 * Stored matrix values.
	 */
	protected double[][] elements;
	/**
	 * Number of rows in the matrix.
	 */
	protected int rows;
	/**
	 * Number of columns in the matrix.
	 */
	protected int cols;

	/**
	 * Creates a new matrix with given number of rows and columns.
	 * 
	 * @param rows
	 *            number of rows of created matrix
	 * @param cols
	 *            number of columns of created matrix
	 */
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.elements = new double[rows][cols];
	}

	/**
	 * Creates a new matrix with given number of rows and columns and filling
	 * them with values from two-dimensional array.
	 * 
	 * @param rows
	 *            number of rows of created matrix
	 * @param cols
	 *            number of columns of created matrix
	 * @param elements
	 *            value with which matrix will be filled
	 * @param useGiven
	 *            if false constructor will copy given array, otherwise it will
	 *            use given one
	 */
	public Matrix(int rows, int cols, double[][] elements, boolean useGiven) {
		this(rows, cols);
		if (useGiven) {
			this.elements = elements;

		} else {
			this.elements = new double[rows][cols];
			for (int i = 0; i < rows; i++) {
				System.arraycopy(elements[i], 0, this.elements[i], 0, cols);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getRowsCount()
	 */
	@Override
	public int getRowsCount() {
		return rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#getColsCount()
	 */
	@Override
	public int getColsCount() {
		return cols;
	}

	/**
	 * @throws IllegalArgumentException if row or column is invalid
	 */
	@Override
	public double get(int row, int col) {
		validateCell(row, col);
		return elements[row][col];
	}

	/**
	 * @throws IllegalArgumentException if row or column is invalid
	 */
	@Override
	public IMatrix set(int row, int col, double value) {
		validateCell(row, col);
		elements[row][col] = value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#copy()
	 */
	@Override
	public IMatrix copy() {
		return new Matrix(rows, cols, elements, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#newInstance(int, int)
	 */
	@Override
	public IMatrix newInstance(int rows, int cols) {
		double[][] matrixArray = new double[rows][cols];
		for (double[] ds : matrixArray) {
			Arrays.fill(ds, 0);
		}

		return new Matrix(rows, cols, matrixArray, true);
	}

	/**
	 * Parses string as matrix. Each row must be separated with '|' and values are separated with spaces.
	 * @param s string containing a matrix
	 * @return parsed matrix
	 * @throws IllegalArgumentException if some cells are missing
	 */
	public static Matrix parseSimple(String s) {

		String[] rowsStrings = s.split("\\|");

		int rowCount = rowsStrings.length;
		double[][] matrixArray = new double[rowCount][];

		for (int i = 0; i < rowsStrings.length; i++) {

			IVector v = Vector.parseSimple(rowsStrings[i]);
			matrixArray[i] = v.toArray();

			if (matrixArray[i].length != matrixArray[0].length) {
				throw new IllegalArgumentException(
						"Matrix is missing some cells.");
			}
		}
		int colCount = matrixArray[0].length;
		return new Matrix(rowCount, colCount, matrixArray, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IMatrix#toArray()
	 */
	@Override
	public double[][] toArray() {
		double[][] copy = new double[rows][cols];
		for (int i = 0; i < elements.length; i++) {
			System.arraycopy(elements[i], 0, copy[i], 0, cols);
		}

		return copy;
	}

	/**
	 * Checks if matrix contains given row and column.
	 * @param row row index which will be checked
	 * @param col column index which will be checked
	 * @throws IllegalArgumentsException if row or column is invalid
	 */
	private void validateCell(int row, int col) {
		if ((row < 0 || row >= rows) || (col < 0 || col >= cols)) {
			throw new IllegalArgumentException(String.format(
					"Invalid row/collumn. Matrix has dimensions %dx%d", rows,
					cols));
		}
	}

}
