package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Exit command sends signal to shell to stop taking commands.
 * 
 * @author Luka Skugor
 *
 */
public class ExitShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Exit command.
	 */
	public ExitShellCommand() {
		name = "exit";
		description.add(String.format("Terminates %s.", MyShell.SHELL_NAME));
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
		return ShellStatus.TERMINATE;
	}

}
