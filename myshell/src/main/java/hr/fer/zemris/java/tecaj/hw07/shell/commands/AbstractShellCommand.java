package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements some default functionality of {@link ShellCommand} and implements
 * some additional command which are useful for executing each shell command.
 * 
 * @author Luka Skugor
 *
 */
public abstract class AbstractShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	protected String name;
	/**
	 * Command description. Each list element is a new line.
	 */
	protected List<String> description;

	/**
	 * Creates a new shell command initializing command's description.
	 */
	public AbstractShellCommand() {
		description = new LinkedList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand#getCommandName
	 * ()
	 */
	@Override
	public String getCommandName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand#
	 * getCommandDescription()
	 */
	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

	/**
	 * Parses command's arguments and escapes appropriate ones if requested.
	 * Escaped characters can be '"' and '\'.If escaping is enabled then all
	 * word within quotes are parsed as single argument.
	 * 
	 * @param arguments
	 *            string which will be parsed
	 * @param escaping
	 *            if true escaping is enabled
	 * @return parsed arguments
	 * @throws IllegalArgumentException
	 *             if escaping is enabled and some quotes aren't closed
	 */
	protected List<String> parseArguments(String arguments, boolean escaping) {
		if (!escaping) {
			return Arrays.asList(arguments.split(" +"));
		}

		List<String> argumentsList = new LinkedList<String>();

		boolean escapeNext = false;
		boolean quoteStart = false;
		StringBuilder escapedArgumentBuilder = new StringBuilder();
		for (char c : arguments.toCharArray()) {
			// skip beginning spaces
			if (!quoteStart && escapedArgumentBuilder.length() == 0 && c == ' ') {
				continue;
			}
			if (escapedArgumentBuilder.length() == 0 && c == '\"') {
				quoteStart = true;
				continue;
			}

			if (!escapeNext && c == '\\') {
				escapeNext = true;
				continue;
			}
			// if escaped character shouldn't be escaped add escape
			if (escapeNext && !(c == '\\' || c == '\"')) {
				escapedArgumentBuilder.append('\\');
			}

			// if quote ends and last quote wasn't escaped or if sequence is not
			// quoted and word ends finish argument
			if ((!escapeNext && quoteStart && c == '\"')
					|| (!quoteStart && c == ' ')) {
				argumentsList.add(escapedArgumentBuilder.toString());
				escapedArgumentBuilder = new StringBuilder();
				quoteStart = false;
				escapeNext = false;
				continue;
			}

			escapedArgumentBuilder.append(c);
			escapeNext = false;
		}
		String lastArgument = escapedArgumentBuilder.toString();
		if (!lastArgument.isEmpty() && quoteStart) {
			throw new IllegalArgumentException("Some quotes aren't closed!");
		} else if (!lastArgument.isEmpty()) {
			argumentsList.add(lastArgument);
		}

		return argumentsList;
	}

	/**
	 * Writes text to {@link Environment}'s output stream.
	 * 
	 * @param env
	 *            reference to the environment
	 * @param text
	 *            text to write
	 */
	protected void writeToEnvironment(Environment env, String text) {
		try {
			env.write(text);
		} catch (IOException e1) {
			System.err.println("Output error occured.");
		}
	}

	/**
	 * Writes line to {@link Environment}'s output stream.
	 * 
	 * @param env
	 *            reference to the environment
	 * @param text
	 *            text to write
	 */
	protected void writelnToEnvironment(Environment env, String text) {
		try {
			env.writeln(text);
		} catch (IOException e1) {
			System.err.println("Output error occured.");
		}
	}

	/**
	 * Reads line from {@link Environment}'s input stream.
	 * 
	 * @param env
	 *            reference to the environment
	 * @return read line
	 */
	protected String readFromEnvironment(Environment env) {
		try {
			return env.readLine();
		} catch (IOException e1) {
			System.err.println("Input error occured.");
		}
		return null;
	}

	/**
	 * Checks if argument count is valid. If it's not appropriate message is
	 * printed.
	 * 
	 * @param env
	 *            reference to the environment in which command is called
	 * @param args
	 *            arguments to check
	 * @param minCount
	 *            minimum number of arguments
	 * @param maxCount
	 *            maximum number of arguments
	 * @return true if argument count is valid, else false
	 * @throws IllegalArgumentException
	 *             if minimum count is greater than max count
	 */
	protected boolean checkExpectedArguments(Environment env,
			List<String> args, int minCount, int maxCount) {
		if (minCount > maxCount) {
			throw new IllegalArgumentException();
		}

		int count = args.size();
		if (!(minCount <= count && count <= maxCount)) {
			int expected = (minCount > count) ? minCount : maxCount;
			writelnToEnvironment(env, "Expected at "
					+ ((minCount > count) ? "least " : "most ") + expected
					+ " argument" + ((expected > 1) ? "s" : "") + ".");
			return false;
		}

		return true;
	}

}
