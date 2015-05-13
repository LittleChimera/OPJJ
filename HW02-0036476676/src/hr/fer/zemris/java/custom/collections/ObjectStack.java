package hr.fer.zemris.java.custom.collections;

/**
 * Class that simulates stack. All operations with stack are with complexity
 * 0(1)
 * 
 * @author Borna Feldšar
 * @version 1.0
 */
public class ObjectStack {
	private ArrayBackedIndexedCollection collection;

	/**
	 * Constructs a new ObjectStack with initialCapacity capacity.
	 * 
	 * @param initialCapacity
	 *            capacity of stack
	 */
	public ObjectStack(int initialCapacity) {
		collection = new ArrayBackedIndexedCollection(initialCapacity);
	}

	/**
	 * Constructs a new ObjectStack with default capacity=16.
	 */
	public ObjectStack() {
		collection = new ArrayBackedIndexedCollection();
	}

	/**
	 * Returns true if stack is empty, false otherwise.
	 * 
	 * @return true for empty stack, false otherwise
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * Return number of elements in stack.
	 * 
	 * @return number of objects in stack
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Pushes given value on the stack. Value can't be null.
	 * 
	 * @param value
	 * @throws IllegalArgumentException
	 *             if value is null
	 */
	public void push(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Value on stack can't be null.");
		}
		collection.add(value);
	}

	/**
	 * Removes last value pushed on stack from stack and returns it.
	 * 
	 * @return last object placed on stack.
	 * @throws EmptyStackException
	 *             if stack is empty
	 */
	public Object pop() {
		if (collection.isEmpty()) {
			throw new EmptyStackException();
		}

		int size = collection.size();
		Object obj = collection.get(size - 1);
		collection.remove(size - 1);
		return obj;
	}

	/**
	 * Returns last element placed on stack but does not delete it from stack.
	 * 
	 * @return last object placed on stack.
	 */
	public Object peek() {
		if (collection.isEmpty()) {
			throw new EmptyStackException();
		}
		return collection.get(collection.size() - 1);
	}

	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		collection.clear();
	}
}
