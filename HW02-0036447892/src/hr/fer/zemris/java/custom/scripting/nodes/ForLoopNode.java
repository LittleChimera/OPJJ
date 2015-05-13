package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.*;

/**
 * Razred <code>ForLoopNode</code> sadrži tag For koji se sastoji od Tokena redom (varijabla,startExpression,
 * endExpression,stepExpression).
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class ForLoopNode extends Node {

	private Token varijabla;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;

	/**
	 * Konstruktor stvara instancu For čvora sa pratećim tokenima.
	 * Step token može imati null vrijednost.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public ForLoopNode(Token varijabla, Token startExpression,Token endExpression, Token stepExpression) {
		this.varijabla = varijabla;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Metoda <code>getVarijabla</code> dohvaća token varijablu.
	 * @return varijabla varijabla
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Token getVarijabla() {
		return varijabla;
	}

	/**
	 * Metoda <code>getStartExpression</code> dohvaća token StartExpression.
	 * @return startExpression StartExpression
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Token getStartExpression() {
		return startExpression;
	}

	/**
	 * Metoda <code>getEndExpression</code> dohvaća token EndExpression.
	 * @return endExpression EndExpression.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Token getEndExpression() {
		return endExpression;
	}

	/**
	 * Metoda <code>getStepExpression</code> dohvaća token StepExpression.
	 * @return stepExpression StepExpression.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Token getStepExpression() {
		return stepExpression;
	}

}
