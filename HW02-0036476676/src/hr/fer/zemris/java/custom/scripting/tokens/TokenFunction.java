package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a function inside tags of a document. Function starts with
 * character @. Valid function names start with letter and can contain letter, number
 * or underscore.
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenFunction extends Token {
	String name;
	
	/**
	 * Construct new TokenFunction with name.
	 * @param name name of a function
	 */
	public TokenFunction(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for name attribute.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Returns string representation of TokenFunction.
	 */
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Returns string representation of TokenFunction.
	 */
	@Override
	public String toString() {
		return name;
	}
}
