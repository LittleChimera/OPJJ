package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.Instruction;

/**
 * Simple implementation of a
 * {@link hr.fer.zemris.java.simplecomp.models.ExecutionUnit}.
 * 
 * @author Luka Skugor
 *
 */
public class ExecutionUnitImpl implements ExecutionUnit {

	/**
	 * @throws RuntimeException
	 *             if non-instruction should be executed
	 */
	@Override
	public boolean go(Computer computer) {
		computer.getRegisters().setProgramCounter(0);
		while (true) {
			int counter = computer.getRegisters().getProgramCounter();
			Object currentAdressObject = computer.getMemory().getLocation(
					counter);
			if (!(currentAdressObject instanceof Instruction)) {
				throw new RuntimeException();
			}
			computer.getRegisters().setProgramCounter(++counter);
			if (((Instruction) currentAdressObject).execute(computer)) {
				break;
			}
		}
		return true;
	}

}
