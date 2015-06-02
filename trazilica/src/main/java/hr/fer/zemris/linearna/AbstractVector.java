package hr.fer.zemris.linearna;

/**
 * Abstract implementation of {@link IVector}.
 * 
 * @author Luka Skugor
 *
 */
/**
 * @author luka
 *
 */
public abstract class AbstractVector implements IVector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#copyPart(int)
	 */
	@Override
	public IVector copyPart(int n) {
		IVector copied = newInstance(n);
		for (int index = 0, length = Math.min(getDimension(), n); index < length; index++) {
			copied.set(index, this.get(index));
		}
		return copied;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#add(hr.fer.zemris.linearna.IVector)
	 */
	@Override
	public IVector add(IVector other) throws IncompatibleOperandException {
		return calc(other, (d1, d2) -> d1 + d2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#nAdd(hr.fer.zemris.linearna.IVector)
	 */
	@Override
	public IVector nAdd(IVector other) throws IncompatibleOperandException {
		return this.copy().add(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#sub(hr.fer.zemris.linearna.IVector)
	 */
	@Override
	public IVector sub(IVector other) throws IncompatibleOperandException {
		return calc(other, (d1, d2) -> d1 - d2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#nSub(hr.fer.zemris.linearna.IVector)
	 */
	@Override
	public IVector nSub(IVector other) throws IncompatibleOperandException {
		return this.copy().sub(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#scalarMultiply(double)
	 */
	@Override
	public IVector scalarMultiply(double byValue) {
		return calc(this, (d1, d2) -> d1 * byValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#nScalarMultiply(double)
	 */
	@Override
	public IVector nScalarMultiply(double byValue) {
		return this.copy().scalarMultiply(byValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#norm()
	 */
	@Override
	public double norm() {
		double result = 0;
		for (double e : toArray()) {
			result += e * e;
		}
		return Math.sqrt(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#normalize()
	 */
	@Override
	public IVector normalize() {
		return scalarMultiply(1. / norm());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#nNormalize()
	 */
	@Override
	public IVector nNormalize() {
		return copy().normalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.linearna.IVector#cosine(hr.fer.zemris.linearna.IVector)
	 */
	@Override
	public double cosine(IVector other) throws IncompatibleOperandException {
		return scalarProduct(other) / (this.norm() * other.norm());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.linearna.IVector#scalarProduct(hr.fer.zemris.linearna.IVector
	 * )
	 */
	@Override
	public double scalarProduct(IVector other)
			throws IncompatibleOperandException {
		// first multiply each dimension of one vector with the one other vector
		// and then sum them all
		IVector dimsMultiplied = ((AbstractVector) copy()).calc(other,
				(d1, d2) -> d1 * d2);

		double result = 0;
		for (double e : dimsMultiplied.toArray()) {
			result += e;
		}
		return result;
	}

	/**
	 * Operation is NOT supported.
	 * @throws UnsupportedOperationException because operation is not supported
	 */
	@Override
	public IVector nVectorProduct(IVector other) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#nFromHomogeneus()
	 */
	@Override
	public IVector nFromHomogeneus() {
		if (getDimension() < 2) {
			throw new IncompatibleOperandException();
		}

		int dimension = getDimension();
		double last = get(dimension - 1);
		if (last == 0) {
			throw new IncompatibleOperandException("Last element is zero.");
		}

		return copyPart(dimension - 1).scalarMultiply(1. / last);
	}

	/**
	 * Operation is NOT supported.
	 * @throws UnsupportedOperationException because operation is not supported
	 */
	@Override
	public IMatrix toRowMatrix(boolean liveView) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Operation is NOT supported.
	 * @throws UnsupportedOperationException because operation is not supported
	 */
	@Override
	public IMatrix toColumnMatrix(boolean liveView) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.linearna.IVector#toArray()
	 */
	@Override
	public double[] toArray() {
		int length = getDimension();
		double[] array = new double[length];

		for (int i = 0; i < length; i++) {
			array[i] = get(i);
		}

		return array;
	}

	/**
	 * Performs {@link DoubleBinaryOperator.calc} on between each responding
	 * dimension from this vector and other vector.
	 * 
	 * @param other
	 *            vector with which operation will be performed
	 * @param op
	 *            operator
	 * @return resulting vector
	 * @throws IncompatibleOperandException
	 *             if vectors don't have the same dimension
	 */
	private IVector calc(IVector other, DoubleBinaryOperator op)
			throws IncompatibleOperandException {

		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException();
		}
		for (int i = 0, length = getDimension(); i < length; i++) {
			this.set(i, op.calc(this.get(i), other.get(i)));
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
	 * Formats vector as a string with given precision of values.
	 * 
	 * @param precision
	 *            number of decimal places in each value
	 * @return formatted string
	 */
	private String toString(int precision) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0, dimension = getDimension(); i < dimension; i++) {
			stringBuilder.append(String.format("%." + precision + "f", get(i)))
					.append(" ");
		}
		return stringBuilder.toString().trim();
	}

}
