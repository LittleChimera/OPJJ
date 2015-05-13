package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Value used for storing operators.
 * @author Mihovil Vinković
 *
 */
public class TokenOperator extends Token {
	
	private String symbol;
	
	/**
	 * Basic constructor.
	 * @param symbol Value of the token.
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
}
