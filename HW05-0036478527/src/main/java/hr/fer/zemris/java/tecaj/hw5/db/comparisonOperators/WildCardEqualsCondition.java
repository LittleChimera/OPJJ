package hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators;

/**
 * Implementation of IComparisonOperator for a "equals" operator. It supports a single wild card which is a substitute for any matching string.
 * 
 * For example: "abc*" will match anything starting with "abc".
 * @author Luka Škugor
 *
 */
public class WildCardEqualsCondition implements IComparisonOperator {

	/**
	 * Wild card character
	 */
	public static final char WILD_CARD = '*';

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.IComparisonOperator#satisfied(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		int count = 0;
		for (char character : value2.toCharArray()) {
			if (character == WILD_CARD) {
				count++;
				if (count > 1) {
					throw new IllegalArgumentException(
							"Comparsion string can't contain multiple wild cards!");
				}
			}
		}
		if (count == 1) {
			int wildCardPosition = value2.indexOf(WILD_CARD);
			String leftOfWildCard = value2.substring(0, wildCardPosition);
			String rightOfWildCard = value2.substring(wildCardPosition + 1, value2.length());
			/*value2 = value2.replaceAll("\\" + WILD_CARD, "." + WILD_CARD);
			value2 = "^" + value2 + "$";*/
			if (value1.startsWith(leftOfWildCard) && value1.endsWith(rightOfWildCard)) {
				return true;
			} else {
				return false;
			}
		} 
		
		if (value1.matches(value2)) {
			return true;
		}
		return false;
	}

}
