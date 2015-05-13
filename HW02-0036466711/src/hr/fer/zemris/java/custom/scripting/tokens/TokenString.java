package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token used for storing strings.
 * @author Mihovil Vinković
 *
 */
public class TokenString extends Token {

	private String value;
	
	/**
	 * Basic constructor.
	 * @param value The string value.
	 */
	public TokenString(String value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value;
	}
}
