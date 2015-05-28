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

			while (currentValue <= end) {
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
	}

	public void execute() {
		documentNode.accept(visitor);
	}

	private class TokenExecutor implements ITokenVisitor {

		private Stack<ValueWrapper> executionStack = new Stack<ValueWrapper>();

		public Stack<Object> getReverseStack() {
			Stack<Object> reversedStack = new Stack<Object>();
			for (ValueWrapper wrapper : executionStack) {
				reversedStack.push(wrapper.getValue());
			}

			return reversedStack;
		}

		@Override
		public void visit(TokenConstantDouble token) {
			executionStack.push(new ValueWrapper(token.getValue()));
		}

		@Override
		public void visit(TokenConstantInteger token) {
			executionStack.push(new ValueWrapper(token.getValue()));
		}

		@Override
		public void visit(TokenFunction token) {
			String functionName = token.getName();
			try {

				switch (functionName) {
				case "sin":
					validateStackMinimumSize(1, functionName);
					double x = (double) executionStack.pop().getValue();
					// calculate for degrees
					executionStack.push(new ValueWrapper(Math.sin(x*Math.PI/180)));
					break;

				case "decfmt":
					validateStackMinimumSize(2, functionName);
					String format = (String) executionStack.pop().getValue();
					DecimalFormat decmft;
					try {
						decmft = new DecimalFormat(format);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("Illegal decimal format pattern for function: @" + functionName);
					}
					double number = (double) executionStack.pop().getValue();
					executionStack.push(new ValueWrapper(decmft.format(number)));
					break;
				case "dup":
					executionStack.push(new ValueWrapper(executionStack.peek().getValue()));
					break;
				case "swap":
					ValueWrapper top = executionStack.pop();
					ValueWrapper second = executionStack.pop();
					executionStack.push(top);
					executionStack.push(second);
					break;
				case "setMimeType":
					String mimeType = (String) executionStack.pop().getValue();
					requestContext.setMimeType(mimeType);
					break;
				case "paramGet":
					validateStackMinimumSize(2, functionName);
					Object defaultValue = executionStack.pop().getValue();
					String param = (String) executionStack.pop().getValue();
					String paramValue = requestContext.getParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(paramValue) : new ValueWrapper(defaultValue));
					break;

				case "pparamGet":
					validateStackMinimumSize(2, functionName);
					defaultValue = executionStack.pop().getValue();
					param = (String) executionStack.pop().getValue();
					paramValue = requestContext.getPersistentParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(paramValue) : new ValueWrapper(defaultValue));
					break;
				case "pparamSet":
					String name = (String) executionStack.pop().getValue();
					Object value = executionStack.pop().getValue();
					requestContext.setPersistentParameter(name, value.toString());
					break;
				case "pparamDel":
					String paramName = (String) executionStack.pop().getValue();
					requestContext.removePersistentParameter(paramName);
					break;
				case "tparamGet":
					validateStackMinimumSize(2, functionName);
					defaultValue = executionStack.pop().getValue();
					param = (String) executionStack.pop().getValue();
					paramValue = requestContext.getTemporaryParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(paramValue) : new ValueWrapper(defaultValue));
					break;
				case "tparamSet":
					name = (String) executionStack.pop().getValue();
					value = executionStack.pop().getValue();
					requestContext.setTemporaryParameter(name, value.toString());
					break;
				case "tparamDel":
					paramName = (String) executionStack.pop().getValue();
					requestContext.removeTemporaryParameter(paramName);
					break;

				default:
					throw new RuntimeException("Unsupported function: "
							+ token.getName());
				}

			} catch (ClassCastException illegalArguments) {
				throw new RuntimeException("Illegal arguments for function: @" + functionName);
			} catch (java.util.EmptyStackException notEnoughArguments) {
				throw new RuntimeException("Not enough arguments given for function: @" + functionName);
			}
		}

		private void validateStackMinimumSize(int minimumSize,
				String functionName) {
			if (executionStack.size() < minimumSize) {
				throw new RuntimeException("Expected at least " + minimumSize
						+ " arguments for function @" + functionName);
			}
		}

		@Override
		public void visit(TokenOperator token) {
			ValueWrapper operand1 = null;
			Object operand2 = null;
			try {
				operand2 = executionStack.pop().getValue();
				operand1 = executionStack.pop();
			} catch (java.util.EmptyStackException e) {
				throw new RuntimeException("Not enough operands for operation: " + token.getSymbol());
			}

			try {
				switch (token.getSymbol()) {
				case "+":
					operand1.increment(operand2);
					break;

				case "-":
					operand1.decrement(operand2);
					break;

				case "*":
					operand1.multiply(operand2);
					break;

				case "/":
					operand1.divide(operand2);
					break;
				}
			} catch (Exception e) {
				throw new RuntimeException("Math error");
			}
			executionStack.push(operand1);
		}

		@Override
		public void visit(TokenString token) {
			executionStack.push(new ValueWrapper(token.getValue()));
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
			executionStack.push(new ValueWrapper(variableValue));
		}

	}

}
