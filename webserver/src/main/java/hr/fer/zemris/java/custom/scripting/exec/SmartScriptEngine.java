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

/**
 * Engine which executes a Smart Script using given {@link RequestContext} and
 * writes results using {@link RequestContext#write}.
 *
 * For TextNode it writes the text that node contains using requestContext's
 * write method.
 *
 * For ForLoopNode it iterates over element which ForLoopNode surrounds.
 * Iteration will be made as long as long as start variable is less or equals
 * than end value at the ForLoop block end. Start value is increased after each
 * iteration for a step value which is 1 by default. Start value can also be
 * changed in the block itself by Echo block.
 *
 * For EchoNode it goes through every parameter and applies all operations and
 * functions. Finally when everything is calculated, the rest and the calculated
 * elements are written using {@link RequestContext#write}.
 *
 * Supported operations are +, -, * and /. Every operator takes last 2 tokens
 * for calculation.
 *
 * Each function takes enough of last elements which are needed to apply the
 * function.
 *
 * Supported functions are:
 *
 * • sin(x) ; calculates sinus from given argument and stores the result back to
 * stack. Conceptually, equals to: x = pop(), r = sin(x), push(r) .
 *
 * • decfmt(x,f) ; formats decimal number using given format f which is
 * compatible with DecimalFormat; produces a string. X can be integer, double or
 * string representation of a number. Conceptually, equals to: f = pop(), x =
 * pop(), r = decfmt(x,f), push(r) .
 *
 * • dup() ; duplicates current top value from stack. Conceptually, equals to: x
 * = pop(), push(x), push(x) . • swap() ; replaces the order of two topmost
 * items on stack. Conceptually, equals to: a = pop(), b = pop(), push(a),
 * push(b) .
 *
 * • setMimeType(x) ; takes string x and calls requestContext.setMimeType(x) .
 * Does not produce any result.
 *
 * • paramGet(name, defValue) ; Obtains from requestContext parameters map a
 * value mapped for name and pushes it onto stack. If there is no such mapping,
 * it pushes instead defValue onto stack. Conceptually, equals to: dv = pop(),
 * name = pop(), value=reqCtx.getParam(name), push(value==null ? defValue :
 * value) .
 *
 * • pparamGet(name, defValue) ; same as paramGet but reads from requestContext
 * persistent parameters map.
 *
 * • pparamSet(value, name) ; stores a value into requestContext persistant
 * parameters map. Conceptually, equals to: name = pop(), value = pop(),
 * reqCtx.setPerParam(name, value) .
 *
 * • pparamDel(name) ; removes association for name from requestContext
 * persistentParameters map.
 *
 * • tparamGet(name, defValue) ; same as paramGet but reads from requestContext
 * temporaryParameters map.
 *
 * • tparamSet(value, name) ; stores a value into requestContext
 * temporaryParameters map. Conceptually, equals to: name = pop(), value =
 * pop(), reqCtx.setTmpParam(name, value) .
 *
 * • tparamDel(name) ; removes association for name from requestContext
 * temporaryParameters map.
 *
 * @author Luka Skugor
 *
 */
public class SmartScriptEngine {

	/**
	 * Document Node of the script which will be executed.
	 */
	private DocumentNode documentNode;
	/**
	 * Request context which is used for processing the script.
	 */
	private RequestContext requestContext;
	/**
	 * Multistack which is used for keeping variable values. Each value is kept on its own stack.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	/**
	 * Visitor which visits each node and performs it's function.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		/* (non-Javadoc)
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitTextNode(hr.fer.zemris.java.custom.scripting.nodes.TextNode)
		 */
		@Override
		public void visitTextNode(TextNode node) {
			writeToContext(node.getText());
		}

		/* (non-Javadoc)
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitForLoopNode(hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode)
		 */
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

		/**
		 * Gets token value as <code>double</code>.
		 * @param token token which value will be cast to <code>double</code>
		 * @return token's value as double
		 * @throws RuntimeException if token can't be converted to <code>double</code>
		 */
		private double getTokenValue(Token token) {
			if (token instanceof TokenConstantInteger) {
				return ((TokenConstantInteger) token).getValue();
			} else if (token instanceof TokenConstantDouble) {
				return ((TokenConstantDouble) token).getValue();
			} else {
				throw new RuntimeException("Illegal token in for loop");
			}
		}

		/* (non-Javadoc)
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitEchoNode(hr.fer.zemris.java.custom.scripting.nodes.EchoNode)
		 */
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

		/* (non-Javadoc)
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitDocumentNode(hr.fer.zemris.java.custom.scripting.nodes.DocumentNode)
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

		/**
		 * Writes a string to context using {@link RequestContext#write}.
		 * @param text text which will be written
		 */
		private void writeToContext(String text) {
			try {
				requestContext.write(text);
			} catch (IOException e) {
				throw new RuntimeException("Writing context error");
			}
		}
	};

	/**
	 * Creates a new SmartScriptEngine with given {@link DocumentNode} and {@link RequestContext}.
	 * @param documentNode {@link DocumentNode} of the executing script
	 * @param requestContext request context which is used for executing the script
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes the script.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

	/**
	 * Executes tokens from {@link EchoNode}.
	 *
	 * @author Luka Skugor
	 *
	 */
	private class TokenExecutor implements ITokenVisitor {

		/**
		 * Stack which is used for performing functions and operations of the node.
		 */
		private Stack<ValueWrapper> executionStack = new Stack<ValueWrapper>();

		/**
		 * Gets reversed execution stack.
		 * @return reversed execution stack
		 */
		public Stack<Object> getReverseStack() {
			Stack<Object> reversedStack = new Stack<Object>();
			for (ValueWrapper wrapper : executionStack) {
				reversedStack.push(wrapper.getValue());
			}

			return reversedStack;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor#visit(hr
		 * .fer.zemris.java.custom.scripting.tokens.TokenConstantDouble)
		 */
		@Override
		public void visit(TokenConstantDouble token) {
			executionStack.push(new ValueWrapper(token.getValue()));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor#visit(hr
		 * .fer.zemris.java.custom.scripting.tokens.TokenConstantInteger)
		 */
		@Override
		public void visit(TokenConstantInteger token) {
			executionStack.push(new ValueWrapper(token.getValue()));
		}

		/**
		 * @throws RuntimeException if function could not take enough arguments
		 */
		@Override
		public void visit(TokenFunction token) {
			String functionName = token.getName();
			try {

				switch (functionName) {
				case "sin":
					validateStackMinimumSize(1, functionName);
					double x = (double) executionStack.pop().getValue();
					// calculate for degrees
					executionStack.push(new ValueWrapper(Math.sin(x * Math.PI
							/ 180)));
					break;

				case "decfmt":
					validateStackMinimumSize(2, functionName);
					String format = (String) executionStack.pop().getValue();
					DecimalFormat decmft;
					try {
						decmft = new DecimalFormat(format);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(
								"Illegal decimal format pattern for function: @"
										+ functionName);
					}
					double number = (double) executionStack.pop().getValue();
					executionStack
					.push(new ValueWrapper(decmft.format(number)));
					break;
				case "dup":
					validateStackMinimumSize(1, functionName);
					executionStack.push(new ValueWrapper(executionStack.peek()
							.getValue()));
					break;
				case "swap":
					validateStackMinimumSize(2, functionName);
					ValueWrapper top = executionStack.pop();
					ValueWrapper second = executionStack.pop();
					executionStack.push(top);
					executionStack.push(second);
					break;
				case "setMimeType":
					validateStackMinimumSize(1, functionName);
					String mimeType = (String) executionStack.pop().getValue();
					requestContext.setMimeType(mimeType);
					break;
				case "paramGet":
					validateStackMinimumSize(2, functionName);
					Object defaultValue = executionStack.pop().getValue();
					String param = (String) executionStack.pop().getValue();
					String paramValue = requestContext.getParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(
							paramValue) : new ValueWrapper(defaultValue));
					break;

				case "pparamGet":
					validateStackMinimumSize(2, functionName);
					defaultValue = executionStack.pop().getValue();
					param = (String) executionStack.pop().getValue();
					paramValue = requestContext.getPersistentParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(
							paramValue) : new ValueWrapper(defaultValue));
					break;
				case "pparamSet":
					validateStackMinimumSize(2, functionName);
					String name = (String) executionStack.pop().getValue();
					Object value = executionStack.pop().getValue();
					requestContext.setPersistentParameter(name,
							value.toString());
					break;
				case "pparamDel":
					validateStackMinimumSize(1, functionName);
					String paramName = (String) executionStack.pop().getValue();
					requestContext.removePersistentParameter(paramName);
					break;
				case "tparamGet":
					validateStackMinimumSize(2, functionName);
					defaultValue = executionStack.pop().getValue();
					param = (String) executionStack.pop().getValue();
					paramValue = requestContext.getTemporaryParameter(param);
					executionStack.push(paramValue != null ? new ValueWrapper(
							paramValue) : new ValueWrapper(defaultValue));
					break;
				case "tparamSet":
					validateStackMinimumSize(2, functionName);
					name = (String) executionStack.pop().getValue();
					value = executionStack.pop().getValue();
					requestContext
					.setTemporaryParameter(name, value.toString());
					break;
				case "tparamDel":
					validateStackMinimumSize(1, functionName);
					paramName = (String) executionStack.pop().getValue();
					requestContext.removeTemporaryParameter(paramName);
					break;

				default:
					throw new RuntimeException("Unsupported function: "
							+ functionName);
				}

			} catch (ClassCastException illegalArguments) {
				throw new RuntimeException("Illegal arguments for function: @"
						+ functionName);
			} catch (java.util.EmptyStackException notEnoughArguments) {
				throw new RuntimeException(
						"Not enough arguments given for function: @"
								+ functionName);
			}
		}

		/**
		 * Validates that function has enough arguments to take.
		 * @param argumentsCount number of arguments which function takes
		 * @param functionName name of the function
		 */
		private void validateStackMinimumSize(int argumentsCount,
				String functionName) {
			if (executionStack.size() < argumentsCount) {
				throw new RuntimeException("Expected at least " + argumentsCount
						+ " arguments for function @" + functionName);
			}
		}

		/**
		 * @throws RuntimeException if operation could not be completed
		 */
		@Override
		public void visit(TokenOperator token) {
			ValueWrapper operand1 = null;
			Object operand2 = null;
			try {
				operand2 = executionStack.pop().getValue();
				operand1 = executionStack.pop();
			} catch (java.util.EmptyStackException e) {
				throw new RuntimeException(
						"Not enough operands for operation: "
								+ token.getSymbol());
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

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor#visit(hr
		 * .fer.zemris.java.custom.scripting.tokens.TokenString)
		 */
		@Override
		public void visit(TokenString token) {
			executionStack.push(new ValueWrapper(token.getValue()));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor#visit(hr
		 * .fer.zemris.java.custom.scripting.tokens.TokenVariable)
		 */
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
