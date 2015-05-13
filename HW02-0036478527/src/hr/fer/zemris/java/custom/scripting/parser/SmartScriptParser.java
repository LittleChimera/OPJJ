package hr.fer.zemris.java.custom.scripting.parser;

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

/**
 * Smart parser for custom language described in 2nd Homework of class OPJJ.
 * 
 * @author Luka Skugor
 *
 */
public class SmartScriptParser {

	/**
	 * Document's body unparsed
	 */
	private String documentBody;
	/**
	 * Current position in document's body while parsing
	 */
	private int position;
	/**
	 * Root of document's tree model
	 */
	private DocumentNode documentNode;

	/**
	 * Creates a parser with text it needs to parse.
	 * 
	 * @param documentBody
	 *            text of the document
	 */
	public SmartScriptParser(String documentBody) {
		if (documentBody == null) {
			documentBody = new String();
		}
		this.documentBody = documentBody;
		documentNode = null;
	}

	/**
	 * Parses the document and builds document's tree model.
	 * 
	 * @return document node of built tree model
	 */
	public DocumentNode parse() {
		DocumentNode start = new DocumentNode();
		position = 0;

		ObjectStack stack = new ObjectStack();
		stack.push(start);

		while (peekNext() != null) {

			Node childNode = null;
			Node lastNode = null;

			try {
				lastNode = (Node) stack.peek();
			} catch (EmptyStackException stackEmptied) {
				throw new SmartScriptParserException("Unexcpected END tag.");
			}

			if (isNextText()) {
				String parsedText = parseText();
				childNode = new TextNode(parsedText);

			} else if (isNextTagOpening()) {
				String tagName = parseTagName();

				if (tagName.equalsIgnoreCase("END")) {
					parseEndTag();
					stack.pop();
					continue;

				} else if (tagName.trim().equalsIgnoreCase("FOR")) {
					childNode = parseForTag();
					stack.push(childNode);

				} else if (tagName.equalsIgnoreCase("=")) {
					childNode = parseEchoTag();

				} else {
					throw new SmartScriptParserException(
							"Missing tag parser function.");
				}
			} else {
				throw new SmartScriptParserException("Unknown error");
			}

			lastNode.addChildNode(childNode);

		}

		if (stack.size() != 1) {
			throw new SmartScriptParserException(
					"Some blocks aren't proprely opened or closed");
		}

		return (DocumentNode) stack.peek();

	}

	/**
	 * Tests if next characters are Text inside of Document Body
	 * 
	 * @return true if characters are Text, else false
	 */
	private boolean isNextText() {
		String nextCharacter = peekNext();
		if (nextCharacter == null) {
			return false;
		}

		if (nextCharacter.charAt(0) == '{') {
			if (isNextTagOpening()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Tests if next characters are opening of a tag.
	 * 
	 * @return true if tag is opened, else false
	 */
	private boolean isNextTagOpening() {
		String nextCharacters = peekNext(2);
		return nextCharacters.equals("{$");
	}

	/**
	 * Tests if next characters have number format.
	 * 
	 * @return true if next characters have number format, else false
	 */
	private boolean isNextANumber() {
		String nextCharacters = peekNext(2);

		try {
			if (Character.isDigit(nextCharacters.charAt(0))
					|| ((nextCharacters.charAt(0) == '+' || nextCharacters
							.charAt(0) == '-') && Character
							.isDigit(nextCharacters.charAt(1)))) {
				return true;
			} else {
				return false;
			}
		} catch (IndexOutOfBoundsException endOfFile) {
			return false;
		}
	}

	/**
	 * Array of all known tags which can be parsed. Each one has its own parsing
	 * method
	 * 
	 * "=" - Echo tag
	 * 
	 * "FOR " - ForLoop tag (This tag requires space at the end. Only then tag's
	 * tokens can be parsed.)
	 * 
	 * "END" - Closing of ForLoop tag
	 */
	private static String[] knownTags = { "=", "FOR ", "END" };

	/**
	 * Parses next characters as tag opening and tag name.
	 * 
	 * This method should be called only when tag is opened.
	 * 
	 * @return name of the tag
	 * @throws SmartScriptParserException
	 *             if tag is unknown
	 */
	private String parseTagName() {
		StringBuilder tagBuilder = new StringBuilder();

		parseTagOpening();
		skipSpaces();

		// initialize comparing tags
		boolean[] matches = new boolean[knownTags.length];
		for (int i = knownTags.length - 1; i >= 0; i--) {
			matches[i] = true;
		}

		int tagLength = 0;
		int validTags = knownTags.length;

		// read tag name
		String nextChar = peekNext();
		while (nextChar != null && !nextChar.equals("$") && validTags > 0) {

			tagBuilder.append(nextChar);
			takeNextDefault();
			nextChar = peekNext();
			tagLength++;

			for (int index = 0; index < knownTags.length; index++) {
				if (matches[index] == false) {
					continue;
				}
				try {
					if (!knownTags[index].substring(0, tagLength)
							.equalsIgnoreCase(tagBuilder.toString())) {
						validTags--;
						matches[index] = false;

					} else if (knownTags[index].length() == tagLength) {
						skipSpaces();
						return tagBuilder.toString();
					}

				} catch (IndexOutOfBoundsException readTagIsLonger) {
					validTags--;
					matches[index] = false;
				}
			}

		}

		throw new SmartScriptParserException("Invalid tag name: "
				+ tagBuilder.toString());
	}

	/**
	 * Reads next characters form document body with escaping. If next character
	 * is an escaping character ('\') method will try to escape next character.
	 * If next character is not escapable method returns both of them.
	 * 
	 * @param escapableCharacters
	 *            array of characters which can be escaped
	 * @return next characters
	 */
	private String takeNext(char[] escapableCharacters) {
		char nextChar;

		try {
			nextChar = documentBody.charAt(position);
			position++;

			if (nextChar == '\\') {

				try {
					char escapedChar = documentBody.charAt(position);
					position++;

					for (char escapee : escapableCharacters) {
						if (escapee == escapedChar) {
							return Character.toString(escapedChar);
						}
					}

					return Character.toString(nextChar)
							+ Character.toString(escapedChar);

				} catch (IndexOutOfBoundsException endOfDocument) {
					// ignorable - nextChar will be returned
				}

			}
			return Character.toString(nextChar);

		} catch (IndexOutOfBoundsException endOfDocument) {
			return null;
		}
	}

	/**
	 * Reads next characters using escaping inside of a string.
	 * 
	 * @return next characters
	 */
	private String takeNextFromString() {
		char[] escapableCharacters = { '\\', '"' };
		return takeNext(escapableCharacters);
	}

	/**
	 * Reads next characters using escaping inside of the document body.
	 * 
	 * @return next characters
	 */
	private String takeNextFromBody() {
		char[] escapableCharacters = { '\\', '{' };
		return takeNext(escapableCharacters);
	}

	/**
	 * Reads next character with no escaping.
	 * 
	 * @return next characters
	 */
	private String takeNextDefault() {
		char[] escapableCharacters = {};
		return takeNext(escapableCharacters);
	}

	/**
	 * Peeks at next character without incrementing position in document.
	 * 
	 * @return next character or null if end of document is reached
	 */
	private String peekNext() {
		try {
			return Character.toString(documentBody.charAt(position));
		} catch (IndexOutOfBoundsException endOfDocument) {
			return null;
		}
	}

	/**
	 * Peeks at next characters without incrementing position in document.
	 * 
	 * @param length
	 *            number of next characters to read
	 * @return String of next characters with requested length or maximum length
	 *         possible.
	 */
	private String peekNext(int length) {
		try {
			return documentBody.substring(position, position + length);
		} catch (IndexOutOfBoundsException endOfFile) {
			return documentBody.substring(position, documentBody.length());
		}
	}

	/**
	 * Parses next characters as text from document body as long as next
	 * character is text.
	 * 
	 * @return parsed text
	 */
	private String parseText() {
		StringBuilder textBuilder = new StringBuilder();

		while (isNextText()) {
			textBuilder.append(takeNextFromBody());
		}

		return textBuilder.toString();
	}

	/**
	 * Parses next characters as closing of a tag. This method should only be
	 * called when tag is closing.
	 * 
	 * @throws SmartScriptParserException
	 *             if closing tag is not found
	 */
	private void parseTagClosing() {
		String closingFirst = takeNextDefault();
		String closingSecond = takeNextDefault();

		if (closingFirst == null || closingSecond == null
				|| !closingFirst.equals("$") || !closingSecond.equals("}")) {
			throw new SmartScriptParserException("Expexted closing tag");
		}
	}

	/**
	 * Parses next characters as opening of a tag. This method should only be
	 * called when tag is opening.
	 * 
	 * @throws SmartScriptParserException
	 *             if opening tag is not found
	 */
	private void parseTagOpening() {
		String openingFirst = takeNextDefault();
		String openingSecond = takeNextDefault();

		if (openingFirst == null || openingSecond == null
				|| !openingFirst.equals("{") || !openingSecond.equals("$")) {
			throw new SmartScriptParserException("Tag not found.");
		}
	}

	/**
	 * Parses next characters as END Tag. This method should only be called when
	 * opening of END Tag is read.
	 */
	private void parseEndTag() {
		parseTagClosing();
	}

	/**
	 * Parses next characters as FOR Tag. This method should only be called when
	 * opening of END Tag is read.
	 * 
	 * @return node of parsed ForLoop
	 * @throws SmartScriptParserException
	 *             if tag's tokens are invalid.
	 */
	private Node parseForTag() {
		Token[] forTokens = { null, null, null, null };
		for (int i = 0; i < 4; i++) {
			try {
				forTokens[i] = readToken();
			} catch (SmartScriptParserException ignorable) {
			}
		}

		// check for errors
		for (int i = 0; i < forTokens.length - 1; i++) {
			if (forTokens[i] == null) {
				throw new SmartScriptParserException(
						"Tokens inside of FOR tag are invalid");
			}
		}

		for (int i = 0; i < forTokens.length; i++) {
			if (forTokens[i] instanceof TokenFunction) {
				throw new SmartScriptParserException(
						"Function is not allowed inside of FOR tag");
			}
		}

		if (!(forTokens[0] instanceof TokenVariable)) {
			throw new SmartScriptParserException(
					"First token of For Loop must me a variable");
		}

		ForLoopNode forLoopNode = new ForLoopNode((TokenVariable) forTokens[0],
				forTokens[1], forTokens[2], forTokens[3]);

		parseTagClosing();

		return forLoopNode;
	}

	/**
	 * Parses next characters as ECHO Tag. This method should only be called
	 * when opening of ECHO Tag is read.
	 * 
	 * @return node of parsed Echo
	 * @throws SmartScriptParserException
	 *             if tag isn't properly closed
	 */
	private Node parseEchoTag() {
		Token currentToken;
		ObjectStack stack = new ObjectStack();

		while (true) {

			try {
				currentToken = readToken();
			} catch (SmartScriptParserException noMoreTokens) {
				skipSpaces();
				break;
			}
			stack.push(currentToken);

			String nextChar = peekNext();
			if (nextChar != null && nextChar.equals("$")) {
				break;
			}
		}
		parseTagClosing();

		EchoNode echoNode = new EchoNode(stack.size());
		for (int counter = stack.size() - 1; counter >= 0; counter--) {
			echoNode.insert(counter, (Token) stack.pop());
		}

		return echoNode;
	}

	/**
	 * Reads next characters as a token. This method should only be called when
	 * parser is positioned inside of a tag. Method automatically detects which
	 * type of token should be read.
	 * 
	 * @return read token
	 */
	private Token readToken() {
		char tokenFirstChar;
		String peekedChar = peekNext();

		if (peekedChar != null) {
			tokenFirstChar = peekedChar.charAt(0);
		} else {
			throw new SmartScriptParserException("Another token expected");
		}

		Token readToken;
		if (Character.isLetter(tokenFirstChar)) {
			readToken = parseTokenVariable();

		} else if (isNextANumber()) {
			readToken = parseTokenNumber();

		} else if (tokenFirstChar == '@') {
			readToken = parseTokenFunction();

		} else if (tokenFirstChar == '"') {
			readToken = parseTokenString();

		} else if (tokenFirstChar == '*' || tokenFirstChar == '+'
				|| tokenFirstChar == '-' || tokenFirstChar == '/'
				|| tokenFirstChar == '%') {
			readToken = parseTokenOperator();

		} else {
			throw new SmartScriptParserException("Unkown token.");
		}

		skipSpaces();
		return readToken;
	}

	/**
	 * Parses next characters as a Token Variable.
	 * 
	 * This method should NOT be called on its own. Delegate the job of reading
	 * a token to the readToken().
	 * 
	 * @return read Token Variable
	 */
	private Token parseTokenVariable() {
		String peekedChar = peekNext();
		// doesn't check starting conditions!

		StringBuilder variableName = new StringBuilder();
		variableName.append(takeNextDefault());

		peekedChar = peekNext();

		while (peekedChar != null && !isSpace(peekedChar.charAt(0))) {
			char currentChar = peekedChar.charAt(0);
			if (Character.isAlphabetic(currentChar) || currentChar == '_'
					|| Character.isDigit(currentChar)) {
				variableName.append(takeNextDefault());
				peekedChar = peekNext();
			} else {
				break;
			}
		}

		return new TokenVariable(variableName.toString());
	}

	/**
	 * Parses next characters as a Token Function.
	 * 
	 * This method should NOT be called on its own. Delegate the job of reading
	 * a token to the readToken().
	 * 
	 * @return read Token Function
	 */
	private Token parseTokenFunction() {
		String peekedChar = peekNext();
		// doesn't check starting conditions!

		StringBuilder functionName = new StringBuilder();
		functionName.append(takeNextDefault());

		peekedChar = peekNext();
		if (peekedChar == null || !Character.isAlphabetic(peekedChar.charAt(0))) {
			throw new SmartScriptParserException(
					"Function name must be @ followed by a letter");
		}

		while (peekedChar != null && !isSpace(peekedChar.charAt(0))) {
			char currentChar = peekedChar.charAt(0);
			if (Character.isAlphabetic(currentChar) || currentChar == '_'
					|| Character.isDigit(currentChar)) {
				functionName.append(takeNextDefault());
				peekedChar = peekNext();
			} else {
				break;
			}
		}

		return new TokenFunction(functionName.toString());
	}

	/**
	 * Parses next characters in document as a Number Token. If decimal point is
	 * found treats a number as a double, else as an integer.
	 * 
	 * This method should NOT be called on its own. Delegate the job of reading
	 * a token to the readToken().
	 * 
	 * @return parsed Double Token for double or parsed Integer Token for
	 *         integer
	 */
	private Token parseTokenNumber() {
		String peekedChar = peekNext();
		// doesn't check starting conditions!

		StringBuilder numberBuilder = new StringBuilder();
		numberBuilder.append(takeNextDefault());

		peekedChar = peekNext();

		boolean decimalPoint = false;
		while (peekedChar != null && !isSpace(peekedChar.charAt(0))) {
			char currentChar = peekedChar.charAt(0);

			if (currentChar == '.' || Character.isDigit(currentChar)) {
				numberBuilder.append(takeNextDefault());
				peekedChar = peekNext();

				if (currentChar == '.') {
					if (decimalPoint == false) {
						decimalPoint = true;
					} else {
						throw new SmartScriptParserException(
								"Invalid number. Multiple decimal spaces read.");
					}
				}
			} else {
				break;
				// throw new
				// SmartScriptParserException("Encountered invalid number.");
			}
		}
		if (decimalPoint) {
			return new TokenConstantDouble(Double.parseDouble(numberBuilder
					.toString()));
		} else {
			return new TokenConstantInteger(Integer.parseInt(numberBuilder
					.toString()));
		}
	}

	/**
	 * Parses next characters in document as a Token String.
	 * 
	 * This method should NOT be called on its own. Delegate the job of reading
	 * a token to the readToken().
	 * 
	 * @return parsed String Token
	 */
	private Token parseTokenString() {
		String peekedChar = peekNext();
		// doesn't check starting conditions!

		StringBuilder variableName = new StringBuilder();
		takeNextDefault();

		peekedChar = peekNext();

		while (peekedChar != null && peekedChar.charAt(0) != '"') {
			variableName.append(takeNextFromString());
			peekedChar = peekNext();
		}

		String closingString = takeNextDefault();
		if (closingString == null || !closingString.equals("\"")) {
			throw new SmartScriptParserException("String not closed");
		}

		return new TokenString(variableName.toString());
	}

	private Token parseTokenOperator() {
		return new TokenOperator(takeNextDefault());
	}

	/**
	 * Tests if the character is a space.
	 * 
	 * Following characters are treated as spaces: ' ', '\t', '\n', '\r'
	 * 
	 * @param test
	 *            character to be tested
	 * @return true if character is treated as space, else false
	 */
	private boolean isSpace(char test) {
		if (test == ' ' || test == '\t' || test == '\r' || test == '\n') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Ignores all following spaces from current position in document. Position
	 * is set to index of next character which is not space.
	 * 
	 * Following characters are treated as spaces: ' ', '\t', '\n', '\r'
	 * 
	 */
	private void skipSpaces() {
		String nextChar = peekNext();
		while (nextChar != null) {
			char next = nextChar.charAt(0);

			if (isSpace(next)) {
				takeNextDefault();
				nextChar = peekNext();
			} else {
				break;
			}
		}
	}

	/**
	 * Gets document node. If document is not yet parsed calls parse function
	 * first.
	 * 
	 * @return document node
	 */
	public DocumentNode getDocumentNode() {
		if (documentNode == null) {
			return parse();
		} else {
			return documentNode;
		}

	}
}
