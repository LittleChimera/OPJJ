package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a variable.
 * 
 * @author Luka Skugor
 *
 */
public class TokenVariable extends Token {

	/**
	 * name of the variable
	 */
	private String name;

	/**
	 * Creates a token variable with given name.
	 * 
	 * @param name
	 *            name of the variable
	 */
	public TokenVariable(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}

}
