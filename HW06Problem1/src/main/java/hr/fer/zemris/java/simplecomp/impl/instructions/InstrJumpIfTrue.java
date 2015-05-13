package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which changes program counter. Instruction takes one and only arguments which
 * is a number on which program counter will be set.
 * 
 * @author Luka Skugor
 *
 */
public class InstrJumpIfTrue implements Instruction {

	/**
	 * Memory location on which program counter will be set.
	 */
	protected int jumpLocation;

	/**
	 * Creates a new InstrDecrement with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 1 argument
	 */
	public InstrJumpIfTrue(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		this.jumpLocation = ((Integer) arguments.get(0).getValue()).intValue();
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
		if (computer.getRegisters().getFlag()) {
			computer.getRegisters().setProgramCounter(jumpLocation);
		}
		return false;
	}

}
