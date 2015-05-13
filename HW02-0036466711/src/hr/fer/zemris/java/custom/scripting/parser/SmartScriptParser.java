package hr.fer.zemris.java.custom.scripting.parser;

import java.util.regex.PatternSyntaxException;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;

/**
 * Parses the desired String according to a certain grammar. For information on
 * the grammar, get into the FER Java course.
 * 
 * @author Mihovil Vinković
 *
 */
public class SmartScriptParser {

	ObjectStack stack;

	/**
	 * Basic constructor.
	 * 
	 * @param docBody
	 *            String to be parsed.
	 * @exception SmartScriptParserException
	 *                Thrown when the document isn't formatted correctly.
	 */
	public SmartScriptParser(String docBody) {
		stack = new ObjectStack();
		this.parse(docBody);
	}

	/**
	 * Converts a string to a token according to it's value.
	 * 
	 * @param value
	 *            String to be evaluated.
	 * @return The tokenization of the String.
	 */
	private static Token tokenize(String value) {
		if (value == null)
			throw new SmartScriptParserException();

		else if (value.substring(0, 1).equals("@")) { /* function */
			String name = value.substring(1);

			if (!isValidVar(name))
				throw new SmartScriptParserException();

			return new TokenFunction(name);

		} else if (value.matches("[\\+\\-\\*/%]")) { /* operator */
			return new TokenOperator(value);

		} else if (value.startsWith("\"") && value.endsWith("\"")) { /* string */

			/*
			 * removes double backslashes, quotes, new lines, carriage returns
			 * and tabs
			 */
			value = value.replace("\\\\", "\\").replace("\\\"", "\"")
					.replace("\\n", "\n").replace("\\r", "\r")
					.replace("\\t", "\t");

			return new TokenString(value);

		} else if (isValidVar(value)) { /* variable */
			return new TokenVariable(value);
		}

		try {
			Integer iValue = Integer.parseInt(value); /* integer */
			return new TokenConstantInteger(iValue);
		} catch (NumberFormatException e) {
		}

		try {
			Double dValue = Double.parseDouble(value); /* double */
			return new TokenConstantDouble(dValue);
		} catch (NumberFormatException e) {
		}

		throw new SmartScriptParserException();
	}

	/**
	 * Checks if the string is in the valid variable form, i.e. starts with
	 * alphabetic character and doesnt contains anything except alphanumeric
	 * characters or an underslash.
	 * 
	 * @param str
	 *            String to be evaluated.
	 * @return Truth value whether the string conforms to the variable naming
	 *         rules.
	 */
	private static boolean isValidVar(String str) {
		// [a-zA-Z] --> starts with alphabetic character
		// [a-zA-Z_0-9]* --> 0 or more alphanumeric/underslash characters.
		return str.matches("[a-zA-Z][a-zA-Z_0-9]*");
	}

	/**
	 * Splits a string by spaces except when the spaces are within quotes.
	 * 
	 * @param str
	 *            String to be evaluated.
	 * @return String array split by spaces (except when inside quotes).
	 */
	private static String[] spaceSplitExceptQuotes(String str) {
		// [ ]+ --> spaces
		// ? --> if
		// [^\"]* --> zero or more non-quotes
		// \"[^\"]*\" --> quote followed by non-quotes and ending in a quote
		// * --> all of that zero or more times
		// [^\"]*$ --> non-quotes and end of line
		return str.split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	}

	/**
	 * Parses tags.
	 * 
	 * @param Tag
	 *            candidate to be parsed.
	 * @return Whether the tag was parsed.
	 * @exception SmartScriptParserException
	 *                Thrown when the document isn't formatted correctly.
	 */
	private boolean parseTag(String inString) {

		if (inString.length() < 2) {
			return false;
		}
		String firstSymbol = inString.substring(0, 1), lastSymbol = inString
				.substring(inString.length() - 1);

		/* It's a tag! */
		if (firstSymbol.equals("$") && lastSymbol.equals("$")) {

			String noTags = inString.substring(1, inString.length() - 1).trim();

			if (noTags.equals("")) {
				throw new SmartScriptParserException();
			}
			/* It's an empty tag */
			else if (noTags.startsWith("=")
					&& !noTags.substring(1).trim().equals("")) {

				noTags = noTags.trim().substring(1).trim(); /* remove "=" */
				String[] words = spaceSplitExceptQuotes(noTags);
				Token[] tokens = new Token[words.length];

				for (int i = 0; i < words.length; i++) {
					tokens[i] = tokenize(words[i]);
				}

				Node newNode = new EchoNode(tokens);
				Node topNode = (Node) stack.pop();
				topNode.addChildNode(newNode);
				stack.push(topNode);

			}
			/* It's a for loop tag */
			else if (noTags.toLowerCase().startsWith("for")) {

				noTags = noTags.substring(3).trim();

				String[] words;
				try {
					words = spaceSplitExceptQuotes(noTags);
				} catch (PatternSyntaxException e) {
					throw new SmartScriptParserException();
				}

				if (words.length != 3 && words.length != 4) {
					throw new SmartScriptParserException();
				} else {
					Node topNode = (Node) stack.pop();
					ForLoopNode newNode;

					if (!isValidVar(words[0])) {
						throw new SmartScriptParserException();
					}

					TokenVariable var = new TokenVariable(words[0]);
					
					Token startExp = tokenize(words[1]);
					if (!(startExp instanceof TokenVariable
							|| startExp instanceof TokenConstantDouble
							|| startExp instanceof TokenConstantInteger || startExp instanceof TokenString)) {
						throw new SmartScriptParserException();
					}
					Token endExp = tokenize(words[2]);

					if (words.length == 3) {
						newNode = new ForLoopNode(var, startExp, endExp);
					} else {
						Token stepExp = tokenize(words[3]);
						newNode = new ForLoopNode(var, startExp, endExp,
								stepExp);
					}

					topNode.addChildNode(newNode);
					stack.push(topNode);
					stack.push(newNode);
				}
			} else if (noTags.toLowerCase().equals("end")) {
				Object stackTop = stack.pop();

				if (!(stackTop instanceof ForLoopNode)) {
					throw new SmartScriptParserException();
				}
			} else {
				throw new SmartScriptParserException();
			}

			return true;

		}
		/* tag that starts but doesn't end */
		else if (firstSymbol.equals("$") || lastSymbol.equals("$")) {
			throw new SmartScriptParserException();
		} else {
			return false;
		}
	}

	/**
	 * Main parsing function. Checks either for tags or for ordinary text.
	 * 
	 * @param docBody
	 *            String to be parsed.
	 * @exception SmartScriptParserException
	 *                Thrown when the document isn't formatted correctly.
	 */
	private void parse(String docBody) {
		boolean parsed;

		stack.push(new DocumentNode());

		int findFrom = 0;

		/*
		 * Adds a backslash to every closing curly brace that was opened with a
		 * backslash opening curly brace. Preparation for splitting.
		 */
		while (docBody.indexOf("\\{", findFrom) != -1) {
			findFrom = docBody.indexOf("\\{", findFrom);
			findFrom = docBody.indexOf("}", findFrom);
			docBody = docBody.substring(0, findFrom - 1) + "\\"
					+ docBody.substring(findFrom);
		}
		/*
		 * Split by curly braces unless preceeded by backslash. ?<![\\\\]) -->
		 * there must not be a backslash before this [{] --> The curly brace |
		 * --> or then the closing curly brace.
		 */
		String[] strings = docBody.trim()
				.split("(?<![\\\\])[{]|(?<![\\\\])[}]");

		for (String currentStr : strings) {
			parsed = parseTag(currentStr);
			if (!parsed) {
				Node newNode = new TextNode(currentStr);
				Node topNode = (Node) stack.pop();
				topNode.addChildNode(newNode);
				stack.push(topNode);
			}
		}

		Object top = stack.pop();
		if (!stack.isEmpty()) {
			throw new SmartScriptParserException();
		}
		stack.push(top);
	}

	/**
	 * @return The whole document node, and as it's children, the other nodes.
	 */
	public DocumentNode getDocumentNode() {
		return (DocumentNode) stack.peek();
	}
}
