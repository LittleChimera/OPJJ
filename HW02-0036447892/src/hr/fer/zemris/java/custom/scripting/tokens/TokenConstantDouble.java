package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Razred <code>TokenConstantDouble</code> sprema double tip tokena
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class TokenConstantDouble extends Token{

	private double vrijednost;

	/**
	 * Konstruktor stvara instancu double tokena.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public TokenConstantDouble(double vrijednost){
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
