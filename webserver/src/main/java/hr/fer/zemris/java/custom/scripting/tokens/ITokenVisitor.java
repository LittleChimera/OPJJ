package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ITokenVisitor represents a {@link Token} processor implementing a visitor pattern.
 *
 * @author Luka Skugor
 *
 */
public interface ITokenVisitor {

	/**
	 * This method is called when visiting a {@link TokenConstantDouble} token.
	 * @param token visited token
	 */
	void visit(TokenConstantDouble token);
	/**
	 * This method is called when visiting a {@link TokenConstantInteger} token.
	 * @param token visited token
	 */
	void visit(TokenConstantInteger token);
	/**
	 * This method is called when visiting a {@link TokenFunction} token.
	 * @param token visited token
	 */
	void visit(TokenFunction token);
	/**
	 * This method is called when visiting a {@link TokenOperator} token.
	 * @param token visited token
	 */
	void visit(TokenOperator token);
	/**
	 * This method is called when visiting a {@link TokenString} token.
	 * @param token visited token
	 */
	void visit(TokenString token);
	/**
	 * This method is called when visiting a {@link TokenVariable} token.
	 * @param token visited token
	 */
	void visit(TokenVariable token);


}
