package hr.fer.zemris.linearna;

/**
 * Defines creators for default implementations of {@link IVector} and
 * {@link IMatrix}.
 * 
 * @author Luka Skugor
 *
 */
public class LinAlgDefaults {

	/**
	 * Creates a default matrix with given number of rows and columns.
	 * @param rows number of rows
	 * @param cols number of columns
	 * @return created matrix
	 */
	public static IMatrix defaultMatrix(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	/**
	 * Creates a default vector with given dimension.
	 * @param dimension vector's dimension
	 * @return created vector
	 */
	public static IVector defaultVector(int dimension) {
		return new Vector(new double[] {}).newInstance(dimension);
	}

}
