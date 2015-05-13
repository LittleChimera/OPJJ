package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Helper class for {@link hr.fer.zemris.java.tecaj.hw07.crypto.Crypto}
 * @author Luka Skugor
 *
 */
public class CryptoHelper {

	/**
	 * Parses a string for hex values and returns array of bytes parsed as hex values.
	 * @param hexString string containing hex values ([0-9][A-F])
	 * @return array of parsed bytes
	 * @throws NumberFormatException if string contains characters which are not hex values.
	 */
	public static byte[] hextobyte(String hexString) {
		if (hexString.length() % 2 == 1) {
			hexString = "0" + hexString;
		}
		byte[] byteText = new byte[hexString.length() / 2];
		for (int i = 0, parislength = hexString.length() / 2; i < parislength; i++) {
			try {
				byteText[i] = (byte) Integer.parseInt(
						hexString.substring(i * 2, (i + 1) * 2), 16);				
			} catch (NumberFormatException e) {
				throw new NumberFormatException("Given string contains non hexadecimal characters.");
			}
		}

		return byteText;
	}

	/**
	 * Creates AES {@link javax.crypto.Cipher} for given key and vector.
	 * @param keyText key string
	 * @param ivText vector string
	 * @param encrypt if true cipher encrypts values, if false cipher decrypts values
	 * @return created cipher
	 */
	public static Cipher getAesCipher(String keyText, String ivText,
			boolean encrypt) {
		keyText = keyText.trim();
		ivText = ivText.trim();
		
		SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(
				hextobyte(ivText));
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			//ignorable
		}
		try {
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
					keySpec, paramSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			//ignorable
		}

		return cipher;
	}

	
}
