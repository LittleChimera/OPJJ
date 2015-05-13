package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token used for storing the names of variables.
 * @author Mihovil Vinković
 *
 */
public class TokenVariable extends Token {

	private String name;
	
	/**
	 * Basic constructor.
	 * @param name Name of the variable.
	 */
	public TokenVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
}
