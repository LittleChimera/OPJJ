package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import java.lang.StringBuilder;

/**
 * Base class of all graph nodes.
 * 
 * @author Erik Banek
 */
public class Node {
	/**
	 * Holds direct children of the node. Is initialized only on adding 
	 * the first child of this node.
	 */
	private ArrayBackedIndexedCollection childArray;
	
	/**
	 * Constructs a Node, initializes underlying array to null.
	 */
	public Node() {
		this.childArray = null;
	}
	
	/**
	 * Adds a direct child node. Initializes array if that is the
	 * first child to be added.
	 * 
	 * @param child to be added to the array of children.
	 * @throws IllegalArgumentException if child is null.
	 */
	public void addChildNode(Node child) {
		if (this.childArray == null) {
			this.childArray = new ArrayBackedIndexedCollection();
		}
		this.childArray.add(child);
	}
	
	/**
	 * Gets the size of underlying array, if no children were added to array
	 * returns 0.
	 * 
	 * @return 0 if array is null, else size of array.
	 */
	public int numberOfChildren() {
		if (this.childArray == null) {
			return 0;
		}
		return this.childArray.size(); 
	}
	
	/**
	 * Gets the child that is placed in array[argument].
	 * 
	 * @param index of child that is wanted. Should be between 
	 * 				0 and numberOfChildren()-1 inclusively.
	 * @return wanted child.
	 * @throws NullPointerException if the underlying array wasn't initialized. 
	 * @throws IndexOutOfBoundsException if index is not in correct bounds.
	 */
	public Node getChild(int index) {
		if (this.childArray == null) {
			throw new NullPointerException();
		}
		return (Node) this.childArray.get(index);
	}
	
	/**
	 * Converts Node content back into string form. Serves mainly for checking
	 * the correctness of SmartScriptParser.
	 * 
	 * @return string content of a node.
	 */
	public String generateText() {
		if (this.childArray == null) {
			return "";
		}
		StringBuilder returnStringBuilder = new StringBuilder();
		for (int i=0; i<this.numberOfChildren(); i++) {
			Node currentChild = this.getChild(i);
			returnStringBuilder.append(currentChild.generateText());
		}
		return returnStringBuilder.toString();
	}
}
