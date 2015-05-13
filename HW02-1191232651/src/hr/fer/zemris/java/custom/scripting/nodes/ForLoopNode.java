package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.*;
import java.lang.StringBuilder;

/**
 * Node representing a single for-loop construct. Inherits from node class.
 * All fields except stepExpression cannot be null.
 * 
 * @author Erik Banek
 */
public class ForLoopNode extends Node {
	/** Holds the name of variable in the loop.*/
	private TokenVariable variable;
	/** Start expression in loop, usually a number.*/
	private Token startExpression;
	/** End expression in loop, usually a number. */
	private Token endExpression;
	/** Step expression for exotic for-loops. Can be null. */
	private Token stepExpression;
	
	/**
	 * Constructs a read-only for-loop with four constructor arguments.
	 * 
	 * @param variable
	 * @param startExpression
	 * @param endExpression
	 * @param stepExpression
	 * 
	 * @throws IllegalArgumentException if any of first three values are null.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression, 
			Token endExpression, Token stepExpression) {
		if(variable == null || startExpression == null || endExpression == null) {
			throw new IllegalArgumentException();
		}
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Constructs a read-only for-loop node with three arguments.
	 * 
	 * @param variable
	 * @param startExpression
	 * @param endExpression
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression, 
			Token endExpression) {
		this(variable, startExpression, endExpression, null);
	}
	
	/**
	 * Getter of read-only token.
	 * @return wanted value;
	 */
	public TokenVariable getVariable() {
		return this.variable;
	}
	
	/**
	 * Getter of read-only token.
	 * @return wanted value;
	 */
	public Token getStartExpression() {
		return this.startExpression;
	}
	
	/**
	 * Getter of read-only token.
	 * @return wanted value;
	 */
	public Token getEndExpression() {
		return this.endExpression;
	}
	
	/**
	 * Getter of read-only token.
	 * @return wanted value;
	 */
	public Token getStepExpression() {
		return this.stepExpression;
	}
	
	/**
	 * Converts ForLoopNode information back to string form.
	 * Mainly for checking correctness of SmartParser.
	 * 
	 * @return all text from ForLoopNode converted into format for parsing.
	 */
	@Override
	public String generateText() {
		StringBuilder returnStringBuilder = new StringBuilder();
		returnStringBuilder.append("{$FOR ");
		returnStringBuilder = appendForLoopVariables(returnStringBuilder);
		returnStringBuilder.append("$}");
		
		if (this.numberOfChildren()>0) {
			for (int i=0; i<this.numberOfChildren(); i++) {
				returnStringBuilder.append(this.getChild(i).generateText());
			}
		}
		
		//no spaces before or after end! because text nodes should contain that.
		returnStringBuilder.append("{$END$}");
		return returnStringBuilder.toString();
	}
	
	/**
	 * Appends to given StringBuilder tokens that are in the FOR tag.
	 * Makes the generateText() method more readable. 
	 * 
	 * @param sb StringBuilder to append to.
	 * @return StringBuilder with tokens appended.
	 */
	private StringBuilder appendForLoopVariables(StringBuilder sb) {
		sb.append(this.variable.asText() + " ");
		sb.append(this.startExpression.asText() + " ");
		if (this.stepExpression != null) {
			sb.append(this.stepExpression.asText() + " ");
		}
		sb.append(this.endExpression.asText() + " ");
		return sb;
	}
}
