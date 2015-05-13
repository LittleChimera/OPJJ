package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Razred <code>EchoNode</code> sadrži tag Echo koji se sastoji od niza Tokena.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class EchoNode extends Node {

	private Token[] tokeni;

	/**
	 * Konstruktor stvara instancu Echo čvora sa pratećim tokenima.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public EchoNode(Token[] tokeni) {
		this.tokeni = tokeni;
	}

	/**
	 * Metoda <code>getTokeni</code> dohvaća tokene.
	 * @return tokeni polje tokena za Echo čvor.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Token[] getTokeni() {
		return tokeni;
	}
}
