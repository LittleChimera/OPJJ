package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token used for holding integer values.
 * @author miha
 *
 */
public class TokenConstantInteger extends Token {

	private int value;
	
	/**
	 * Basic constructor.
	 * @param value Value of the node.
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value + "";
	}
}
