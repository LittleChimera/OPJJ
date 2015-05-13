package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which multiplies values of two registers and saves the result to the 3 one.
 * First instruction argument is the register where the result will be stored
 * and on the other two operation is performed.
 * 
 * @author Luka Škugor
 *
 */
public class InstrMul implements Instruction {
	/**
	 * Index of the first argument register.
	 */
	private int registerIndex1;
	/**
	 * Index of the second argument register.
	 */
	private int registerIndex2;
	/**
	 * Index of the third argument register.
	 */
	private int registerIndex3;

	/**
	 * Creates a new InstrAdd with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 3
	 *             arguments
	 */
	public InstrMul(List<InstructionArgument> arguments) {
		if (arguments.size() != 3) {
			throw new IllegalArgumentException("Expected 3 arguments!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if (!arguments.get(1).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		if (!arguments.get(2).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 2!");
		}
		this.registerIndex1 = ((Integer) arguments.get(0).getValue())
				.intValue();
		this.registerIndex2 = ((Integer) arguments.get(1).getValue())
				.intValue();
		this.registerIndex3 = ((Integer) arguments.get(2).getValue())
				.intValue();
	}

	/**
	 * @throws ArithmeticException
	 *             if objects in given registers can't be multiplied.
	 */
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters()
				.getRegisterValue(registerIndex2);
		Object value2 = computer.getRegisters()
				.getRegisterValue(registerIndex3);

		try {
			computer.getRegisters().setRegisterValue(
					registerIndex1,
					Integer.valueOf(((Integer) value1).intValue()
							* ((Integer) value2).intValue()));
		} catch (ClassCastException e) {
			throw new ArithmeticException("Only numbers can be multiplied.");
		}

		return false;
	}
}