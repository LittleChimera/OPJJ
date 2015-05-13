package hr.fer.zemris.java.custom.scripting.parser;
/**
 * Exception is throwen in SmartScriptParser when error occures.
 * @author Borna Feldšar
 * @version 1.0
 *
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new EmptyStackException with null as its error message string.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Constructs a new EmptyStackException with error message.
	 * @param message error message.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new EmptyStackException with cause of exception.
	 * @param cause cause of exception
	 */
	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new EmptyStackException with error message and cause of exception.
	 * @param message error message
	 * @param cause cause of exception
	 */
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
