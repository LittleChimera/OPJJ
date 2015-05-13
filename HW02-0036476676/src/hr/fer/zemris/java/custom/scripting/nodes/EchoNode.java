package hr.fer.zemris.java.custom.scripting.nodes;
/**
 * Node with various tokens inside.
 */
import hr.fer.zemris.java.custom.scripting.tokens.Token;

public class EchoNode extends Node {
	private Token[] tokens;
	
	/**
	 * Constructs new EchoNode with array of Tokens.
	 * @param tokens arry of tokens inside echo node.
	 */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Returns length of tokens array.
	 * @return
	 */
	public int getNumberOfTokens() {
		return tokens.length;
	}
	
	/**
	 * Returns Token with index from array of Tokens.
	 * @param index index of Token in array.
	 * @return Token with index
	 * @throws NoSuchChildException if index is smaller then 0 and bigger than size-1.
	 */
	public Token getToken(int index) {
		if (index < 0 || index > tokens.length - 1) {
			throw new NoSuchChildException("Invalid index!");
		}
		return tokens[index];
	}
	
	/**
	 * Returns string representation of the EchoNode, which contains all childrens
	 * string representations.
	 */
	@Override
	public String asText() {
		String str = "{$ =";
		int size = tokens.length;
		for (int i = 0; i < size; i++) {
			str += " " + tokens[i].asText();

		}
		str += " $}";
		return str;
	}
	
	/**
	 * Returns string representation of the EchoNode, which contains all childrens
	 * string representations for output.
	 */
	@Override
	public String toString() {
		String str = "{$ =";
		int size = tokens.length;
		for (int i = 0; i < size; i++) {

			str += " " + tokens[i].toString();
		}
		str += " $}";
		return str;
	}
}
