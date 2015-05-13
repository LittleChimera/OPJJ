package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Exception is throwen in node class when null child is added in collection.
 * @author Borna Feldšar
 * @version 1.0
 */
public class NullChildException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new EmptyStackException with null as its error message string.
	 */
	public NullChildException() {
		super();
	}
	
	/**
	 * Constructs a new EmptyStackException with error message.
	 * @param message error message.
	 */
	public NullChildException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new EmptyStackException with cause of exception.
	 * @param cause cause of exception
	 */
	public NullChildException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new EmptyStackException with error message and cause of exception.
	 * @param message error message
	 * @param cause cause of exception
	 */
	public NullChildException(String message, Throwable cause) {
		super(message, cause);
	}
}
