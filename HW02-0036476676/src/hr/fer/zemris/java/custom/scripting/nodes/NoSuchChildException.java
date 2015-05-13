package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Exception is throwen in node class when index of child is smaller then 0 or bigger than size-1.
 * @author Borna
 *
 */
public class NoSuchChildException extends DocumentModelException{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new EmptyStackException with null as its error message string.
	 */
	public NoSuchChildException() {
		super();
	}
	
	/**
	 * Constructs a new EmptyStackException with error message.
	 * @param message error message.
	 */
	public NoSuchChildException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new EmptyStackException with cause of exception.
	 * @param cause cause of exception
	 */
	public NoSuchChildException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new EmptyStackException with error message and cause of exception.
	 * @param message error message
	 * @param cause cause of exception
	 */
	public NoSuchChildException(String message, Throwable cause) {
		super(message, cause);
	}
}
