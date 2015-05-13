package hr.fer.zemris.java.tecaj.hw07.crypto.actions;

import hr.fer.zemris.java.tecaj.hw07.crypto.CryptoHelper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * {@link ICryptoAction } which when executed creates a SHA-1 checksum for given file.
 * @author Luka Skugor
 *
 */
public class CheckshaAction implements ICryptoAction {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.crypto.actions.ICryptoAction#execute(java.util.List)
	 */
	@Override
	public void execute(List<String> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		Path filePath = Paths.get(arguments.get(0));
		byte[] calculatedSha = calculateSha(filePath);
		
		System.out.format("Please provide expected sha-256 digest for %s:%n> ", filePath.getFileName());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		try {
			s = br.readLine();
		} catch (IOException e) {
			System.err.print("Input error!");
			System.exit(1);
		}
		byte[] inputSha = CryptoHelper.hextobyte(s);
		
		System.out.print("Digesting completed. ");
		if (Arrays.equals(calculatedSha, inputSha)) {
			System.out.format("Digest of %s matches expected digest.%n",
					filePath.getFileName());
		} else {
			StringBuilder shaStringBuilder = new StringBuilder();
			for (byte b : calculatedSha) {
				shaStringBuilder.append(String.format("%02X", b));
			}
			System.out
					.format("Digest of %s does not match the expected digest. Digest was: %s%n",
							filePath.getFileName(), shaStringBuilder.toString()
									.toLowerCase());
		}
	}

	/**
	 * Calculates a SHA-1 checksum for given file path.
	 * @param filePath path of the file which checksum will be calculated
	 * @return array of bytes representing the calculated checksum
	 * @throws RuntimeException if file is not found/readable
	 */
	private static byte[] calculateSha(Path filePath) {

		MessageDigest aes = null;
		try {
			aes = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// ignorable
		}

		try (InputStream is = new BufferedInputStream(Files.newInputStream(
				filePath, StandardOpenOption.READ))) {
			byte[] buffer = new byte[1024];

			while (true) {
				int r = is.read(buffer);
				if (r < 1) {
					break;
				}
				aes.update(buffer, 0, r);
			}

		} catch (Exception e) {
			throw new RuntimeException("File not found");
		}

		return aes.digest();
	}
}
