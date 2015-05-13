package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * Basic node interface not directly used, contains a notion of subnodes/children.
 * @author Mihovil Vinković
 *
 */
public class Node {

	ArrayBackedIndexedCollection children;

	/**
	 * Adds a new child to the node.
	 * @param child The Node to be added as a child.
	 */
	public void addChildNode(Node child) {
		if (children == null) {
			children = new ArrayBackedIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * @return Current number of children.
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		} else {
			return children.size();
		}
	}

	/**
	 * Return a specific child.
	 * @param index Index of the child within the collection.
	 * @return The child desired.
	 */
	public Node getChild(int index) {
		return (Node) children.get(index);
	}

	/**
	 * Text representation of the node.
	 * @return String representation.
	 */
	public String asText() {
		return "";
	}
}
