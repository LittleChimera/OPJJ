package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents root node of document tree model.
 * 
 * @author luka
 *
 */
public class DocumentNode extends Node {

	/**
	 * Creates empty node
	 */
	public DocumentNode() {
		super();
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
		for (int i = 0, n = numberOfChildren(); i < n; i++) {
			getChild(i).accept(visitor);
		}
	}

}
