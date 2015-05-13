package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenVariable</code> sprema token varijablu.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenVariable extends Token {

	private String ime;

	/**
	 * Konstruktor stvara instancu token varijable.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenVariable(String ime){
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
