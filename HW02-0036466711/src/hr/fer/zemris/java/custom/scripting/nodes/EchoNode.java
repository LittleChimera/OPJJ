package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Node used for storing what an empty tag contains.
 * 
 * @author Mihovil Vinković
 */
public class EchoNode extends Node {

	private Token[] tokens;

	/**
	 * Only constructor.
	 * 
	 * @param tokens
	 *            Array of tokens that the tag contains.
	 */
	public EchoNode(Token[] tokens) {
		super();
		this.tokens = tokens;
	}

	/**
	 * Returns the value of the tokens contained.
	 * 
	 * @return Tokens contained.
	 */
	public Token[] getTokens() {
		return tokens;
	}

	@Override
	public String asText() {
		String ret = "";
		for (Token t : tokens) {
			ret += " " + t.asText();
		}
		ret = "{$= " + ret.substring(1) + "$}";
		return ret;
	}
}
