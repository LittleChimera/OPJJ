package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing operator inside a tag of the document. Supproted operators
 * are +, -, *, /, %
 * 
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenOperator extends Token{
	String symbol;
	
	/**
	 * Constructs new TokenOperator with symbol.
	 * @param symbol operator symbol
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Getter for operator symbol.
	 * @return symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Returns string representation of TokenOperator.
	 */
	@Override
	public String asText() {
		return symbol;
	}
	
	/**
	 * Returns string representation of TokenOperator.
	 */
	@Override
	public String toString() {
		return symbol;
	}
}
