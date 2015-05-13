package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * InstrJump has the same functionality as InstrJumpIfTrue but without checking
 * flag value.
 * 
 * @author Luka Skugor
 *
 */
public class InstrJump extends InstrJumpIfTrue implements Instruction {

	/**
	 * Creates a new InstrDecrement with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 1 argument
	 */
	public InstrJump(List<InstructionArgument> arguments) {
		super(arguments);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.impl.instructions.InstrJumpIfTrue#execute
	 * (hr.fer.zemris.java.simplecomp.models.Computer)
	 */
	@Override
	public boolean execute(Computer computer) {
		computer.getRegisters().setProgramCounter(jumpLocation);
		return false;
	}

}
