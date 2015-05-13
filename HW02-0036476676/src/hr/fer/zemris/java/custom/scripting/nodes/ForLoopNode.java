package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
/**
 * ForLoop Node must have variable as first
 * variable and and can't have operator or function token.

 * @author Borna Feldšar
 * @version 1.0
 */
public class ForLoopNode extends Node {
	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;
	private Token[] tokens;
	
	/**
	 * Default constructor with all attributes
	 * 
	 * @param variable
	 *            Variable token for node
	 * @param startExpression
	 *            Token for start of for loop
	 * @param endExpression
	 *            Token for end of for loop
	 * @param stepExpression
	 *            Token for step of for loop. Can be null if step is not defined
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {
		if (variable == null || startExpression == null
				|| endExpression == null) {
			throw new IllegalArgumentException(
					"Arguments in ForLoopNode can't be null");
		}
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
		tokens = new Token[4];
		tokens[0] = variable;
		tokens[1] = startExpression;
		tokens[2] = endExpression;
		tokens[3] = stepExpression;
	}
	
	/**
	 * Default constructor with all attributes. Step expression is null.
	 * 
	 * @param variable
	 *            Variable token for node
	 * @param startExpression
	 *            Token for start of for loop
	 * @param endExpression
	 *            Token for end of for loop.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression) {
		this(variable, startExpression, endExpression, null);
	}
	
	/**
	 * Getter for variable attribute.
	 * @return variable
	 */
	public TokenVariable getVariable() {
		return variable;
	}
	
	/**
	 * Getter for startExpression attribute.
	 * @return startExpression
	 */
	public Token getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Getter for endExpression attribute.
	 * @return endExpression
	 */
	public Token getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Getter for stepExpression attribute.
	 * @return stepExpression
	 */
	public Token getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * Returns length of tokens array
	 * @return tokens length
	 */
	public int getNumberOfTokens() {
		return tokens.length;
	}
	
	/**
	 * Returns Token with index from token array.
	 * @param index index of requested Token
	 * @return Token with index
	 */
	public Token getToken(int index) {
		if (index < 0 || index > tokens.length - 1) {
			throw new NoSuchChildException("Invalid index!");
		}
		return tokens[index];
	}
	
	/**
	 * Returns string representation of the document node, which contains all childrens
	 * string representations.
	 */
	@Override
	public String asText() {
		String str = "{$ FOR";
		int size = numberOfChildren();
		for (int i = 0; i < 4; i++) {
			if (tokens[i] != null) {
				str += " " + tokens[i].asText();
			}
		}
		str += " $}";
		for (int i = 0; i < size; i++) {
			str += getChild(i).asText();
		}
		str += "{$END$}";
		return str;
	}
	
	/**
	 * Returns string representation of the document node, which is contains all childrens
	 * string representations for output.
	 */
	@Override
	public String toString() {
		String str = "{$ FOR";
		int size = numberOfChildren();
		for (int i = 0; i < 4; i++) {
			if (tokens[i] != null) {
				str += " " + tokens[i].toString();
			}
		}
		str += " $}";
		for (int i = 0; i < size; i++) {
			str += getChild(i).toString();
		}
		str += "{$END$}";
		return str;
	}
}
