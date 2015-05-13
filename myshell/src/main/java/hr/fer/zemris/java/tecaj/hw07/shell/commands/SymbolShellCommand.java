package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Symbol command expects 1 or 2 arguments. If one arguments is given that
 * argument represents symbol name in the {@link Environment} which can be
 * "MULTINE", "PROMPT", "MORELINES". It then prints a symbol for the required
 * symbol name in the current environment. If two arguments are given first
 * represents the same as before and second is the replacement symbol for that
 * symbol name.
 * 
 * @author Luka Skugor
 *
 */
public class SymbolShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Symbol command.
	 */
	public SymbolShellCommand() {
		name = "symbol";
		description.add("Gets or sets environment symbols.");
		description.add("");
		description.add("Usage: symbol [SYMBOL_NAME] [REPLACE_SYMBOL]");
		description.add("");
		description
				.add("Replaces environment symbol for SYMBOL_NAME with REPLACE_SYMBOL");
		description
				.add("If REPLACE_SYMBOL is ommited prints current value of envirnment symbol for SYMBOL_NAME.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  symbol PROMPT");
		description.add("  symbol MULTILINE #");
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

		String symbolName = argumentsList.get(0);
		if (argumentsList.size() == 1) {
			try {
				writelnToEnvironment(env, String.format(
						"Symbol for %s is '%c'", symbolName,
						getSymbolForName(env, symbolName)));
			} catch (IllegalArgumentException e) {
				writelnToEnvironment(env, e.getLocalizedMessage());
			}
			return ShellStatus.CONTINUE;
		}

		String replaceString = argumentsList.get(1);
		if (replaceString.length() != 1) {
			writelnToEnvironment(env,
					"Second argument should be a single character.");
			return ShellStatus.CONTINUE;
		}

		Character replaceSymbol = replaceString.charAt(0);

		Character oldSymbol = replaceSymbol(env, symbolName, replaceSymbol);
		if (oldSymbol == null) {
			return ShellStatus.CONTINUE;
		}

		writelnToEnvironment(env, String.format(
				"Symbol for %s changed from '%c' to '%c'", symbolName,
				oldSymbol, replaceSymbol));

		return ShellStatus.CONTINUE;
	}

	/**
	 * Replaces a requested symbol in the current environment with the given
	 * one.
	 * 
	 * @param env
	 *            current environment
	 * @param symbolName
	 *            replacing symbol's name
	 * @param replaceSymbol
	 *            replacement symbol
	 * @return replaced symbol
	 */
	private Character replaceSymbol(Environment env, String symbolName,
			Character replaceSymbol) {

		Character oldSymbol = null;
		try {
			oldSymbol = getSymbolForName(env, symbolName);
			setSymbolForName(env, symbolName, replaceSymbol);
		} catch (IllegalArgumentException e) {
			writelnToEnvironment(env, e.getLocalizedMessage());
		}

		return oldSymbol;
	}

	/**
	 * Gets symbol from the environment for the given symbol's name.
	 * 
	 * @param env
	 *            current environment
	 * @param symbolName
	 *            symbol's name
	 * @return requested symbol
	 */
	private Character getSymbolForName(Environment env, String symbolName) {
		switch (symbolName) {
		case "PROMPT":
			return env.getPromptSymbol();
		case "MORELINES":
			return env.getMorelinesSymbol();
		case "MULTILINE":
			return env.getMultilineSymbol();
		default:
			throw new IllegalArgumentException("Unknown SYMBOL NAME.");
		}
	}

	/**
	 * Replaces symbol from the environment for the given symbol's name.
	 * 
	 * @param env
	 *            current environment
	 * @param symbolName
	 *            symbol's name
	 * @param replaceSymbol replacement symbol
	 */
	private void setSymbolForName(Environment env, String symbolName,
			Character replaceSymbol) {
		switch (symbolName) {
		case "PROMPT":
			env.setPromptSymbol(replaceSymbol);
			break;
		case "MORELINES":
			env.setMorelinesSymbol(replaceSymbol);
			break;
		case "MULTILINE":
			env.setMultilineSymbol(replaceSymbol);
			break;

		default:
			throw new IllegalArgumentException("Unknown SYMBOL NAME.");
		}
	}

}
