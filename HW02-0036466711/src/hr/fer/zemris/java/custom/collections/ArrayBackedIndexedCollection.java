package hr.fer.zemris.java.custom.collections;

/**
 * A collection providing custom insertion with O(n) complexity.
 * 
 * @author Mihovil Vinković
 */
public class ArrayBackedIndexedCollection {

	private int size;
	private int capacity;
	Object[] elements;

	/**
	 * Default constructor that provides an initial array size of 16.
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}

	/**
	 * A constructor where user provides initial array size. If you dont know
	 * how big you array should be, use the default constructor.
	 * 
	 * @param initialCapacity
	 *            Initial size of the array.
	 * @exception IllegalArgumentException
	 *                Capacity has to be over 1.
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}

		this.capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}

	/**
	 * Checks if the collection has zero elements.
	 * 
	 * @return Truth value whether the collection has zero elements.
	 */
	public boolean isEmpty() {
		return elements[0] == null;
	}

	/**
	 * Checks the current number of held elements in the collection.
	 * 
	 * @return Size of the collection.
	 */
	public int size() {
		return size;
	}

	/**
	 * Helper method for resizing the collection when there is not enough space.
	 */
	private void resize() {
		Object[] pomElems = new Object[capacity * 2];

		for (int i = 0; i < capacity; i++) {
			pomElems[i] = elements[i];
		}
		elements = pomElems;
		capacity = capacity * 2;
	}

	/**
	 * Adds an element to the tail of the collection.
	 * 
	 * @param value
	 *            Value that will be added
	 * @exception The
	 *                new value must not be null.
	 */
	public void add(Object value) {
		insert(value, size);
	}

	/**
	 * Returns the element at a desired index.
	 * 
	 * @param index
	 *            Index of the desired element within the collection.
	 * @return The element at the given index
	 * @exception IllegalArgumentException
	 *                Index has to be greater than 0 and less than total size.
	 */
	public Object get(int index) {
		if (index < 0 || index > size - 1) {
			throw new IllegalArgumentException();
		}

		return elements[index];
	}

	/**
	 * Removes the element at a desired index.
	 * 
	 * @param index
	 *            Index of the desired element within the collection.
	 * @exception IllegalArgumentException
	 *                Index has to be greater than 0 and less than total size.
	 */
	public void remove(int index) {
		if (index < 0 || index > size - 1) {
			throw new IllegalArgumentException();
		}

		for (int i = index + 1; i < size; i++) {
			elements[i - 1] = elements[i];
		}

		elements[size - 1] = null;
		size--;
	}

	/**
	 * Inserts the value at any index within the collection and pushes the
	 * elements after it.
	 * 
	 * @param value
	 *            Value that will be added.
	 * @param position
	 *            Index that the new element will be inserted at.
	 * @exception IllegalArgumentException
	 *                Index has to be greater than 0 and less than total size
	 *                and the new value must not be null.
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException();
		}

		if (position < 0 || position > size) {
			throw new IllegalArgumentException();
		}

		if (size == capacity) {
			resize();
		}

		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}

		elements[position] = value;
		size++;
	}

	/**
	 * Returns the index of the element with the specified value or -1 if that
	 * element doesn't exist.
	 * 
	 * @param value
	 *            Value of the desired element.
	 * @return Index of the desired element.
	 */
	public int indexOf(Object value) {
		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Checks if the element exists in the collection.
	 * 
	 * @param value
	 *            Value of the desired element.
	 * @return Truth value whether the element exists.
	 */
	public boolean contains(Object value) {
		return indexOf(value) >= 0;
	}

	/**
	 * Removes all elements from the collection.
	 */
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}
}
