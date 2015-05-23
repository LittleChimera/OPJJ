package hr.fer.zemris.java.custom.collections;

/**
 * This class is an implementation of dynamic array.
 * 
 * @author Luka Skugor
 *
 */
public class ArrayBackedIndexedCollection {

	/**
	 * size of the collection
	 */
	private int size;
	/**
	 * capacity of the collection
	 */
	private int capacity;
	/**
	 * array where elements are stored
	 */
	private Object[] elements;

	/**
	 * Creates ArrayBackedIndexedCollection with initial capacity.
	 * 
	 * @param initialCapacity
	 *            initial capacity of the instance
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException(
					"Initial capacity has to be greater than 1.");
		}
		capacity = initialCapacity;
		size = 0;
		elements = new Object[capacity];
	}

	/**
	 * Creates ArrayBackedIndexedCollection with default capacity(16).
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}

	/**
	 * Sees if the collection is empty.
	 * 
	 * @return true if collection is empty, else false
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Calculates number of objects inside of the collection.
	 * 
	 * @return size of collection
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds an object to the collection.
	 * 
	 * @param value
	 *            added object
	 */
	public void add(Object value) {
		insert(value, size);
	}

	/**
	 * Doubles the capacity of the collection.
	 */
	private void reallocateCapacity() {
		reallocateCapacity(capacity * 2);
	}

	/**
	 * Reallocates collection's capacity to custom capacity.
	 * 
	 * @param newCapacity
	 * @throws IllegalArgumentException
	 *             if new capacity is smaller than collection's size
	 */
	private void reallocateCapacity(int newCapacity) {
		if (newCapacity < size) {
			throw new IllegalArgumentException(
					"Reallocated capacity must be greater than size of collection.");
		}

		Object[] reallocatedElements = new Object[newCapacity];
		for (int index = 0; index < size; index++) {
			reallocatedElements[index] = elements[index];
		}

		elements = reallocatedElements;

	}

	/**
	 * Gets an object from index location.
	 * 
	 * @param index
	 *            position at which element is located
	 * @return object from the collection
	 */
	public Object get(int index) {
		validateIndex(index);

		return elements[index];
	}

	/**
	 * Validates index.
	 * 
	 * @param index
	 * @throws IndexOutOfBoundsException
	 *             if index is invalid
	 */
	private void validateIndex(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException(
					"Index location is not defined.");
		}
	}

	/**
	 * Removes an element at index location. All elements positioned right to
	 * the element are repositioned one space to the left.
	 * 
	 * @param index
	 *            position of the element to be removed
	 */
	public void remove(int index) {
		validateIndex(index);

		elements[index] = null;
		size--;

		for (int iterateIndex = index; iterateIndex < capacity -1
				&& elements[iterateIndex + 1] != null; iterateIndex++) {

			elements[iterateIndex] = elements[iterateIndex + 1];
			elements[iterateIndex + 1] = null;
		}
	}

	/**
	 * Inserts an element at index location. If an object is already present at
	 * that location all elements to the will be shifted one place to the right.
	 * 
	 * @param value
	 *            object to insert
	 * @param position
	 *            index of the inserted element
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Collection can't store null values.");
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException(
					"Collection is not defined at that index.");
		}

		if (capacity == size) {
			reallocateCapacity();
		}

		for (int index = size; index > position; index--) {
			elements[index + 1] = elements[index];
		}
		elements[size] = value;
		size++;
	}

	/**
	 * Searches the collection for requested object and returns its index.
	 * 
	 * @param value
	 *            request element
	 * @return object's index if element is found or -1 if it's not found
	 */
	public int indexOf(Object value) {
		if (value != null) {
			for (int index = 0; index < size; index++) {
				if (elements[index].equals(value)) {
					return index;
				}
			}
		}

		return -1;
	}

	/**
	 * Searches the collection for the element.
	 * 
	 * @param value
	 *            object to search for
	 * @return true if found, else false
	 */
	public boolean contains(Object value) {
		if (indexOf(value) != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes all elements from the collection. Capacity of the collection will
	 * stay the same.
	 */
	public void clear() {
		for (int index = 0; index < size; index++) {
			elements[index] = null;
		}

		size = 0;
	}

	/**
	 * Demo of this class.
	 * 
	 * @param args
	 *            arguments from command line
	 */
	public static void main(String[] args) {
		ArrayBackedIndexedCollection col = new ArrayBackedIndexedCollection(2);
		col.add(new Integer(20));
		col.add("New York");
		col.add("San Francisco"); // here the internal array is reallocated to 4
		System.out.println(col.contains("New York")); // writes: true
		col.remove(1); // removes "New York"; shifts "San Francisco" to position
						// 1
		System.out.println(col.get(1)); // writes: "San Francisco"
		System.out.println(col.size()); // writes: 2

	}

}
