package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a decimal number constant inside a tags.
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenConstantDouble extends Token {
	double value;
	
	/**
	 * Constructs a new TokenConstantDouble with value of argument.
	 * @param value  value of double number constant
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Getter for value attribute.
	 * @return
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Returns string representation of the TokenConstantDouble.
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	/**
	 * Returns string representation of the TokenConstantDouble.
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
