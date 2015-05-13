package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMultistack allows storing multiple {@link hr.fer.zemris.java.custom.scripting.exec.ValueWrapper} for a key. Each key's value
 * acts as a stack to which values can be pushed, popped or peeked.
 * 
 * @author Luka Skugor
 *
 */
public class ObjectMultistack {

	/**
	 * Map in which MultistackEntries are stored.
	 */
	private Map<String, MultistackEntry> hashMap;

	/**
	 * Creates a new ObjectMultistack.
	 */
	public ObjectMultistack() {
		hashMap = new HashMap<String, MultistackEntry>();
	}

	/**
	 * Pushes a value wrapper on stack with given key.
	 * @param name stack key
	 * @param valueWrapper pushed value wrapper
	 * @throws IllegalArgumentException if name is null
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		validateKey(name);
		MultistackEntry entry = new MultistackEntry(valueWrapper);
		if (hashMap.containsKey(name)) {
			hashMap.get(name).append(entry);
		} else {
			hashMap.put(name, entry);
		}
	}

	/**
	 * Checks if stack is empty when trying to pop or peek from the stack and throws exception if it is.
	 * @param name stack key
	 * @throws EmptyStackException if stack is empty
	 */
	private void checkForEmptyStack(String name) {
		if (!hashMap.containsKey(name)) {
			throw new EmptyStackException();
		}
	}

	/**
	 * Pops a value wrapper from a stack with given key.
	 * @param name stack key
	 * @return popped value wrapper
	 * @throws IllegalArgumentException if name is null
	 * @throws EmptyStackException if stack is empty
	 */
	public ValueWrapper pop(String name) {
		validateKey(name);
		checkForEmptyStack(name);

		MultistackEntry entry = hashMap.get(name);
		MultistackEntry prev = null;
		for (; entry.next != null; entry = entry.next) {
			prev = entry;
		}
		ValueWrapper popedValue = entry.getValue();

		if (prev == null) {
			hashMap.remove(name);
		} else {
			prev.next = null;
		}

		return popedValue;
	}

	/**
	 * Peeks a value wrapper from the top of a stack with given key.
	 * @param name stack key
	 * @return peeked value wrapper
	 * @throws IllegalArgumentException if name is null
	 * @throws EmptyStackException if stack is empty
	 */
	public ValueWrapper peek(String name) {
		validateKey(name);
		checkForEmptyStack(name);

		MultistackEntry entry = hashMap.get(name);
		for (; entry.next != null; entry = entry.next)
			;

		return entry.getValue();
	}

	/**
	 * Checks if the stack with given key is empty.
	 * @param name stack key
	 * @return true if stack is empty, else false
	 * @throws IllegalArgumentException if name is null
	 */
	public boolean isEmpty(String name) {
		validateKey(name);
		return !hashMap.containsKey(name);
	}
	
	/**
	 * Checks if key is null and throws exception if it is.
	 * @param name key
	 * @throws IllegalArgumentException if name is null
	 */
	private void validateKey(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Keys can't be null.");
		}
	}

	/**
	 * Element of a stack. Holds the reference of the next element on the stack.
	 * @author Luka Skugor
	 *
	 */
	private static class MultistackEntry {

		/**
		 * ValueWrapper of stack element.
		 */
		private ValueWrapper value;
		/**
		 * Next element on the stack.
		 */
		private MultistackEntry next;

		/**
		 * Creates a new MultistackEntry with given ValueWrapper.
		 * @param valueWrapper wrapped value
		 */
		public MultistackEntry(ValueWrapper valueWrapper) {
			this.value = valueWrapper;
		}

		/**
		 * Appends an entry on the stack.
		 * @param entry appended entry
		 */
		public void append(MultistackEntry entry) {
			if (next == null) {
				next = entry;
			} else {
				next.append(entry);
			}
		}

		/**
		 * Gets ValueWrapper of stack element.
		 * @return ValueWrapper
		 */
		public ValueWrapper getValue() {
			return value;
		}
	}


}
