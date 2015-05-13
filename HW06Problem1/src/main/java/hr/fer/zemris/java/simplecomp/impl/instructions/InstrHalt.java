package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which halts the processor. Instruction takes no arguments.
 * 
 * @author Luka Skugor
 *
 */
public class InstrHalt implements Instruction {

	/**
	 * Creates a new InstrEcho with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if any arguments are passed
	 */
	public InstrHalt(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Unexpected arguments!");
		}
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
		return true;
	}

}
