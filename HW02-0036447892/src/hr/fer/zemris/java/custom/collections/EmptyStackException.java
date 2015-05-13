package hr.fer.zemris.java.custom.collections;


/**
 * Razred <code>EmptyStackException</code> brine se o iznimki kada je strog prazan te 
 * omogućuje korisnku uhvatiti iznimku na način da upozori korisnika metodom koja prima kao argument string,
 * ili samo uhvatiti iznimku.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor stvara instancu iznimke.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Konstruktor stvara instancu iznimke.
	 * @param s parametar koji daje ispis korisniku.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public EmptyStackException(String s) {
		super(s);
	}
}
