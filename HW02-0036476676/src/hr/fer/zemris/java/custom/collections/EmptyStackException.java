package hr.fer.zemris.java.custom.collections;
/**
 * Thrown by methods in the ObjectStack class to indicate that the stack is empty.
 * @author Borna Feldšar
 *
 */
public class EmptyStackException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new EmptyStackException with null as its error message string.
	 */
	public EmptyStackException() {
		super();
	}
	
	/**
	 * Constructs a new EmptyStackException with error message.
	 * @param message error message.
	 */
	public EmptyStackException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new EmptyStackException with cause of exception.
	 * @param cause cause of exception
	 */
	public EmptyStackException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new EmptyStackException with error message and cause of exception.
	 * @param message error message
	 * @param cause cause of exception
	 */
	public EmptyStackException(String message, Throwable cause) {
		super(message, cause);
	}
}
