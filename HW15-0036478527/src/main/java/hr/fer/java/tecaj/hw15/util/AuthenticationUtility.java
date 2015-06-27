package hr.fer.java.tecaj.hw15.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AuthenticationUtility is a utility class which contains some methods to ease
 * authentication.
 * 
 * @author Luka Skugor
 *
 */
public class AuthenticationUtility {

	/**
	 * Encrypts password with SHA-1 algorithm.
	 * 
	 * @param password
	 *            encrypting password
	 * @return encrypted password in hexadecimal format
	 */
	public static String encryptPassword(String password) {
		MessageDigest pwDigest = null;
		try {
			pwDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ignorable) {
		}

		pwDigest.update(password.getBytes());

		StringBuilder shaStringBuilder = new StringBuilder();
		for (byte b : pwDigest.digest()) {
			shaStringBuilder.append(String.format("%02X", b));
		}

		return shaStringBuilder.toString();
	}

}
