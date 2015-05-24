package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.Stack;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			writeToContext(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variableName = node.getVariable().getName();
			double currentValue = getTokenValue(node.getStartExpression());
			double end = getTokenValue(node.getEndExpression());
			double step = getTokenValue(node.getStepExpression());

			while (currentValue < end) {
				multistack.push(variableName, new ValueWrapper(currentValue));
				for (int j = 0, n = node.numberOfChildren(); j < n; j++) {
					node.getChild(j).accept(this);
				}
				currentValue = (double) multistack.pop(variableName).getValue();
				currentValue += step;
			}
		}

		private double getTokenValue(Token token) {
			if (token instanceof TokenConstantInteger) {
				return ((TokenConstantInteger) token).getValue();
			} else if (token instanceof TokenConstantDouble) {
				return ((TokenConstantDouble) token).getValue();
			} else {
				throw new RuntimeException("Illegal token in for loop");
			}
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			TokenExecutor tokenExecutor = new TokenExecutor(multistack);
			for (Token token : node.tokens()) {
				token.accept(tokenExecutor);
			}
			Stack<Object> printStack = tokenExecutor.getReverseStack();
			for (Object object : printStack) {
				System.out.print(object);
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

		private void writeToContext(String text) {
			try {
				requestContext.write(text);
			} catch (IOException e) {
				throw new RuntimeException("Writing context error");
			}
		}
	};

	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	public void execute() {
		documentNode.accept(visitor);
	}

	private static class TokenExecutor implements ITokenVisitor {

		private Stack<Object> calculationStack = new Stack<Object>();
		private ObjectMultistack multistack;

		public TokenExecutor(ObjectMultistack multistack) {
			this.multistack = multistack;
		}

		public Stack<Object> getReverseStack() {
			Stack<Object> reversedStack = new Stack<Object>();
			for (Object object : calculationStack) {
				reversedStack.push(object);
			}

			return reversedStack;
		}

		@Override
		public void visit(TokenConstantDouble token) {
			calculationStack.push(token.getValue());
		}

		@Override
		public void visit(TokenConstantInteger token) {
			calculationStack.push(token.getValue());
		}

		@Override
		public void visit(TokenFunction token) {
			String functionName = token.getName();
			switch (functionName) {
			case "sin":
				validateStackMinimumSize(1, functionName);
				double x = (double) calculationStack.pop();
				calculationStack.push(Math.sin(x));
				break;
			case "decfmt":

				break;
			case "dup":

				break;
			case "swap":

				break;
			case "setMimeType":

				break;
			case "paramGet":

				break;
			case "pparamGet":

				break;
			case "pparamSet":

				break;
			case "pparamDel":

				break;
			case "tparamGet":

				break;
			case "tparamSet":

				break;
			case "tparamDel":

				break;

			default:
				throw new RuntimeException("Unsupported function: "
						+ token.getName());
			}
		}

		private void validateStackMinimumSize(int minimumSize,
				String functionName) {
			if (calculationStack.size() < 1) {
				throw new RuntimeException("Expected at least " + minimumSize
						+ " arguments for function @" + functionName);
			}
		}

		@Override
		public void visit(TokenOperator token) {
			double operand1 = 0, operand2 = 0;
			try {
				operand2 = (double) calculationStack.pop();
				operand1 = (double) calculationStack.pop();
			} catch (EmptyStackException e) {
				// TODO: handle exception
			} catch (ClassCastException notANumber) {
				throw new RuntimeException("One of operands is not a number");
			}

			double result = 0;
			switch (token.getSymbol()) {
			case "+":
				result = operand1 + operand2;
				break;

			case "-":
				result = operand1 - operand2;
				break;

			case "*":
				result = operand1 * operand2;
				break;

			case "/":
				if (operand2 == 0) {
					throw new RuntimeException("Attempted to divide with zero");
				}
				result = operand1 / operand2;
				break;
			}
			calculationStack.push((result == (int) result) ? (int) result
					: result);
		}

		@Override
		public void visit(TokenString token) {
			calculationStack.push(token.getValue());
		}

		@Override
		public void visit(TokenVariable token) {
			Object variableValue;
			try {
				variableValue = multistack.peek(token.getName()).getValue();
			} catch (EmptyStackException e) {
				throw new RuntimeException("Variable " + token.getName()
						+ " is not initialized");
			}
			calculationStack.push(variableValue);
		}

	}

}
