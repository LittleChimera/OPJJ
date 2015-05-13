package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred <code>EchoNode</code> sadrži TextNode koji se sastoji od niza znakova.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TextNode extends Node{

	private String tekst;

	/**
	 * Konstruktor stvara instancu TectNode čvora sa pratećim nizom znakova.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TextNode(String tekst){
		this.tekst=tekst;	
	}

	/**
	 * Metoda <code>getTekst</code> dohvaća tekst.
	 * @return tekst tekst
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public String getTekst() {
		return tekst;
	}
}
