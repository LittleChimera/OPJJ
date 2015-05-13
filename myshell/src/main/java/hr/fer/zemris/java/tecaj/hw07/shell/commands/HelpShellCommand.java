package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.List;
import java.util.function.Consumer;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Help command provides information on existing shell commands of the
 * environment in which is executed. If started with no arguments, it lists
 * names of all supported commands. If started with single argument, it prints
 * name and the description of selected command (or printss appropriate error
 * message if no such command exists).
 * 
 * @author Luka Skugor
 *
 */
public class HelpShellCommand extends AbstractShellCommand {

	/**
	 * Creates new Help command.
	 */
	public HelpShellCommand() {
		name = "help";
		description.add("Lists usage of available shell commands");
		description.add("");
		description.add("Usage: help [COMMAND_NAME]");
		description.add("");
		description
				.add("If COMMAND_NAME is omitted all commands will be listed.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  help copy");
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
		List<String> argumentsList = parseArguments(arguments, false);

		if (!checkExpectedArguments(env, argumentsList, 1, 1)) {
			return ShellStatus.CONTINUE;
		}

		if (argumentsList.get(0).isEmpty()) {
			printAllCommands(env);
		} else {
			printSingleCommand(env, argumentsList.get(0));
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints all commands and first line from their description.
	 * @param env environment on which help is called
	 */
	private void printAllCommands(Environment env) {
		env.commands().forEach(new Consumer<ShellCommand>() {

			@Override
			public void accept(ShellCommand command) {
				writelnToEnvironment(env, String.format("%-10s %s", command
						.getCommandName(),
						command.getCommandDescription().get(0)));

			}
		});

	}

	/**
	 * Prints a whole description of a single command.
	 * @param env environment on which help is called
	 * @param commandName name of requested command's description
	 */
	private void printSingleCommand(Environment env, String commandName) {
		boolean[] found = { false };
		env.commands().forEach(new Consumer<ShellCommand>() {

			@Override
			public void accept(ShellCommand command) {
				if (command.getCommandName().equals(commandName)) {
					command.getCommandDescription().forEach(
							(d) -> writelnToEnvironment(env, d));
					found[0] = true;
				}
			}

		});

		if (!found[0]) {
			writelnToEnvironment(env, "Command not found.");
			return;
		}
	}
}
