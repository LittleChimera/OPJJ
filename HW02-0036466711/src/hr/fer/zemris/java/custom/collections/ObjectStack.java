package hr.fer.zemris.java.custom.collections;

/**
 * Standard implementation of a stack.
 * 
 * @author Mihovil Vinković
 * @see java.util.Stack
 */
public class ObjectStack {

	ArrayBackedIndexedCollection stack = new ArrayBackedIndexedCollection();

	/**
	 * Checks whether the stack contains any elements.
	 * 
	 * @return Truth value whether the stack is empty.
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Number of elements on the stack.
	 * 
	 * @return Size of the stack.
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Adds an element to the top of the stack.
	 * 
	 * @param value
	 *            Value of the element to be added.
	 */
	public void push(Object value) {
		stack.insert(value, stack.size());
	}

	/**
	 * Removes and returns the element at the top of the stack.
	 * 
	 * @return The object at the top of the stack.
	 * @exception EmptyStackException
	 *                Can't pop an empty stack.
	 */
	public Object pop() {
		if (stack.isEmpty()) {
			throw new EmptyStackException();
		}
		int size = stack.size();
		Object o = stack.get(size - 1);
		stack.remove(size - 1);
		return o;
	}

	/**
	 * Returns, but doesn't remove the element at the top of the stack.
	 * 
	 * @return The object at the top of the stack.
	 * @exception EmptyStackException
	 *                Can't peek in an empty stack.
	 */
	public Object peek() {
		if (stack.isEmpty()) {
			throw new EmptyStackException();
		}
		return stack.get(stack.size() - 1);
	}

	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		stack.clear();
	}
}
