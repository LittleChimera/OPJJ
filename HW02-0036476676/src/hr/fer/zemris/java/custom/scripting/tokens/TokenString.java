package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing string inside a tag of a document.
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenString extends Token {
	private String value;
	private String escapeValue;
	
	/**
	 * Constructs new TokenString with vaule.
	 * @param value value of string.
	 */
	public TokenString(String value){
		this.value=value;
		escapeValue=value.replace("\\", "\\\\");
		this.value=value.replace("\\\"", "\"");
	}
	
	/**
	 * Getter for string value.
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns string representation of TokenString for parsing.
	 */
	@Override 
	public String asText(){
		return "\""+escapeValue+"\"";
	}
	
	/**
	 * Returns string representation of TokenString.
	 */
	@Override
	public String toString(){
		return this.value;
	}
}
