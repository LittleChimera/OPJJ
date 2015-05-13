package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token used for storing function names.
 * @author Mihovil Vinković
 *
 */
public class TokenFunction extends Token {

	private String name;
	
	/**
	 * Basic constructor.
	 * @param name Function name.
	 */
	public TokenFunction(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return "@" + name;
	}
}
