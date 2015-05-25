package hr.fer.zemris.java.custom.scripting.exec;

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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

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
			TokenExecutor tokenExecutor = new TokenExecutor();
			for (Token token : node.tokens()) {
				token.accept(tokenExecutor);
			}
			Stack<Object> printStack = tokenExecutor.getReverseStack();
			for (Object object : printStack) {
				if (object instanceof Double
						&& (double) object == (int) ((Double) object)
						.doubleValue()) {
					writeToContext("" + (int) ((Double) object).doubleValue());
					continue;
				}
				writeToContext(object.toString());
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
		//requestContext.setMimeType("text/plain");
	}

	public void execute() {
		documentNode.accept(visitor);
	}

	private class TokenExecutor implements ITokenVisitor {

		private Stack<Object> executionStack = new Stack<Object>();

		public Stack<Object> getReverseStack() {
			Stack<Object> reversedStack = new Stack<Object>();
			for (Object object : executionStack) {
				reversedStack.push(object);
			}

			return reversedStack;
		}

		@Override
		public void visit(TokenConstantDouble token) {
			executionStack.push(token.getValue());
		}

		@Override
		public void visit(TokenConstantInteger token) {
			executionStack.push(token.getValue());
		}

		@Override
		public void visit(TokenFunction token) {
			String functionName = token.getName();
			try {

				switch (functionName) {
				case "sin":
					validateStackMinimumSize(1, functionName);
					double x = (double) executionStack.pop();
					executionStack.push(Math.sin(x));
					break;
				case "decfmt":
					validateStackMinimumSize(2, functionName);
					String format = (String) executionStack.pop();
					DecimalFormat decmft;
					try {
						decmft = new DecimalFormat(format);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("Illegal decimal format pattern for function: @" + functionName);
					}
					Double number = (Double) executionStack.pop();
					executionStack.push(decmft.format(number));
					break;
				case "dup":

					break;
				case "swap":

					break;
				case "setMimeType":
					String mimeType = (String) executionStack.pop();
					requestContext.setMimeType(mimeType);
					break;
				case "paramGet":
					validateStackMinimumSize(2, functionName);
					Number defaultValue = (Number) executionStack.pop();
					String param = (String) executionStack.pop();
					String paramValue = requestContext.getParameter(param);
					Number value = (paramValue != null)?Double.parseDouble(paramValue):null;
					//TODO obradi exception
					executionStack.push(value == null ? defaultValue : value);
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

			} catch (ClassCastException illegalArguments) {
				throw new RuntimeException("Illegal arguments for function: @" + functionName);
			}
		}

		private void validateStackMinimumSize(int minimumSize,
				String functionName) {
			if (executionStack.size() < 1) {
				throw new RuntimeException("Expected at least " + minimumSize
						+ " arguments for function @" + functionName);
			}
		}

		@Override
		public void visit(TokenOperator token) {
			double operand1 = 0, operand2 = 0;
			try {
				operand2 = (double) executionStack.pop();
				operand1 = (double) executionStack.pop();
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
			executionStack.push((result == (int) result) ? (int) result
					: result);
		}

		@Override
		public void visit(TokenString token) {
			executionStack.push(token.getValue());
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
			executionStack.push(variableValue);
		}

	}

}
