package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenConstantInteger</code> sprema integer tip tokena
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenConstantInteger extends Token{

	private int vrijednost;

	/**
	 * Konstruktor stvara instancu integer tokena.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenConstantInteger(int vrijednost){
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
		return String.valueOf(vrijednost);
	}
}
