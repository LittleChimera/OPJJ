package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenString</code> sprema string operator.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenString extends Token {

	private String vrijednost;

	/**
	 * Konstruktor stvara instancu token string.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenString(String vrijednost){
		this.vrijednost=vrijednost;
	}

	/**
	 * Metoda <code>asText</code> vraća string.
	 * @return string
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	@Override
	public String asText(){
		return vrijednost;
	}	
}
