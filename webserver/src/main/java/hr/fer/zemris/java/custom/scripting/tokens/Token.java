package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Represents a token inside of a Tag node.
 *
 * @author luka
 *
 */
public abstract class Token {

	/**
	 * Gets token's String representation.
	 *
	 * @return token's String representation.
	 */
	public String asText() {
		return new String();
	}

	/**
	 * Calls appropriate {@link ITokenVisitor}'s method for visiting the token.
	 *
	 * @param visitor
	 *            token visitor
	 */
	public abstract void accept(ITokenVisitor visitor);
}
