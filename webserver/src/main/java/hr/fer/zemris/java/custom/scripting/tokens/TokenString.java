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
		return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

}
