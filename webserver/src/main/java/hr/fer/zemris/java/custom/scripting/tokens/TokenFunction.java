package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a function.
 * 
 * @author Luka Skugor
 *
 */
public class TokenFunction extends Token {

	/**
	 * name of the function
	 */
	private String name;

	/**
	 * Creates a token function with given name
	 * 
	 * @param name
	 *            function's name
	 */
	public TokenFunction(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return "@" + name;
	}
	
	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Gets token's function name.
	 * @return token's function name
	 */
	public String getName() {
		return name;
	}
}
