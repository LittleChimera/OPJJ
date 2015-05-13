package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing integer value.
 * 
 * @author Luka Skugor
 *
 */
public class TokenConstantInteger extends Token {

	private int value;

	/**
	 * Creates a token with given value.
	 * 
	 * @param value
	 *            integer
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

}
