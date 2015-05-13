package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token that represents a constant double value.
 * 
 * @author Erik Banek
 */
public class TokenConstantDouble extends Token {
	/** Token with double value. */
	private double value;
	
	/** Constructs a read-only token that holds a double. */
	public TokenConstantDouble(double doubleForToken) {
		this.value = doubleForToken;
	}
	
	/** Value getter. */
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String asText() {
		return Double.toString(this.value);
	}
}
