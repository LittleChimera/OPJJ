package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Simple implementation of a
 * {@link hr.fer.zemris.java.simplecomp.models.Computer}.
 * 
 * @author Luka Škugor
 *
 */
public class ComputerImpl implements Computer {

	/**
	 * Computer's memory.
	 */
	private Memory memory;
	/**
	 * Computer's registers.
	 */
	private Registers registers;

	/**
	 * Creates a new computer with requested number of memory locations and
	 * available registers.
	 * 
	 * @param memoryLocations
	 *            number of memory locations of the computer
	 * @param registers
	 *            number of registers of the computer
	 */
	public ComputerImpl(int memoryLocations, int registers) {
		this.memory = new MemoryImpl(memoryLocations);
		this.registers = new RegistersImpl(registers);
	}

	@Override
	public Memory getMemory() {
		return memory;
	}

	@Override
	public Registers getRegisters() {
		return registers;
	}

}
