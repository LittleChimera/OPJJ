package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

/**
 * Interface on which every comparison operator is built. Comparison operators
 * check the values against a literal using their operator.
 * 
 * @author Luka Škugor
 *
 */
public interface IComparisonOperator {
	/**
	 * Matches the value against a literal for this operator.
	 * 
	 * @param value1
	 *            value against which a literal will be checked
	 * @param value2
	 *            literal
	 * @return true if value matches given literal for this operation, else
	 *         false
	 */
	public boolean satisfied(String value1, String value2);
}
