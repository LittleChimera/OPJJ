package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.util.List;

/**
 * ShellCommand represents an executable command in a shell.
 * @author Luka Skugor
 *
 */
public interface ShellCommand {
	/**
	 * Executes the command in the environment with given arguments.
	 * @param env environment in which the command is executed
	 * @param arguments command arguments
	 * @return ShellStatus.CONTINUE or ShellStatus.TERMINATE to terminate the shell
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	/**
	 * Gets command name.
	 * @return command name
	 */
	String getCommandName();
	/**
	 * Gets command description.
	 * @return command description
	 */
	List<String> getCommandDescription();
}
