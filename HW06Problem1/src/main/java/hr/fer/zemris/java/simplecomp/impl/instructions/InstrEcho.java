package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which echoes a value of a single register. First and only instruction
 * arguments is a register which will be echoed.
 * 
 * @author Luka Skugor
 *
 */
public class InstrEcho implements Instruction {

	/**
	 * Index of register which will be echoed.
	 */
	private int registerIndex;

	/**
	 * Creates a new InstrEcho with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 1
	 *             arguments
	 */
	public InstrEcho(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		this.registerIndex = ((Integer) arguments.get(0).getValue()).intValue();
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
		System.out.print(computer.getRegisters()
				.getRegisterValue(registerIndex));
		return false;
	}

}
