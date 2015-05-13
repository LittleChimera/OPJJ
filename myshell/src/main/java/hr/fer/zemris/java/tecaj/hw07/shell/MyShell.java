package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.TreeShellCommand;

/**
 * This program is a simple shell which enables multiline input and supports
 * following commands: ls, exit, tree, help, cat, symbol, charsets, mkdir, copy,
 * hexdump. Each command has it's description which can be get calling help with
 * command name as argument.
 * 
 * @author Luka Skugor
 *
 */
public class MyShell {

	/**
	 * Shell's name.
	 */
	public static final String SHELL_NAME = "MyShell";
	/**
	 * Shell's version.
	 */
	public static final String SHELL_VERSION = "1.0";

	/**
	 * Called on program start.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Map<String, ShellCommand> commands = new TreeMap<String, ShellCommand>();
		commands.put("ls", new LsShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());

		Environment env = new EnvironmentImpl(commands);
		System.out.format("Welcome to %s v %s%n", SHELL_NAME, SHELL_VERSION);

		ShellStatus status = ShellStatus.CONTINUE;
		do {
			String line = "";
			try {
				boolean multiline = false;
				Character morelinesCharacter = env.getMorelinesSymbol();

				do {
					if (multiline) {
						env.write(String.format("%-2c",
								env.getMultilineSymbol()));
					} else {
						env.write(String.format("%-2c", env.getPromptSymbol()));
					}

					line += " " + env.readLine().trim();
					if (line.endsWith(morelinesCharacter.toString())) {
						multiline = true;
						line = line.substring(0, line.length() - 1);
					} else {
						multiline = false;
					}

				} while (multiline);

				line = line.trim();
			} catch (IOException e) {
				System.err.println("Input/Output error.");
				System.exit(1);
			}

			String[] lineSplit = line.split(" +", 2);
			String commandName = lineSplit[0];

			if (!commands.containsKey(commandName)) {
				try {
					env.writeln(String.format("Command not found: %s",
							commandName));
				} catch (IOException e) {
					System.err.println("Output error.");
					System.exit(2);
				}
				continue;
			}

			String arguments = ((lineSplit.length > 1) ? lineSplit[1] : "");
			ShellCommand command = commands.get(commandName);
			status = command.executeCommand(env, arguments);

		} while (status != ShellStatus.TERMINATE);

	}
}
