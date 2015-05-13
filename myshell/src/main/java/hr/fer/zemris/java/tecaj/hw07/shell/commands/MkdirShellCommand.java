package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * The Mkdir command takes a single argument: directory name, and creates the
 * appropriate directory structure.
 * 
 * @author Luka Skugor
 *
 */
public class MkdirShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Mkdir command.
	 */
	public MkdirShellCommand() {
		name = "mkdir";
		description.add("Creates a new directory from given PATH.");
		description.add("");
		description.add("Usage: mkdir [PATH]");
		description.add("");
		description
				.add("Creates all parent directories as well if they don't exist.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  mkdir outer/innerdir1/innerdir2");
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
		List<String> argumentsList = parseArguments(arguments, true);

		if (!checkExpectedArguments(env, argumentsList, 1, 1)) {
			return ShellStatus.CONTINUE;
		}

		Path dirPath = Paths.get(argumentsList.get(0));
		if (Files.exists(dirPath)) {
			writelnToEnvironment(
					env,
					String.format(((Files.isDirectory(dirPath)) ? "Directory"
							: "File") + "\"%s\" already exists.",
							dirPath.getFileName()));
			return ShellStatus.CONTINUE;
		}

		try {
			Files.createDirectories(dirPath);
		} catch (IOException e) {
			writelnToEnvironment(env, "Creation error.");
		}

		return ShellStatus.CONTINUE;
	}

}
