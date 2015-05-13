package hr.fer.zemris.java.tecaj.hw4.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * This class is an implementation of hash map collection. It contains values
 * which are mapped to distinct keys. Most of methods in this map implementation
 * have complexity <i>O(1)</i>. Any object can be assigned as key or value in
 * this map, but keys must be distinct. This class implements
 * <code>Iterable{@literal<}SimpleHashtable.TableEntry{@literal>}</code> so it
 * can be used in short version of <code>for</code> loop. With addition of more
 * elements to the map, the capacity will change accordingly.
 * 
 * @see SimpleHashtable.TableEntry
 * @author Domagoj Latečki
 *
 */
public class SimpleHashtable implements Iterable<SimpleHashtable.TableEntry> {
	
	
	public int capacity() {
		return table.length;
	}
	/**
	 * Class which represents a single key-value entry in hash table. While
	 * value can be changed at will, key can only be assigned upon creation of
	 * an object from this class.
	 * 
	 * @see SimpleHashtable
	 * @author Domagoj Latečki
	 *
	 */
	public static class TableEntry {
		
		/**
		 * Object which represents a key in hash map entry.
		 */
		private Object key;
		/**
		 * Value which is mapped to key contained in this object.
		 */
		private Object value;
		/**
		 * Reference to the next entry in slot of the hash map.
		 */
		private TableEntry next;
		
		/**
		 * Constructs an object which contains value mapped to provided key, and
		 * also contains reference to the next object in list.
		 * 
		 * @param key key which will be stored in this entry.
		 * @param value value which will be mapped to given key.
		 * @param next reference to next entry in the list.
		 */
		public TableEntry(Object key, Object value, TableEntry next) {
		
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Constructs an object which contains value mapped to provided key.
		 * 
		 * @param key key which will be stored in this entry.
		 * @param value value which will be mapped to given key.
		 */
		public TableEntry(Object key, Object value) {
		
			this(key, value, null);
		}
		
		/**
		 * Fetches the key of this entry.
		 * 
		 * @return Key stored in this object.
		 */
		public Object getKey() {
		
			return key;
		}
		
		/**
		 * Fetches the value of this entry.
		 * 
		 * @return Value stored in this object.
		 */
		public Object getValue() {
		
			return value;
		}
		
		/**
		 * Sets the value stored in this object.
		 * 
		 * @param value new value to be stored in the object.
		 */
		public void setValue(Object value) {
		
			this.value = value;
		}
		
		@Override
		public String toString() {
		
			return key.toString() + "=" + value.toString();
		}
	}
	
	/**
	 * Size of the hash table created by default constructor.
	 */
	private static final int DEFAULT_HASHTABLE_SIZE = 16;
	/**
	 * Array which contains key-value pairs stored in hash table.
	 */
	private TableEntry[] table;
	/**
	 * Current number of key-value pairs in the hash table.
	 */
	private int size;
	/**
	 * Counter for number of modifications done to the hash table since it was
	 * created.
	 */
	private int modificationCount;
	
	/**
	 * Constructs a <code>SimpleHashtable</code> object with desired initial
	 * capacity.
	 * 
	 * @param capacity initial capacity of the hash map.
	 * @throws IllegalArgumentException If provided capacity is less than 1,
	 *             this exception will be thrown.
	 */
	public SimpleHashtable(int capacity) {
	
		if (capacity < 1) {
			throw new IllegalArgumentException("Provided capacity of hashtable was less than 1.");
		}
		
		// log [base 2] x = (log [base 10] x) / (log [base 10] 2)
		double power = Math.ceil(Math.log10(capacity) / Math.log10(2.0));
		this.table = new TableEntry[(int) Math.pow(2.0, power)];
		this.size = 0;
		this.modificationCount = 0;
	}
	
	/**
	 * Constructs a <code>SimpleHashtable</code> object with default initial
	 * capacity of {@value #DEFAULT_HASHTABLE_SIZE}.
	 */
	public SimpleHashtable() {
	
		this(DEFAULT_HASHTABLE_SIZE);
	}
	
	/**
	 * Puts a new key-value pair in the hash map. If given key already exists in
	 * the map, value assigned to that key will be changed to provided value.
	 * Provided key must not be a <code>null</code> reference.
	 * 
	 * @param key key which will be added to the map.
	 * @param value value which will be mapped to given key.
	 * @throws IllegalArgumentException If provided key is a <code>null</code>
	 *             reference, this exception will be thrown.
	 */
	public void put(Object key, Object value) {
	
		if (key == null) {
			throw new IllegalArgumentException("Provided key was a null reference.");
		}
		
		int slot = calculateSlot(key);
		TableEntry current = table[slot];
		TableEntry previous = null;
		if (current == null) {
			table[slot] = new TableEntry(key, value);
			modificationCount++;
			size++;
		} else {
			while (current != null) {
				if (current.getKey().equals(key)) {
					current.setValue(value);
					modificationCount++;
					break;
				} else {
					previous = current;
					current = current.next;
				}
			}
			if (current == null) {
				previous.next = new TableEntry(key, value);
				modificationCount++;
				size++;
			}
		}
		
		int filledSlots = 0;
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				filledSlots++;
			}
		}
		if (filledSlots >= (int) (0.75 * table.length)) {
			TableEntry[] oldTable = table;
			size = 0;
			table = new TableEntry[2 * oldTable.length];
			for (int i = 0; i < oldTable.length; i++) {
				current = oldTable[i];
				while (current != null) {
					put(current.getKey(), current.getValue());
					current = current.next;
				}
			}
			modificationCount++;
		}
	}
	
	/**
	 * Fetches the value assigned to provided key from the hash table. If there
	 * is no such key in the table, <code>null</code> reference will be
	 * returned.
	 * 
	 * @param key key for which value will be fetched from the table.
	 * @return Value mapped to provided key, or <code>null</code> reference if
	 *         no such key exists in the table. Note that <code>null</code>
	 *         reference can also be returned if provided key has
	 *         <code>null</code> reference assigned as its value.
	 */
	public Object get(Object key) {
	
		if (key == null) {
			return null;
		}
		
		TableEntry current = table[calculateSlot(key)];
		while (current != null) {
			if (current.getKey().equals(key)) {
				return current.value;
			} else {
				current = current.next;
			}
		}
		
		return null;
	}
	
	/**
	 * Fetches the current number of key-value pairs stored in the hash table.
	 * 
	 * @return Number of stored key-value pairs stored in the hash table.
	 */
	public int size() {
	
		return size;
	}
	
	/**
	 * Method for checking if the hash table contains provided key.
	 * 
	 * @param key key for which will be checked if it is contained in the hash
	 *            table.
	 * @return True if provided key is contained in the hash table, false
	 *         otherwise.
	 */
	public boolean containsKey(Object key) {
	
		if (key == null) {
			return false;
		}
		
		TableEntry current = table[calculateSlot(key)];
		while (current != null) {
			if (current.getKey().equals(key)) {
				return true;
			} else {
				current = current.next;
			}
		}
		
		return false;
	}
	
	/**
	 * Method for checking if the hash table contains provided value.
	 * 
	 * @param value value for which will be checked if it is contained in the
	 *            hash table.
	 * @return True if provided value is contained in the hash table, false
	 *         otherwise.
	 */
	public boolean containsValue(Object value) {
	
		for (int i = 0; i < table.length; i++) {
			TableEntry current = table[i];
			while (current != null) {
				if (value == null) {
					if (current.getValue() == null) {
						return true;
					}
				} else if (value.equals(current.getValue())) {
					return true;
				}
				current = current.next;
			}
		}
		return false;
	}
	
	/**
	 * Removes a key, along with its associated value, from the hash table. If
	 * given key is not in the table, nothing will be removed.
	 * 
	 * @param key key which is to be removed from the hash table.
	 */
	public void remove(Object key) {
	
		if (key == null) {
			return;
		}
		
		int slot = calculateSlot(key);
		TableEntry current = table[slot];
		TableEntry previous = null;
		while (current != null) {
			if (current.getKey().equals(key)) {
				if (previous == null) {
					table[slot] = current.next;
				} else {
					previous.next = current.next;
				}
				current.next = null;
				modificationCount++;
				size--;
				break;
			} else {
				previous = current;
				current = current.next;
			}
		}
	}
	
	/**
	 * Removes all entries from the hash table.
	 */
	public void clear() {
	
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		
		table = new TableEntry[table.length];
		modificationCount++;
		size = 0;
	}
	
	/**
	 * Method used for checking if hash table is empty.
	 * 
	 * @return True if hash table is empty, false otherwise.
	 */
	public boolean isEmpty() {
	
		return size == 0;
	}
	
	@Override
	public String toString() {
	
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int counter = 1;
		for (int i = 0; i < table.length; i++) {
			TableEntry current = table[i];
			while (current != null) {
				builder.append(current.toString());
				if (counter < size) {
					builder.append(", ");
				}
				counter++;
				current = current.next;
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Calculates slot in the table in which given key should be.
	 * 
	 * @param key key for which slot will be calculated.
	 * @return Slot in which key should be stored.
	 */
	private int calculateSlot(Object key) {
	
		return Math.abs(key.hashCode()) % table.length;
	}
	
	@Override
	public Iterator<TableEntry> iterator() {
	
		return new IteratorImpl(modificationCount);
	}
	
	/**
	 * Class which is an implementation of an iterator for
	 * <code>SimpleHashtable</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry> {
		
		/**
		 * Expected number of modifications in the hash table.
		 */
		private int expectedModificationCount;
		/**
		 * Current key-value pair which is iterated.
		 */
		private TableEntry current;
		/**
		 * Previous key-value pair which was iterated.
		 */
		private TableEntry previous;
		/**
		 * Current position in a table array of the hash table.
		 */
		private int positionInTable;
		/**
		 * Flag which indicates if element was removed by iterator in current
		 * iteration step.
		 */
		private boolean removedInThisStep;
		
		/**
		 * Constructs a <code>IteratorImpl</code> object which will be used to
		 * iterate the hash table.
		 * 
		 * @param expectedModificationCount current number of modifications in
		 *            the hash table. This number of modifications in hash table
		 *            will be expected by iterator to remain functioning
		 *            properly.
		 */
		private IteratorImpl(int expectedModificationCount) {
		
			this.expectedModificationCount = expectedModificationCount;
			this.positionInTable = 0;
			this.current = null;
			this.previous = null;
			this.removedInThisStep = false;
		}
		
		/**
		 * Method which throws an <code>ConcurrentModificationException</code>
		 * if hash table was modified externally while iterating.
		 * 
		 * @throws ConcurrentModificationException If hash table was modified
		 *             externally, this exception will be thrown.
		 */
		private void throwExceptionIfModified() {
		
			if (expectedModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
		}
		
		@Override
		public boolean hasNext() {
		
			throwExceptionIfModified();
			
			if (current != null && current.next != null) {
				return true;
			} else {
				for (int i = positionInTable; i < table.length; i++) {
					if (table[i] != null) {
						return true;
					}
				}
				return false;
			}
		}
		
		@Override
		public SimpleHashtable.TableEntry next() {
		
			throwExceptionIfModified();
			removedInThisStep = false;
			
			if (current != previous) {
				previous = current;
			}
			if (current != null && current.next != null) {
				current = current.next;
			} else {
				for (int i = positionInTable; i < table.length; i++) {
					if (table[i] != null) {
						current = table[i];
						positionInTable = i + 1;
						break;
					}
				}
			}
			return current;
		}
		
		@Override
		public void remove() {
		
			throwExceptionIfModified();
			if (removedInThisStep) {
				throw new IllegalStateException();
			}
			
			SimpleHashtable.this.remove(current.getKey());
			expectedModificationCount++;
			current = previous;
			removedInThisStep = true;
		}
	}
}
