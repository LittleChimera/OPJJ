package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * This class is representation of basic node in document tree model.
 * 
 * @author Luka Skugor
 *
 */
public class Node {

	/**
	 * collection of all child nodes
	 */
	ArrayBackedIndexedCollection childrenCollection;

	/**
	 * Adds child to the node.
	 * 
	 * @param child
	 *            child node
	 */
	public void addChildNode(Node child) {
		if (childrenCollection == null) {
			childrenCollection = new ArrayBackedIndexedCollection();
		}

		childrenCollection.add(child);
	}

	/**
	 * Gets number of how many children the node has.
	 * 
	 * @return number of children
	 */
	public int numberOfChildren() {
		if (childrenCollection == null) {
			return 0;
		} else {
			return childrenCollection.size();
		}

	}

	/**
	 * Gets child at index location.
	 * 
	 * @param index
	 *            position on which child is located
	 * @return child at requested index
	 */
	public Node getChild(int index) {
		return (Node) childrenCollection.get(index);
	}

	@Override
	public String toString() {
		StringBuilder nodeStringBuilder = new StringBuilder();
		for (int index = 0, size = numberOfChildren(); index < size; index++) {
			nodeStringBuilder.append(getChild(index).toString());
		}

		return nodeStringBuilder.toString();
	}

}
