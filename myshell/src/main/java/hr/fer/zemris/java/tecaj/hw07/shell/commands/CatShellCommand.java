package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Cat command opens given file and writes it to console. It takes one or two
 * arguments. The first argument is path to some file and is mandatory. The
 * second argument is charset name that should be used to interpret chars from
 * bytes. If not provided, a default platform charset will be used.
 * 
 * @author Luka Skugor
 *
 */
public class CatShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Cat command.
	 */
	public CatShellCommand() {
		name = "cat";
		description
				.add("Prints the content of the FILE with CHARSET encoding.");
		description.add("");
		description.add("Usage: cat [FILE] [CHARSET]");
		description.add("");
		description
				.add("If CHARSET is omitted system's default charset will be used.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  cat examples/example.txt");
		description.add("  cat example.txt UTF-8");
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

		if (!checkExpectedArguments(env, argumentsList, 1, 2)) {
			return ShellStatus.CONTINUE;
		}

		Charset charset;
		if (argumentsList.size() == 1) {
			charset = Charset.defaultCharset();
		} else {
			try {
				charset = Charset.forName(argumentsList.get(1));
			} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
				writelnToEnvironment(env, "Unknown charset.");
				return ShellStatus.CONTINUE;
			}
		}

		readFile(env, Paths.get(argumentsList.get(0)), charset);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Reads and write file to the environment's output stream.
	 * @param env environment from which command is called
	 * @param path path of the fill to be written
	 * @param charset file text encoding
	 */
	private void readFile(Environment env, Path path, Charset charset) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(path.toFile())),
				charset))) {
			char[] buffer = new char[4096];
			while (true) {
				int read = br.read(buffer);
				if (read < 1) {
					break;
				}
				env.write(new String(Arrays.copyOfRange(buffer, 0, read)));
			}
		} catch (Exception e) {

			if (!Files.exists(path)) {
				writelnToEnvironment(env, "File not found.");
			} else if (!Files.isRegularFile(path)) {
				writelnToEnvironment(env, "Given file is not a regular file.");
			} else if (!Files.isReadable(path)) {
				writelnToEnvironment(env, "Source isn't readable.");
			} else {
				writelnToEnvironment(env, "Read/write error occurred.");
			}
		}
	}

}
