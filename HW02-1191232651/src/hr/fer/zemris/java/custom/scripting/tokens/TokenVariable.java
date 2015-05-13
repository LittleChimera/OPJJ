package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token class that represents a single variable by its name.
 * 
 * @author Erik Banek
 */
public class TokenVariable extends Token {
	/** Name of variable in token. */
	private String name;

	/** Constructs a read-only token, that holds a variable name. */
	public TokenVariable(String name) {
		this.name = name;
	}
	
	/** Token variable name getter. */
	public String getName() {
		return this.name;
	}

	@Override
	public String asText() {
		return this.name;
	}
}
