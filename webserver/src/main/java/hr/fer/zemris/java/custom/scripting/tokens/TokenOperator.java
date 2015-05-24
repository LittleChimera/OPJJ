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
	
	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Gets token's operator symbol.
	 * @return token's operator symbol
	 */
	public String getSymbol() {
		return symbol;
	}
}
