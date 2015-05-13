package hr.fer.zemris.java.tecaj.hw07.shell;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

import java.io.IOException;

/**
 * Defines a shell environment. It has capabilities of storing
 * {@link hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand}s, writing to output stream, reading input
 * stream and stores symbols for multiline input and shell prompt.
 * 
 * @author Luka Skugor
 *
 */
public interface Environment {
	/**
	 * Reads line from environments input stream.
	 * 
	 * @return read line
	 * @throws IOException
	 *             if I/O error occurs
	 */
	String readLine() throws IOException;

	/**
	 * Writes text to Environment's output stream.
	 * 
	 * @param text
	 *            text to be written
	 * @throws IOException
	 *             if I/O error occurs
	 */
	void write(String text) throws IOException;

	/**
	 * Writes a line to environment's output stream.
	 * 
	 * @param text line's text
	 * @throws IOException if I/O error occurs
	 */
	void writeln(String text) throws IOException;

	/**
	 * Returns an {@link java.lang.Iterable} of Environment's
	 * {@link hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand}s
	 * 
	 * @return iterable of shell commands
	 */
	Iterable<ShellCommand> commands();

	/**
	 * Gets Environment's multiline symbol.
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();

	/**
	 * Sets Environment's multiline symbol.
	 * @param symbol set symbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Gets Environment's prompt symbol.
	 * @return prompt symbol
	 */
	Character getPromptSymbol();

	/**
	 * Sets Environment's prompt symbol.
	 * @param symbol set symbol
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Gets Environment's more lines symbol.
	 * @return more lines symbol
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets Environment's more lines symbol.
	 * @param symbol more lines symbol
	 */
	void setMorelinesSymbol(Character symbol);

}
