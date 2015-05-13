package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which decrements a value of a register by one. First and only instruction
 * arguments is a register which will be decremented.
 * 
 * @author Luka Skugor
 *
 */
public class InstrDecrement implements Instruction {

	/**
	 * Index of decremented register.
	 */
	private int registerIndex;

	/**
	 * Creates a new InstrDecrement with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 1 argument
	 */
	public InstrDecrement(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		this.registerIndex = ((Integer) arguments.get(0).getValue()).intValue();
	}

	/**
	 * @throws ArithmeticException
	 *             if objects in given register can't be decremented.
	 */
	@Override
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters().getRegisterValue(registerIndex);
		try {
			computer.getRegisters().setRegisterValue(registerIndex,
					Integer.valueOf(((Integer) value1).intValue() - 1));
		} catch (ClassCastException e) {
			throw new ArithmeticException("Can't decrement a non integer");
		}

		return false;
	}

}
