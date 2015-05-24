package hr.fer.zemris.java.custom.scripting.tokens;

public interface ITokenVisitor {

	void visit(TokenConstantDouble token);
	void visit(TokenConstantInteger token);
	void visit(TokenFunction token);
	void visit(TokenOperator token);
	void visit(TokenString token);
	void visit(TokenVariable token);
	
	
}
