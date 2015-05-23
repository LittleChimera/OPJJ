package hr.fer.zemris.java.custom.collections;

/**
 * Implementation of stack.
 * 
 * @author Luka Skugor
 *
 */
public class ObjectStack {

	/**
	 * array collection on top of which Stack is built
	 */
	private ArrayBackedIndexedCollection stackCollection;

	/**
	 * Initializes empty stack.
	 */
	public ObjectStack() {
		stackCollection = new ArrayBackedIndexedCollection();
	}

	/**
	 * Checks if stack is empty.
	 * 
	 * @return true if empty, else false
	 */
	public boolean isEmpty() {
		return stackCollection.isEmpty();
	}

	/**
	 * Gets number of elements on the stack.
	 * 
	 * @return number of elements on the stack
	 */
	public int size() {
		return stackCollection.size();
	}

	/**
	 * Pushes an element on the top of the stack.
	 * 
	 * @param value
	 *            pushed element
	 */
	public void push(Object value) {
		stackCollection.add(value);
	}

	/**
	 * Pops an element from the top of the stack.
	 * 
	 * @return popped element
	 */
	public Object pop() {
		Object poppedElement = peek();

		int lastIndex = stackCollection.size() - 1;
		stackCollection.remove(lastIndex);

		return poppedElement;
	}

	/**
	 * Gets an element from the top of the stack. Element is not removed from
	 * the stack.
	 * 
	 * @return element on the top of the stack
	 */
	public Object peek() {
		int lastIndex = stackCollection.size() - 1;
		if (lastIndex < 0) {
			throw new EmptyStackException();
		}

		Object peekedElement = stackCollection.get(lastIndex);

		return peekedElement;
	}

	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		stackCollection.clear();
	}

}
