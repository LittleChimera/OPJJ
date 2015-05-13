package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * Node in the document. Can have nested nodes as children
 * 
 * @author Borna Feldšar
 * @version 1.0
 */
public class Node {
	private ArrayBackedIndexedCollection collection;

	/**
	 * Adds new child to current node
	 * 
	 * @param child
	 *            node to be added as a child
	 */
	public void addChildNode(Node child) {
		if (child == null) {
			throw new NullChildException("Node can't be null");
		}
		if (collection == null) {
			collection = new ArrayBackedIndexedCollection();
		}
		collection.add(child);
	}

	/**
	 * Returns number of child nodes of this node
	 * 
	 * @return number of child nodes
	 */
	public int numberOfChildren() {
		if (collection == null) {
			return 0;
		} else {
			return collection.size();
		}
	}

	/**
	 * Returns child at given index from the list of children
	 * 
	 * @param index
	 *            index of a child in a list
	 * @return child with index
	 * @throws IllegalArgumentException
	 *             if index is less than 0 or bigger than size of the list
	 */
	public Node getChild(int index) {
		if (index < 0 || index > collection.size() - 1) {
			throw new NoSuchChildException("Invalid index!");
		}

		return (Node) collection.get(index);

	}
	/**
	 * Returns empty string.
	 * @return empty string.
	 */
	public String asText() {
		return "";
	}

}
