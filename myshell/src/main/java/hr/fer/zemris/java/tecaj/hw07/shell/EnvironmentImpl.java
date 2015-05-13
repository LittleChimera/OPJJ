package hr.fer.zemris.java.tecaj.hw07.shell;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Map;

/**
 * @author Luka Skugor
 *
 */
public class EnvironmentImpl implements Environment {
	
	/**
	 * Environment's input stream.
	 */
	private BufferedReader reader;
	/**
	 * Environment's output stream.
	 */
	private BufferedWriter writer;
	/**
	 * Environment's prompt symbol.
	 */
	private Character promptSymbol;
	/**
	 * Environment's shell commands.
	 */
	private Map<String, ShellCommand> commands;
	/**
	 * Environment's multiline symbol.
	 */
	private Character multilineSymbol;
	/**
	 * Environment's more lines symbol.
	 */
	private Character moreLinesSymbol;
	
	/**
	 * Creates a new {@link Environment} containing given shell commands.
	 * @param commands Environemnt's shell commands
	 */
	public EnvironmentImpl(Map<String, ShellCommand> commands) {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
		this.promptSymbol = '>';
		multilineSymbol = '|';
		moreLinesSymbol = '\\';
		this.commands = commands;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#readLine()
	 */
	@Override
	public String readLine() throws IOException {
		return reader.readLine();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#write(java.lang.String)
	 */
	@Override
	public void write(String text) throws IOException {
		writer.write(text);
		writer.flush();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#writeln(java.lang.String)
	 */
	@Override
	public void writeln(String text) throws IOException {
		write(String.format("%s%n", text));
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#commands()
	 */
	@Override
	public Iterable<ShellCommand> commands() {
		return Collections.unmodifiableCollection(commands.values());
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#getMultilineSymbol()
	 */
	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#setMultilineSymbol(java.lang.Character)
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		multilineSymbol = symbol;

	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#getPromptSymbol()
	 */
	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#setPromptSymbol(java.lang.Character)
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = symbol;

	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#getMorelinesSymbol()
	 */
	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.shell.Environment#setMorelinesSymbol(java.lang.Character)
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		moreLinesSymbol = symbol;
	}

}
