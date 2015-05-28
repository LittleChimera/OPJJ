package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a String value.
 *
 * @author Luka Skugor
 *
 */
public class TokenString extends Token {

	private String value;

	/**
	 * Creates a string token with given value.
	 *
	 * @param value
	 *            value of the string
	 */
	public TokenString(String value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return "\""
				+ value.replaceAll("\\\\", "\\").replaceAll("\"", "\\\"")
				.replaceAll("\\n", "\\\\n").replaceAll("\\r", "\\\\r")
				.replaceAll("\\t", "\\\\t") + "\"";
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets token's string.
	 *
	 * @return token's string
	 */
	public String getValue() {
		return value;
	}

}
