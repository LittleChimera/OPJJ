package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import java.lang.StringBuilder;

/**
 * Node representing a command which generates output dynamically.
 * Inherits from the node class.
 * 
 * @author Erik Banek
 */
public class EchoNode extends Node {
	/**
	 * Tokens that will be output.
	 */
	private Token[] tokens;
	
	/**
	 * Constructs a read-only EchoNode.
	 * 
	 * @param tokens array of Token that belong to this echo node.
	 */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}
	
	/** Gets all the tokens of echo node in an array. */
	public Token[] getTokens() {
		return this.tokens;
	}
	
	/**
	 * Returns echo node converted back to string form.
	 */
	@Override
	public String generateText() {
		StringBuilder returnStringBuilder = new StringBuilder();
		returnStringBuilder.append("{$= ");
		
		for (int i=0; i<tokens.length; i++) {
			returnStringBuilder.append(tokens[i].asText());
			returnStringBuilder.append(" ");
		}
		
		returnStringBuilder.append("$}");
		return returnStringBuilder.toString();
	}
}
