package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents a text node of a document tree model. Text node is body's plain
 * text.
 * 
 * @author Luka Skugor
 *
 */
public class TextNode extends Node {

	/**
	 * text of a text node
	 */
	private String text;

	/**
	 * Creates text node from a given string.
	 * 
	 * @param text
	 *            body of text node
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Returns literal text of the text node.
	 * 
	 * @return literal text body
	 */
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
	
	@Override
	public String toString() {
		return getText().replace("\\", "\\\\").replace("{$", "\\{$");
	}
}
