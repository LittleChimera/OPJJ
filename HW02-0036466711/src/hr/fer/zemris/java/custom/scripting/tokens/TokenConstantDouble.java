package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token for holding double values.
 * @author Mihovil Vinković
 *
 */
public class TokenConstantDouble extends Token {

	private double value;
	
	/**
	 * Basic constructor.
	 * @param value Value of the token.
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value + "";
	}
}
