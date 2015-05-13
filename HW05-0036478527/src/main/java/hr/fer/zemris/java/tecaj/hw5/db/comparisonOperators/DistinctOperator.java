package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

/**
 * Implementation of IComparisonOperator for a "distinct" operator.
 * @author Luka Škugor
 *
 */
public class DistinctOperator implements IComparisonOperator {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.IComparisonOperator#satisfied(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return !value1.equals(value2);
	}

}
