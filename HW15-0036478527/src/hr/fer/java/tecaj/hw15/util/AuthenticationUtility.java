package hr.fer.java.tecaj.hw15.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthenticationUtility {

	public static String encryptPassword(String password) {
		MessageDigest pwDigest = null;
		try {
			pwDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ignorable) {
		}
		
		pwDigest.update(password.getBytes());
		return Base64.getEncoder().encodeToString(
				new String(pwDigest.digest()).getBytes());
	}

}
