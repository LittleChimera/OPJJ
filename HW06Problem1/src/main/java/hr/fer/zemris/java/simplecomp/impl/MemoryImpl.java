package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Memory;

/**
 * Simple implementation of a
 * {@link hr.fer.zemris.java.simplecomp.models.Memory}.
 * 
 * @author Luka Škugor
 *
 */
public class MemoryImpl implements Memory {

	/**
	 * Memory's locations.
	 */
	private Object[] memoryLocations;

	/**
	 * Creates a new memory with requested number of locations.
	 * 
	 * @param size memory size
	 */
	public MemoryImpl(int size) {
		memoryLocations = new Object[size];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.simplecomp.models.Memory#getLocation(int)
	 */
	@Override
	public Object getLocation(int location) {
		validateLocation(location);
		return memoryLocations[location];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.simplecomp.models.Memory#setLocation(int,
	 * java.lang.Object)
	 */
	@Override
	public void setLocation(int location, Object value) {
		validateLocation(location);
		memoryLocations[location] = value;
	}

	/**
	 * Validates memory location. Valid memory locations are numbers 0 to memory
	 * size (excluded).
	 * 
	 * @param location
	 *            memory location which will be validated
	 * @throws IllegalArgumentException
	 *             if memory location is invalid
	 */
	private void validateLocation(int location) {
		if (location >= memoryLocations.length || location < 0) {
			throw new IllegalArgumentException("Non existing memory location: "
					+ location);
		}
	}

}
