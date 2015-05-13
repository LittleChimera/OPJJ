package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Simple implementation of a
 * {@link hr.fer.zemris.java.simplecomp.models.Registers}.
 * 
 * @author Luka Škugor
 *
 */
public class RegistersImpl implements Registers {

	/**
	 * Array of registers.
	 */
	private Object[] registers;
	/**
	 * Currently set flag.
	 */
	private boolean flag;
	/**
	 * Program counter which denotes which part of code needs to be executed
	 * next.
	 */
	private int programCounter;

	/**
	 * Creates a new set of registers with given number of registers.
	 * 
	 * @param regsLen
	 *            number of registers
	 */
	public RegistersImpl(int regsLen) {
		registers = new Object[regsLen];
		flag = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.simplecomp.models.Registers#getFlag()
	 */
	@Override
	public boolean getFlag() {
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.simplecomp.models.Registers#getProgramCounter()
	 */
	@Override
	public int getProgramCounter() {
		return programCounter;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if index is invalid
	 */
	@Override
	public Object getRegisterValue(int index) {
		validateIndex(index);
		return registers[index];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.models.Registers#incrementProgramCounter()
	 */
	@Override
	public void incrementProgramCounter() {
		programCounter++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.simplecomp.models.Registers#setFlag(boolean)
	 */
	@Override
	public void setFlag(boolean value) {
		flag = value;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if new program counter value is less than zero
	 */
	@Override
	public void setProgramCounter(int value) {
		if (value < 0) {
			throw new IllegalArgumentException(
					"Program counter can't be less than zero.");
		}
		programCounter = value;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if index is invalid
	 */
	@Override
	public void setRegisterValue(int index, Object value) {
		validateIndex(index);
		registers[index] = value;
	}

	/**
	 * Validates register index. Valid register indexes are numbers 0 to number
	 * of registers (excluded).
	 * 
	 * @param index
	 *            register index which will be validated
	 * @throws IllegalArgumentException
	 *             if index is invalid
	 */
	private void validateIndex(int index) {
		if (index >= registers.length || index < 0) {
			throw new IllegalArgumentException("No register at index " + index);
		}
	}

}
