package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenFunction</code> sprema funkcijski tokena
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenFunction extends Token {

	private String ime;

	/**
	 * Konstruktor stvara instancu funkcijskog tokena.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenFunction(String ime){
		this.ime=ime;
	}

	/**
	 * Metoda <code>asText</code> vraća string.
	 * @return string
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	@Override
	public String asText(){
		return ime;
	}
}
