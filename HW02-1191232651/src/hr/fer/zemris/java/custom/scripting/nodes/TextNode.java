package hr.fer.zemris.java.custom.scripting.nodes;

import java.lang.StringBuilder;

/**
 * Node representing a piece of textual data. Inherits from node class.
 * 
 * @author Erik Banek
 */
public class TextNode extends Node {
	/**
	 * Textual data of node
	 */
	private String text;
	
	/**
	 * Constructs a read-only TextNode with string text.
	 * 
	 * @param text data of TextNode to be constructed.
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/** Gets textual data of TextNode. */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Returns the text contained in TextNode formated to be like the
	 * custom language that is parsed.
	 */
	@Override
	public String generateText() {
		StringBuilder retStringBuilder = new StringBuilder();
		char[] array = this.text.toCharArray();
		for (int i=0; i<array.length; i++) {
			if (array[i] == '\\') {
				retStringBuilder.append("\\\\");//beautiful
			} else if (array[i] == '{') {
				retStringBuilder.append("\\{");
			} else {
				retStringBuilder.append(array[i]);
			}
		}
		return retStringBuilder.toString();
	}
}
