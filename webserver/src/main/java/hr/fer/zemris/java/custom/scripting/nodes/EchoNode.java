package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Represents ECHO Tag node in document tree model.
 * 
 * @author Luka Skugor
 *
 */
public class EchoNode extends Node {

	/**
	 * all tokens inside of echo tag
	 */
	private Token[] tokens;

	/**
	 * Creates an echo node with fixed number of tokens.
	 * 
	 * @param numberOfTokens
	 *            fixed number of tokens
	 */
	public EchoNode(int numberOfTokens) {
		tokens = new Token[numberOfTokens];
	}

	/**
	 * Inserts a token at given index. If token exists at given index it
	 * overwrites it.
	 * 
	 * @param index
	 *            position to be inserted at
	 * @param token
	 *            Token to be inserted
	 * @throws IndexOutOfBoundsException
	 *             if index is greater or equal than the available number of
	 *             tokens.
	 */
	public void insert(int index, Token token) {
		if (index >= tokens.length) {
			throw new IndexOutOfBoundsException("Invalid index location.");
		}
		tokens[index] = token;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

	@Override
	public String toString() {
		StringBuilder echoBuilder = new StringBuilder();

		echoBuilder.append("{$= ");
		for (Token token : tokens) {
			echoBuilder.append(token.asText() + " ");
		}
		echoBuilder.append("$}");

		return echoBuilder.toString();
	}
}
