package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Hexdump produces a hex output of a file. It expects a single argument: file
 * name. If user provides directory name for this command, appropriate error
 * message is written.
 * 
 * @author Luka Skugor
 *
 */
public class HexdumpShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Hexdump command.
	 */
	public HexdumpShellCommand() {
		name = "hexdump";
		description.add("Produces a FILE's hex output.");
		description.add("");
		description.add("Usage: hexdump [FILE]");
		description.add("");
		description.add("Examples of usage:");
		description.add("  hexdump examples/example.txt");
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

		Path filePath = Paths.get(argumentsList.get(0));

		try (InputStream is = new BufferedInputStream(new FileInputStream(
				filePath.toFile()))) {
			byte[] line = new byte[16];
			long bytesRead = 0;
			while (true) {
				int read = is.read(line);
				if (read < 1) {
					break;
				}
				writelnToEnvironment(env, formatLine(line, bytesRead));
				bytesRead += read;
			}

		} catch (IOException e) {

			if (!Files.exists(filePath)) {
				writelnToEnvironment(env, "File not found.");
			} else if (!Files.isRegularFile(filePath)) {
				writelnToEnvironment(env, "Given file is not a regular file.");
			} else if (!Files.isReadable(filePath)) {
				writelnToEnvironment(env, "Source isn't readable.");
			} else {
				writelnToEnvironment(env, "Read/write error occurred.");
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Formats a hexdump line.
	 * @param line bytes printed in hexdump line
	 * @param bytesRead count of how many bytes have been read so far
	 * @return formatted hexdump output line
	 */
	private String formatLine(byte[] line, long bytesRead) {
		StringBuilder linebBuilder = new StringBuilder(100);
		linebBuilder.append(String.format("%08X", bytesRead)).append(": ");

		StringBuilder hexValuesBuilder = new StringBuilder(50);
		StringBuilder standardSubsetBuilder = new StringBuilder(20);
		for (byte b : line) {
			hexValuesBuilder.append(String.format("%02X", b)).append(" ");
			standardSubsetBuilder
					.append((32 <= b && b <= 127) ? (char) b : '.');
		}
		hexValuesBuilder.replace(8 * 3 - 1, 8 * 3, "|").append("| ");
		linebBuilder.append(hexValuesBuilder.toString()).append(
				standardSubsetBuilder.toString());

		return linebBuilder.toString();
	}

}
