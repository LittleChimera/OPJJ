package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Exception is throwen in node class when error occures.
 * @author Borna
 *
 */
public class DocumentModelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new EmptyStackException with null as its error message string.
	 */
	public DocumentModelException() {
		super();
	}
	
	/**
	 * Constructs a new EmptyStackException with error message.
	 * @param message error message.
	 */
	public DocumentModelException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new EmptyStackException with cause of exception.
	 * @param cause cause of exception
	 */
	public DocumentModelException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new EmptyStackException with error message and cause of exception.
	 * @param message error message
	 * @param cause cause of exception
	 */
	public DocumentModelException(String message, Throwable cause) {
		super(message, cause);
	}
}
