package hr.fer.zemris.java.simplecomp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hr.fer.zemris.java.simplecomp.impl.ComputerImpl;
import hr.fer.zemris.java.simplecomp.impl.ExecutionUnitImpl;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.InstructionCreator;
import hr.fer.zemris.java.simplecomp.parser.InstructionCreatorImpl;
import hr.fer.zemris.java.simplecomp.parser.ProgramParser;

/**
 * Simulator simulates a simple assembler processor which description can be
 * found in 6th Homework Assignment of OPPJ@FER. This program take only one
 * command argument which is a path to the text file containing assembler
 * instructions. If argument is omitted, path need to be entered through
 * standard system input.
 * 
 * @author Luka Skugor
 *
 */
public class Simulator {

	/**
	 * Runs on program start
	 * 
	 * @param args
	 *            arguments from command line
	 */
	public static void main(String[] args) {
		String path = null;
		if (args.length != 1) {
			System.out.println("Please enter a path to the assembler code.");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				path = br.readLine();
			} catch (IOException e) {
				System.exit(1);
			}
		} else {
			path = args[0];
		}
		// Stvori računalo s 256 memorijskih lokacija i 16 registara
		Computer comp = new ComputerImpl(256, 16);
		// Stvori objekt koji zna stvarati primjerke instrukcija
		InstructionCreator creator = new InstructionCreatorImpl(
				"hr.fer.zemris.java.simplecomp.impl.instructions");
		// Napuni memoriju računala programom iz datoteke; instrukcije stvaraj
		// uporabom predanog objekta za stvaranje instrukcija
		try {
			ProgramParser.parse(path.trim(), comp, creator);
		} catch (Exception e) {
			System.err.println("Parse error: " + e.getLocalizedMessage());
		}
		// Stvori izvršnu jedinicu
		ExecutionUnit exec = new ExecutionUnitImpl();
		// Izvedi program
		exec.go(comp);

	}

}
