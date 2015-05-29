package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * INodeVisitor represents a {@link Node} processor implementing a visitor pattern.
 *
 * @author Luka Skugor
 *
 */
public interface INodeVisitor {
	/**
	 * This method is called when visiting a {@link TextNode} node.
	 * @param node visited node
	 */
	public void visitTextNode(TextNode node);

	/**
	 * This method is called when visiting a {@link ForLoopNode} node.
	 * @param node visited node
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * This method is called when visiting a {@link EchoNode} node.
	 * @param node visited node
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * This method is called when visiting a {@link DocumentNode} node.
	 * @param node visited node
	 */
	public void visitDocumentNode(DocumentNode node);
}
