package hr.fer.zemris.linearna;

import java.util.Arrays;

/**
 * Simple and concrete implementation of {@link IVector} using primitive array
 * for storing elements.
 * 
 * @author Luka Skugor
 *
 */
public class Vector extends AbstractVector {

	/**
	 * Vector elements.
	 */
	private double[] elements;
	/**
	 * Vector's dimension
	 */
	private int dimension;
	/**
	 * If set to true vector can be only read.
	 */
	private boolean readOnly;

	/**
	 * Creates a new Vector from given elements.
	 * 
	 * @param elements values of the vector
	 */
	public Vector(double[] elements) {
		this(false, false, elements);
	}

	/**
	 * Creates a new Vector from given element and decides whether to use
	 * existing array to create and whether will the vector be read only.
	 * 
	 * @param readOnly
	 *            if set to true vector will be read only
	 * @param useGiven
	 *            if set to true existing array will be used, otherwise it'll be copied
	 * @param elements values of the vector
	 */
	public Vector(boolean readOnly, boolean useGiven, double[] elements) {
		this.readOnly = readOnly;
		if (useGiven) {
			this.elements = elements;
		} else {
			this.elements = Arrays.copyOf(elements, elements.length);
		}
		dimension = this.elements.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#get(int)
	 */
	@Override
	public double get(int index) {
		validateIndex(index);

		return elements[index];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#set(int, double)
	 */
	@Override
	public IVector set(int index, double value)
			throws UnmodifiableObjectException {
		if (readOnly) {
			throw new UnmodifiableObjectException();
		}
		validateIndex(index);

		elements[index] = value;
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
		return new Vector(false, false, elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#newInstance(int)
	 */
	@Override
	public IVector newInstance(int dimension) {
		double[] elements = new double[dimension];
		Arrays.fill(elements, 0);
		return new Vector(false, true, elements);
	}

	/**
	 * Parses a string for a vector. Vector's values should be separated with spaces.
	 * @param s parsing string
	 * @return parsed Vector
	 * @throws NumberFormatException
	 *             if string contains non-double values
	 */
	public static Vector parseSimple(String s) {
		String[] values = s.trim().split(" +");
		double[] elements = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			elements[i] = Double.parseDouble(values[i]);
		}

		return new Vector(elements);
	}

	/**
	 * Validates accessed index of the vector. If invalid exception will be thrown.
	 * @param index accessed index
	 * @throws IndexOutOfBoundsException if accessed index is invalid
	 */
	private void validateIndex(int index) {
		if (index < 0 || index >= dimension) {
			throw new IndexOutOfBoundsException(String.format(
					"Invalid index. Valid indexes for this vector are 0 to %d",
					dimension));
		}
	}

}
