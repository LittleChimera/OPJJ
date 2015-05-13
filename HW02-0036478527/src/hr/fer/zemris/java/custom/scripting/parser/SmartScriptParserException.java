package hr.fer.zemris.java.custom.scripting.parser;

/**
 * This exception is thrown when SmartScriptParser encounters a parsing error.
 * 
 * @author Luka Skugor
 *
 */
public class SmartScriptParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor.
	 */
	public SmartScriptParserException() {
	}

	/**
	 * Creates new exception with a given message.
	 * 
	 * @param message
	 *            message to give to the exception
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

}
