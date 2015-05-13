package hr.fer.zemris.java.hw2;

/**
 * Razred <code>SmartScriptParserException</code> brine se o iznimki kad kosisnik preda krivi string
 * za parsiranje.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 2L;

	/**
	 * Konstruktor stvara instancu iznimke.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Konstruktor stvara instancu iznimke.
	 * @param s parametar koji daje ispis korisniku.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public SmartScriptParserException(String s) {
		super(s);
	}
}
