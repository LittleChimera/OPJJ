package hr.fer.zemris.java.custom.collections;

/**
 * Class gives basic functions for work with Array Collection
 * 
 * @author Borna Feldšar
 * @version 1.0
 */
public class ArrayBackedIndexedCollection {
	private int size;
	private int capacity;
	private Object[] elements;

	/**
	 * Default constructor creates collection size 16.
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}

	/**
	 * Constructor creates collection with initialCapacity size
	 * 
	 * @param initialCapacity
	 *            size of new collection
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity must be positive!");
		}
		size = 0;
		capacity = initialCapacity;
		elements = new Object[capacity];
	}

	/**
	 * Main method. Use for testing collection funcionality.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayBackedIndexedCollection col = new ArrayBackedIndexedCollection(2);
		col.add(new Integer(20));
		col.add("New York");
		col.clear();
		col.add("San Francisco"); // here the internal array is reallocated to 4
		col.insert("Zagreb", 0);
		System.out.println(col.contains("New York")); // writes: true
		// removes "New York"; shifts "San Francisco" to position 1
		for (int i = 0; i < 2; i++) {
			System.out.println(col.get(i)); // writes: "San Francisco"

		}
		System.out.println(col.size()); // writes: 2

	}

	/**
	 * Return true if collection containes no object, true otherwise.
	 * 
	 * @return true if collection is empty, false otherwise.
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns number of currently stored objects in collection.
	 * 
	 * @return collection size
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds given object into the first empty place in the elements array. If
	 * the elements array is full, it reallocete by doubling its size.
	 * 
	 * @param value
	 *            object that adds into collection
	 */
	public void add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null!");
		}

		if (size == capacity) {
			Object[] tmp;
			capacity *= 2;
			tmp = new Object[capacity];
			for (int i = 0; i < size; i++) {
				tmp[i] = elements[i];
			}
			elements = tmp;

		}
		elements[size] = value;
		size++;
	}

	/**
	 * Returns the object that is stored in collection at position index.
	 * 
	 * @param index
	 *            position of stored object in collection
	 * @return object from index
	 * @throws IndexOutOfBoundsException
	 *             if index is smaller then 0 and bigger than size-1.
	 */
	public Object get(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}

		return elements[index];
	}

	/**
	 * Removes the object that is stored in collection at index. Shifts elements
	 * which is at position greater than index one position down.
	 * 
	 * @param index
	 *            index of object for remove
	 * @throws IndexOutOfBoundsException
	 *             * if index is smaller then 0 and bigger than size-1.
	 */
	public void remove(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[size - 1] = null;
		size--;
	}

	/**
	 * Inserts given value at the given position in collection.
	 * 
	 * @param value
	 *            object that inserts in collection
	 * @param position
	 *            position in collection where object will be inserted.
	 * @throws IndexOutOfBoundsException
	 *             if index is smaller then 0 and bigger than size.
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

		if (position == size) {
			add(value);
		} else {
			if (size + 1 > capacity) {
				Object[] tmp;
				capacity *= 2;
				tmp = new Object[capacity];
				for (int i = 0; i < size; i++) {
					tmp[i] = elements[i];
				}
				elements = tmp;

			}
			for (int i = size; i > position; i--) {
				elements[i] = elements[i - 1];
			}
			elements[position] = value;
			size++;
		}
	}

	/**
	 * Returns index of the first occurerence of the given object in collection
	 * 
	 * @param value
	 *            object which index returns
	 * @return index of object in collection, if object is not found returns -1.
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if the collection contains given value.
	 * 
	 * @param value
	 *            object
	 * @return true if collection contains value, false otherwise
	 */
	public boolean contains(Object value) {
		if (value == null) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				return true;
			}
		}
		return false;
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
