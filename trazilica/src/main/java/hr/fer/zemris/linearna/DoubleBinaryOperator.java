package hr.fer.zemris.linearna;

/**
 * Performs a double binary operation on two double values.
 * @author Luka Skugor
 *
 */
public interface DoubleBinaryOperator {
	/**
	 * Performs a calculation between given values.
	 * @param first left double value
	 * @param second right double value
	 * @return calculated double value
	 */
	double calc(double first, double second);
}
