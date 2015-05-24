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
	
	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Get's token's variable name.
	 * @return token's variable name
	 */
	public String getName() {
		return name;
	}

}
