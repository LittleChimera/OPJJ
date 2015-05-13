package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which tests if two registers have same values and sets a flag true if they're
 * equal, else false. Instruction takes two arguments which are the registers
 * which will be compared.
 * 
 * @author Luka Škugor
 *
 */
public class InstrTestEquals implements Instruction {
	/**
	 * Index of the first argument register.
	 */
	private int registerIndex1;
	/**
	 * Index of the second argument register.
	 */
	private int registerIndex2;

	/**
	 * Creates a new InstrDecrement with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 2 argument
	 */
	public InstrTestEquals(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		if (!arguments.get(1).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for argument 1!");
		}
		this.registerIndex1 = ((Integer) arguments.get(0).getValue())
				.intValue();
		this.registerIndex2 = ((Integer) arguments.get(1).getValue())
				.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.models.Instruction#execute(hr.fer.zemris
	 * .java.simplecomp.models.Computer)
	 */
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters()
				.getRegisterValue(registerIndex1);
		Object value2 = computer.getRegisters()
				.getRegisterValue(registerIndex2);

		boolean setFlag;
		if (value1 == value2) {
			setFlag = true;
		} else if (value1 == null) {
			setFlag = false;
		} else if (value1.equals(value2)) {
			setFlag = true;
		} else {
			setFlag = false;
		}

		computer.getRegisters().setFlag(setFlag);
		return false;
	}
}