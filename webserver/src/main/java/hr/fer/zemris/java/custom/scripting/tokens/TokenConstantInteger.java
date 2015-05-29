package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing integer value.
 *
 * @author Luka Skugor
 *
 */
public class TokenConstantInteger extends Token {

	/**
	 * <code>double</code> value of the token
	 */
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

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets token's integer value.
	 * @return token's integer value
	 */
	public int getValue() {
		return value;
	}
}
