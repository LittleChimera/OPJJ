package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing an integer number constant inside a tags.
 * @author Borna Feldšar
 * @version 1.0
 */
public class TokenConstantInteger extends Token {
			int value;
			
			/**
			 * Constructs new TokenConstantInteger with value.
			 * @param value value of integer number constant
			 */
			public TokenConstantInteger(int value){
				this.value=value;
			}
			
			/**
			 * Getter for value.
			 * @return
			 */
			public int getValue() {
				return value;
			}
			
			/**
			 * Returns string representation of the TokenConstantInteger.
			 */
			@Override 
			public String asText(){
				return String.valueOf(value);
			}
			
			/**
			 * Returns string representation of TokenConstantInteger.
			 */
			@Override
			public String toString() {
				return String.valueOf(value);
			}
}
