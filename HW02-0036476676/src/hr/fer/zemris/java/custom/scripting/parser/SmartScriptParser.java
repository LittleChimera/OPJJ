package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Parser for documents with {$ ... $} tags.
 * Supported tags are For loop tag and empty tag.
 * For loop tag must have a variable and 2-3 numbers representing
 * start, end, and step (which is optional) of an expression.
 * 
 * Empty tag consists of various numbers, variables, operators, functions or
 * strings . 
 * Plain text outside the text representing TextNode.
 * Parser can return document as Node tree where root node is document.
 * @author Borna Feldšar
 * @version 2.0
 */
import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

public class SmartScriptParser {
	private String functionRegex = "@([A-Z]|[a-z])([A-z]|[0-9]|_)*";
	private String variableRegex = "([A-Z]|[a-z])([A-z]|[0-9]|_)*";
	private String operatorRegex = "\\*|\\/|\\+|\\-|\\%";
	private String doubleRegex = "-?\\d+\\.\\d+";
	private String integerRegex = "-?\\d+";
	private String documentBody;
	private DocumentNode document;

	/**
	 * Constructor which has document with {$ .. $} tags as parameter. Parses
	 * parses given document and arranges it in tree structure
	 * 
	 * @param documentBody
	 *            document with tags
	 * @throw SmartScriptParserException if error occures.
	 */
	public SmartScriptParser(String documentBody) {
		this.documentBody = documentBody;

		try {
			parse();
		} catch (IllegalArgumentException e) {
			throw new SmartScriptParserException();
		} catch (NullPointerException e1) {
			throw new SmartScriptParserException();
		} catch (EmptyStackException e2) {
			throw new SmartScriptParserException();
		}

	}

	/**
	 * Return documetn node with tree structure.
	 * 
	 * @return
	 */
	public DocumentNode getDocumentNode() {
		return document;

	}

	/**
	 * Creates document tree by parsing documentBody into tag nodes and text
	 * nodes.
	 */
	private void parse() {
		ObjectStack stack = new ObjectStack();
		document = new DocumentNode();
		stack.push(document);
		Node parentNode;
		String string;
		ArrayBackedIndexedCollection collection = parseIntoCollection(documentBody);

		for (int j = 0; j < collection.size(); j++) {
			parentNode = (Node) stack.peek();
			string = ((String) collection.get(j));

			if (string.startsWith("{$")) {
				parseInsideTag(string, stack);

			} else {
				TextNode text = new TextNode(string);
				parentNode.addChildNode(text);
			}
		}
		if (stack.size() > 1) {
			throw new IllegalArgumentException("Not enough {$END$} tags");
		}
	}

	/**
	 * Parses inside tag and creates tree specifyng for loop tags, empty tags
	 * and end tags.
	 * 
	 * @param element
	 *            string element inside tag
	 * @param stack
	 */
	private void parseInsideTag(String element, ObjectStack stack) {
		
		
		ArrayBackedIndexedCollection tokens = new ArrayBackedIndexedCollection();
		element = element.substring(2, element.length() - 2); // string without {$ $}
		
		element = tagParse(element, tokens);
		spaceParse(element, tokens);

		int size = tokens.size();
		String tag = ((String) tokens.get(0)); // on first place in collection is tag name
		Node parent = (Node) stack.peek();

		if (tag.equalsIgnoreCase("FOR")) {
			if (tokens.size() < 4 || tokens.size() > 5) {
				throw new IllegalArgumentException(
						"Invalid for loop arguments number");
			}
			ForLoopNode node;
			int operatorCounter = 0;
			Token token;
			if (!(tokens.get(size - 1) instanceof TokenVariable)) {
				throw new IllegalArgumentException(
						"First argument in for loop must be variable!");
			}
			for (int i = 1; i < size; i++) {
				token = (Token) tokens.get(i);
				if (token instanceof TokenFunction) {
					throw new IllegalArgumentException(
							"Function can't be in for loop.");

				} else if (token instanceof TokenOperator) {
					operatorCounter++;
				}
			}
			if (operatorCounter > 0) {
				throw new IllegalArgumentException(
						"Operator can't be in for loop");
			}
			if (tokens.size() == 5) {
				node = new ForLoopNode((TokenVariable) tokens.get(size - 1),
						(Token) tokens.get(size - 2),
						(Token) tokens.get(size - 3),
						(Token) tokens.get(size - 4));
			} else {
				node = new ForLoopNode((TokenVariable) tokens.get(size - 1),
						(Token) tokens.get(size - 2),
						(Token) tokens.get(size - 3));
			}
			parent.addChildNode(node);
			stack.push(node);
		} else if (tag.equalsIgnoreCase("=")) {
			Token[] token = new Token[size - 1];
			for (int i = size - 1, j = 0; i > 0; i--, j++) { // tokens are placed in reverse
				token[j] = (Token) tokens.get(i);
			}
			parent.addChildNode(new EchoNode(token));
		} else if (tag.equalsIgnoreCase("END")) {
			stack.pop();
		}
	}
	
	/**
	 * Determinates which tag name is it.
	 * @param element string with value inside tag
	 * @param tokens collection of tokens where tag name is added
	 * @return string value of tag without tag name
	 */
	private String tagParse(String element, ArrayBackedIndexedCollection tokens) {
		String tag;
		if (element.matches("\\s+.*")) {
			String[] str = element.split("\\s+", 2);
			element = str[1];
		}
		tag = element;
		if (tag.toLowerCase().startsWith("for")) {
			element = element.substring(3);
			tag = "FOR";
		} else if (tag.toLowerCase().startsWith("=")) {

			element = element.substring(1);
			tag = "=";
		} else if (tag.trim().equalsIgnoreCase("end")) {
			element = "";
			tag = "END";
		}
		if (!element.matches("\\s+.*") && !element.isEmpty()
				&& !tag.equals("=")) {
			throw new IllegalArgumentException("Invalid tag names");

		}
		tokens.add(tag);
		return element;

	}
	
	/**
	 * Determinates variable name while string matches variableRegex.
	 * @param element string inside tag
	 * @param tokens collection of tokens where variable name is added
	 */
	private void variableParse(String element,
			ArrayBackedIndexedCollection tokens) {

		String variable = "";

		while (!element.equals("")) {
			if (String.valueOf(element.charAt(0)).matches("\\s")) {
				spaceParse(element, tokens);
				break;
			} else if (element.charAt(0) == '@') {
				functionParse(element, tokens);
				break;
			} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
				operatorParse(element, tokens);
				break;
			} else if (element.charAt(0) == '"') {
				stringParse(element, tokens);
				break;
			}
			variable += String.valueOf(element.charAt(0));
			element = element.substring(1);

			if (!variable.matches(variableRegex)) {
				throw new IllegalArgumentException("Invalid variable name!");
			}
		}

		tokens.add(new TokenVariable(variable));
		return;
	}
	
	/**
	 * Determinates function name while function matches functionRegex.
	 * @param element string inside tag
	 * @param tokens collection where function token is added
	 */
	private void functionParse(String element,
			ArrayBackedIndexedCollection tokens) {
		String function = "";
		function = element.substring(0, 1);
		element = element.substring(1);
		if (!element.isEmpty()) {
			while (!element.equals("")) {

				if (String.valueOf(element.charAt(0)).matches("\\s")) {
					spaceParse(element, tokens);
					break;
				} else if (element.charAt(0) == '@') {
					functionParse(element, tokens);
					break;
				} else if (String.valueOf(element.charAt(0)).matches(
						operatorRegex)) {
					operatorParse(element, tokens);
					break;
				} else if (element.charAt(0) == '"') {
					stringParse(element, tokens);
					break;
				}

				function += String.valueOf(element.charAt(0));
				element = element.substring(1);

				if (!function.matches(functionRegex)) {
					throw new IllegalArgumentException("Invalid function name!");
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid function name!");
		}
		tokens.add(new TokenFunction(function));
		return;

	}
	
	/**
	 * Determinates operator name.
	 * @param element string inside tag
	 * @param tokens collection where operator token is added.
	 */
	private void operatorParse(String element,
			ArrayBackedIndexedCollection tokens) {
		String operator = element.substring(0, 1);
		element = element.substring(1);
		if (!element.isEmpty()) {
			if (!operator.matches(operatorRegex)) {
				throw new IllegalArgumentException("Invalid function name!");
			}

			if (String.valueOf(element.charAt(0)).matches("\\s")) {
				spaceParse(element, tokens);

			} else if (element.charAt(0) == '@') {
				functionParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
				operatorParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(variableRegex)) {
				variableParse(element, tokens);

			} else if (element.matches("(" + doubleRegex + ").*")) {
				doubleParse(element, tokens);

			} else if (element.matches("(" + integerRegex + ").*")) {
				integerParse(element, tokens);

			} else if (element.charAt(0) == '"') {
				stringParse(element, tokens);
			} else {
				throw new IllegalArgumentException("Invalid character!");
			}
		}

		tokens.add(new TokenOperator(operator));
		return;
	}
	
	/**
	 * First function which determinate what is next token.
	 * @param element string inside tag
	 * @param tokens collection of tokens
	 */
	private void spaceParse(String element, ArrayBackedIndexedCollection tokens) {

		if (element.trim().isEmpty()) {
			return;
		}
		if (element.matches("\\s+.*")) {
			String[] str = element.split("\\s+", 2);
			element = str[1];
		}

		if (element.matches("(" + variableRegex + ").*")) {
			variableParse(element, tokens);
		} else if (element.matches(doubleRegex + ".*")) {
			doubleParse(element, tokens);

		} else if (element.matches(integerRegex + ".*")) {
			integerParse(element, tokens);

		} else if (String.valueOf(element.charAt(0)).matches("\\s")) {
			spaceParse(element, tokens);

		} else if (element.charAt(0) == '@') {
			functionParse(element, tokens);

		} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
			operatorParse(element, tokens);

		} else if (element.charAt(0) == '"') {
			stringParse(element, tokens);
		} else {
			throw new IllegalArgumentException("Invalid character! "
					+ element.charAt(0));
		}

	}
	
	/**
	 * Determinates value inside string. Qoute character can be escaped \"
	 * @param element string inside tag
	 * @param tokens collection of tokens where token string is added
	 */
	private void stringParse(String element, ArrayBackedIndexedCollection tokens) {

		element = element.replace("\\\\", "\\");
		int indexSecQoute = element.indexOf("\"", 1);
		try {
			while (indexSecQoute > 0
					&& element.charAt(indexSecQoute - 1) == '\\') {
				indexSecQoute = element.indexOf("\"", indexSecQoute + 1);
			}
		} catch (IndexOutOfBoundsException e) {

		}
		String string;
		if (indexSecQoute > 0) {
			string = element.substring(1, indexSecQoute);
			element = element.substring(indexSecQoute + 1); // continues from second qoutes
		} else {
			throw new IllegalArgumentException("Invalid number od qoutes");
		}
		if (!element.isEmpty()) {
			if (element.matches("(" + variableRegex + ").*")) {
				variableParse(element, tokens);
			} else if (element.matches(doubleRegex + ".*")) {
				doubleParse(element, tokens);

			} else if (element.matches(integerRegex + ".*")) {
				integerParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches("\\s")) {
				spaceParse(element, tokens);

			} else if (element.charAt(0) == '@') {
				functionParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
				operatorParse(element, tokens);
			} else {
				throw new IllegalArgumentException("Invalid character! "
						+ element.charAt(0));
			}
		}
		tokens.add(new TokenString(string));
		return;

	}
	
	/**
	 * Determinate dobule constant number.
	 * @param element string inside tag.
	 * @param tokens collection where double token is added
	 */
	private void doubleParse(String element, ArrayBackedIndexedCollection tokens) {
		String[] str;
		str = element.split(doubleRegex, 2);
		int numberLength = element.length() - str[1].length();
		double number;
		try {
			number = Double.parseDouble(element.substring(0, numberLength));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid double number");
		}
		element = element.substring(numberLength);

		if (!element.isEmpty()) {
			if (String.valueOf(element.charAt(0)).matches("\\s")) {
				spaceParse(element, tokens);

			} else if (element.charAt(0) == '@') {
				functionParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
				operatorParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(variableRegex)) {
				variableParse(element, tokens);
			} else if (element.charAt(0) == '"') {
				stringParse(element, tokens);
			} else {
				throw new IllegalArgumentException("Invalid character!"
						+ element.charAt(0));
			}
		}
		tokens.add(new TokenConstantDouble(number));
		return;
	}
	
	/**
	 * Determinate integer constant number.
	 * @param element string inside tag.
	 * @param tokens collection where integer token is added
	 */
	private void integerParse(String element,
			ArrayBackedIndexedCollection tokens) {

		String[] str = element.split(integerRegex, 2);
		int numberLength = element.length() - str[1].length();
		int number = 0;
		try {
			number = Integer.parseInt(element.substring(0, numberLength));
		} catch (NumberFormatException e) {
			doubleParse(element, tokens);
			return;
		}
		element = element.substring(numberLength);

		if (!element.isEmpty()) {
			if (String.valueOf(element.charAt(0)).matches("\\s")) {
				spaceParse(element, tokens);

			} else if (element.charAt(0) == '@') {
				functionParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(operatorRegex)) {
				operatorParse(element, tokens);

			} else if (String.valueOf(element.charAt(0)).matches(variableRegex)) {
				variableParse(element, tokens);
			} else if (element.charAt(0) == '"') {
				stringParse(element, tokens);
			} else {
				throw new IllegalArgumentException("Invalid character!");
			}
		}
		tokens.add(new TokenConstantInteger(number));
		return;

	}
	
	/**
	 * Returns given document body in collection seperated by text nodes and tags.
	 * @param docBody document body
	 * @return collection of text nodes and tags
	 */
	private ArrayBackedIndexedCollection parseIntoCollection(String docBody) {
		ArrayBackedIndexedCollection collection = new ArrayBackedIndexedCollection();
		int indexFirst$ = 0;
		int indexSec$ = 0;
		int indexSlash = 0;
		String part1 = "";
		String part2 = "";

		while (!docBody.isEmpty()) {
			indexFirst$ = docBody.indexOf("{$");
			indexSec$ = docBody.indexOf("$}");
			indexSlash = docBody.indexOf('\\');
			while (indexSlash >= 0
					&& indexSlash < indexFirst$
					&& ((docBody.charAt(indexSlash + 1) == '{') || docBody
							.charAt(indexSlash + 1) == '\\')) {
				indexFirst$ = docBody.indexOf("{$", indexSlash + 2);
				indexSlash = docBody.indexOf('\\', indexSlash + 2);
			}

			indexSec$ = docBody.indexOf("$}", indexFirst$ + 2);
			if (indexFirst$ > 0) {
				part1 = docBody.substring(0, indexFirst$);
				collection.add(part1);
				docBody = docBody.substring(indexFirst$);
				indexFirst$ = 0;
				indexSec$ = docBody.indexOf("$}");
			} else if (indexFirst$ < 0) {
				part1 = docBody;
				collection.add(part1);
				docBody = "";
				break;
			}

			if (indexSec$ > 0) {
				part2 = docBody.substring(indexFirst$, indexSec$ + 2);
				collection.add(part2);
				docBody = docBody.substring(indexSec$ + 2);

			} else {
				throw new IllegalArgumentException();
			}
		}
		return collection;
	}
}
