package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which loads a value into a register. Instruction takes two arguments where
 * first is the register in which Object will be loaded and second is the Object
 * which will be loaded into the register.
 * 
 * @author Luka Skugor
 *
 */
public class InstrLoad implements Instruction {

	/**
	 * Index of register in which object will be loaded.
	 */
	private int registerIndex;
	/**
	 * Memory adress of the object which will be loaded in the register
	 */
	private int objectAdress;

	/**
	 * Creates a new InstrDecrement with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 2 argument
	 */
	public InstrLoad(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if (!arguments.get(1).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		this.registerIndex = ((Integer) arguments.get(0).getValue()).intValue();
		this.objectAdress = ((Integer) arguments.get(1).getValue()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.models.Instruction#execute(hr.fer.zemris
	 * .java.simplecomp.models.Computer)
	 */
	@Override
	public boolean execute(Computer computer) {
		// reads the object from the memory and writes it to given register
		computer.getRegisters().setRegisterValue(registerIndex,
				computer.getMemory().getLocation(objectAdress));
		return false;
	}

}
