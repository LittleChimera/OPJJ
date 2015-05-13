package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenOperator</code> sprema token operator.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenOperator extends Token {

	private String simbol;

	/**
	 * Konstruktor stvara instancu token operatora.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenOperator(String  simbol){
		this. simbol= simbol;
	}

	/**
	 * Metoda <code>asText</code> vraća string.
	 * @return string
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	@Override
	public String asText(){
		return  simbol;
	}
}
