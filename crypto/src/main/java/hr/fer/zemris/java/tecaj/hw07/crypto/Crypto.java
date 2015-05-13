package hr.fer.zemris.java.tecaj.hw07.crypto;

import hr.fer.zemris.java.tecaj.hw07.crypto.actions.CheckshaAction;
import hr.fer.zemris.java.tecaj.hw07.crypto.actions.CryptAction;
import hr.fer.zemris.java.tecaj.hw07.crypto.actions.ICryptoAction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Program which has the ability of crypting and decrypting files with AES
 * algorithm. It can also calculate SHA-1 checksum.
 * 
 * @author Luka Skugor
 *
 */
public class Crypto {

	/**
	 * Called on progrma start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Expected at least one action argument!");
			System.exit(1);
		}

		Map<String, ICryptoAction> actions = new HashMap<String, ICryptoAction>();
		actions.put("checksha", new CheckshaAction());
		actions.put("encrypt", new CryptAction(true));
		actions.put("decrypt", new CryptAction(false));

		String actionName = args[0];
		List<String> arguments = Arrays.asList(Arrays.copyOfRange(args, 1,
				args.length));

		try {
			actions.get(actionName).execute(arguments);
		} catch (NullPointerException e) {
			System.err.format("Action \"%s\" doesn't exists.%n", actionName);
			System.exit(2);
		} catch (RuntimeException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(3);
		}
	}

}
