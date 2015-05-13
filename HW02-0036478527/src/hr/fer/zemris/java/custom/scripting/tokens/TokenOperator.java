package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing an operator.
 * 
 * @author Luka Skugor
 *
 */
public class TokenOperator extends Token {

	/**
	 * Operator symbol
	 */
	private String symbol;

	/**
	 * Creates a token with given symbol.
	 * 
	 * @param symbol
	 *            operator's symbol
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}
}
