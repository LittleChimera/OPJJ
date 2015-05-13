package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node holding a simple text.
 * 
 * @author Mihovil Vinković
 *
 */
public class TextNode extends Node {

	private String text;

	/**
	 * Makes a new TextNode with the desired value.
	 * 
	 * @param text
	 *            The desired value of the TextNode.
	 */
	public TextNode(String text) {
		super();
		this.text = text;
	}

	/**
	 * The text held within the node.
	 * 
	 * @return Value of the text.
	 */
	public String getText() {
		return text;
	}

	@Override
	public String asText() {
		return getText();
	}

}
