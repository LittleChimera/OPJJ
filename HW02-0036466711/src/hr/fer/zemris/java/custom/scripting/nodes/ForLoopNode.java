package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * Stores a representation of a for loop with all the necessary values.
 * 
 * @author Mihovil Vinković
 *
 */
public class ForLoopNode extends Node {

	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;

	/**
	 * Constructor that doesn't require a step expression.
	 * 
	 * @param variable
	 *            Variable used for looping.
	 * @param startExpression
	 *            Start value of the loop.
	 * @param endExpression
	 *            When this evaluates to true the loop ends.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression) {
		this(variable, startExpression, endExpression, null);
	}

	/**
	 * Constructor that takes all the values required for a for loop.
	 * 
	 * @param variable
	 *            Variable used for looping.
	 * @param startExpression
	 *            Start value of the loop.
	 * @param endExpression
	 *            When this evaluates to true the loop ends.
	 * @param stepExpression
	 *            Value of variable is changed by this value every step of the
	 *            loop.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {
		super();
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * @return Name of the variable.
	 */
	public TokenVariable getVariable() {
		return variable;
	}

	/**
	 * @return Start expression of the loop.
	 */
	public Token getStartExpression() {
		return startExpression;
	}

	/**
	 * @return End expression of the loop.
	 */
	public Token getEndExpression() {
		return endExpression;
	}

	/**
	 * @return Step expression of the loop.
	 */
	public Token getStepExpression() {
		return stepExpression;
	}

	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$FOR " + variable.asText() + " " + startExpression.asText()
				+ " " + endExpression.asText());
		if (stepExpression != null) {
			sb.append(" " + stepExpression.asText());
		}
		sb.append("$}");
		return sb.toString();
	}

}
