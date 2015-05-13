package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Implementation of a {@link hr.fer.zemris.java.simplecomp.models.Instruction}
 * which takes user input. The first and only arguments is where the read input
 * should be stored. Instruction accepts only integer input. If read input
 * cannot be parsed as integer the flag is set to false.
 * 
 * @author Luka Škugor
 *
 */
public class InstrIinput implements Instruction {

	/**
	 * Memory location where input will be stored.
	 */
	private int location;

	/**
	 * Creates a new InstrEcho with given arguments.
	 * 
	 * @param arguments
	 *            instruction arguments
	 * @throws IllegalArgumentException
	 *             if arguments are illegal or if there's not exactly 1
	 *             arguments
	 */
	public InstrIinput(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for argument 0!");
		}
		this.location = ((Integer) arguments.get(0).getValue()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.models.Instruction#execute(hr.fer.zemris
	 * .java.simplecomp.models.Computer)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.simplecomp.models.Instruction#execute(hr.fer.zemris
	 * .java.simplecomp.models.Computer)
	 */
	public boolean execute(Computer computer) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int readNumber = 0;
		try {
			String s = br.readLine();
			readNumber = Integer.parseInt(s);
		} catch (IOException e) {
			System.out.println("Critical input error!");
			System.exit(1);
		} catch (NumberFormatException e) {
			computer.getRegisters().setFlag(false);
			return false;
		}

		computer.getRegisters().setFlag(true);
		computer.getMemory().setLocation(location, Integer.valueOf(readNumber));

		return false;
	}
}