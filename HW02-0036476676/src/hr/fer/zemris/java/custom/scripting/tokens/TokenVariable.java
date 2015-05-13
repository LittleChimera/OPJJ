package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing a variable inside a tag of a document. Valid variable
 * names start with letter and can contain letter, number or underscore.
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenVariable extends Token {
	private String name;
	
	/**
	 * Consturcts new TokenVariable with string name.
	 * @param name name of variable
	 */
	public TokenVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Returns string representation of TokenVariable.
	 */
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Getter for TokenVariable name.
	 * @return name of TokenVariable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns string representation of TokenVariable.
	 */
	@Override
	public String toString() {
		return name;
	}
}
