package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.nio.charset.Charset;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command charsets takes no arguments and lists names of supported charsets for
 * your Java platform. A single charset name is written per line.
 * 
 * @author Luka Skugor
 *
 */
public class CharsetsShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Charsets command.
	 */
	public CharsetsShellCommand() {
		name = "charsets";
		description.add("Prints all available system's charsets.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand#executeCommand
	 * (hr.fer.zemris.java.tecaj.hw07.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		writelnToEnvironment(env, "Available charsets:");
		for (Charset charset : Charset.availableCharsets().values()) {
			writelnToEnvironment(env, charset.displayName());
		}

		return ShellStatus.CONTINUE;
	}

}
