package hr.fer.zemris.java.tecaj.hw07.crypto.actions;

import java.util.List;

/**
 * Defines a command line action for
 * {@link hr.fer.zemris.java.tecaj.hw07.crypto.Crypto} program. The action can
 * take multiple arguments and is called through {@link execute} method.
 * 
 * @author Luka Skugor
 *
 */
public interface ICryptoAction {
	
	/**
	 * Executes the action.
	 * @param arguments action arguments
	 */
	public void execute(List<String> arguments);
	
}
