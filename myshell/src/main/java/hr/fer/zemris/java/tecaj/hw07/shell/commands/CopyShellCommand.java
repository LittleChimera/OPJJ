package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Copy command copies a file to given path. The command expects two arguments:
 * source file name and destination file name (i.e. paths and names). Is
 * destination file exists, user is asked whether the file should be
 * overwritten. Command works only with files (no directories). If the second
 * argument is directory, it is assumeds that user wants to copy the original
 * file in that directory using the original file name.
 * 
 * @author Luka Skugor
 *
 */
public class CopyShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new copy command.
	 */
	public CopyShellCommand() {
		name = "copy";
		description.add("Copies a SOURCE_FILE content to DESTINATION_FILE");
		description.add("");
		description.add("Usage: copy [SOURCE_FILE] [DESTINATION_FILE]");
		description.add("");
		description
				.add("If DESTINATION_FILE doesn't exists you will be prompted whether to overwrite it.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  copy examples/example.txt examplecopy.txt");
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

		if (!checkExpectedArguments(env, argumentsList, 2, 2)) {
			return ShellStatus.CONTINUE;
		}

		Path sourcePath = Paths.get(argumentsList.get(0));
		if (Files.isDirectory(sourcePath)) {
			writelnToEnvironment(env,
					"Sorce path is a directory and this command doesn't allow copying directiores.");
			return ShellStatus.CONTINUE;
		}
		if (!Files.isReadable(sourcePath)) {
			writelnToEnvironment(env, "Source file isn't readable.");
			return ShellStatus.CONTINUE;
		}

		Path destPath = Paths.get(argumentsList.get(1));
		if (Files.isDirectory(destPath)) {
			destPath = destPath.resolve(sourcePath.getFileName());
		}

		if (Files.exists(destPath)) {
			String answer;
			writelnToEnvironment(env, String.format(
					"File \"%s\" already exists. Do you want to overwrite it?",
					destPath.toString()));
			do {
				writeToEnvironment(env, "([Y]es/[N]o?) ");
				answer = readFromEnvironment(env);
			} while (!validateAnswer(answer));
			if (answer.toLowerCase().startsWith("n")) {
				return ShellStatus.CONTINUE;
			}
		} else {
			try {
				Files.createFile(destPath);
			} catch (IOException e) {
				if (!Files.exists(destPath.getParent())) {
					writelnToEnvironment(env,
							"Destination parent directory doesn't exists.");
				} else if (!Files.isWritable(destPath)) {
					writelnToEnvironment(env,
							"You don't have write access to destination path.");
				} else {
					writelnToEnvironment(env,
							"IO error occurred in creating destination directory.");
				}
				return ShellStatus.CONTINUE;
			}
		}

		try (InputStream is = new BufferedInputStream(new FileInputStream(
				sourcePath.toFile()));
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(destPath.toFile()))) {
			byte[] buffer = new byte[4096];
			while (true) {
				int read = is.read(buffer);
				if (read < 1) {
					break;
				}
				os.write(buffer, 0, read);
			}

		} catch (IOException e) {
			writelnToEnvironment(env, "Error occured when copying file.");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Validates user answer which for confirmation or declination. Valid
	 * answers are "y" or "yes" for confirmation and "n" or "no" for
	 * declination. Answers are not case sensitive.
	 * 
	 * @param answer user answer
	 * @return true if valid, else false
	 */
	private boolean validateAnswer(String answer) {
		String[] legalAnswers = { "y", "yes", "n", "no" };
		Arrays.sort(legalAnswers);
		return (Arrays.binarySearch(legalAnswers, answer.toLowerCase()) >= 0) ? true
				: false;
	}

}
