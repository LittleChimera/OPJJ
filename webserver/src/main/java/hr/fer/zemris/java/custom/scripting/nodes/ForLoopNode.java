package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * This class represents ForLoop node in document tree model
 * 
 * @author luka
 *
 */
public class ForLoopNode extends Node {

	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;

	/**
	 * Creates ForLoop node with all ForLoop's tokens. stepExpression can be
	 * null.
	 * 
	 * @param variable
	 *            variable token
	 * @param startExpression
	 *            start expression token
	 * @param endExpression
	 *            end expression token
	 * @param stepExpression
	 *            step expression token (can be null)
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
		for (int i = 0, n = numberOfChildren(); i < n; i++) {
			getChild(i).accept(visitor);
		}
		visitor.visitForLoopNode(null);
	}
	
	@Override
	public String toString() {
		StringBuilder forLoopBuilder = new StringBuilder();

		forLoopBuilder.append("{$FOR ").append(variable.asText() + " ")
				.append(startExpression.asText() + " ")
				.append(endExpression.asText() + " ");
		if (stepExpression != null) {
			forLoopBuilder.append(stepExpression.asText() + " ");
		}
		forLoopBuilder.append("$}");

		return forLoopBuilder.toString();
	}

}
