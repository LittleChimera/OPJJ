package hr.fer.zemris.java.custom.scripting.nodes;
/**
 * Node representing one whole document.
 * @author Borna Feldšar
 *
 */
public class DocumentNode extends Node {
	
	/**
	 * Returns string representation of the document node, which contains all childrens
	 * string representations.
	 */
	@Override
	public String asText() {
		String str = "";
		int size = numberOfChildren();
		for (int i = 0; i < size; i++) {
			str += getChild(i).asText();
		}
		return str;
	}
	
	/**
	 * Returns string representation of the document node, which is contains all childrens
	 * string representations for output.
	 */
	@Override
	public String toString() {
		String str = "";
		int size = numberOfChildren();
		for (int i = 0; i < size; i++) {
			str += getChild(i).toString();
		}
		return str;
	}

}
