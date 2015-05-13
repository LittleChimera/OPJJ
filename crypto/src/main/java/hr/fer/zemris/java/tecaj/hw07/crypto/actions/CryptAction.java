package hr.fer.zemris.java.tecaj.hw07.crypto.actions;

import hr.fer.zemris.java.tecaj.hw07.crypto.CryptoHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.crypto.Cipher;

/**
 * {@link ICryptoAction } which encrypts or decrypts a file in AES based on given constructor argument.
 * User will be prompted to write AES key and initialization vector in both cases.
 * @author Luka Skugor
 *
 */
public class CryptAction implements ICryptoAction {

	/**
	 * A flag indicating if the action will encrypt or decrypt given file.
	 */
	private boolean encrpyt;

	/**
	 * Creates an action which encrypts data if given argument is true, else decrypts. 
	 * @param encrypt if true decrypts data, else decrypts
	 */
	public CryptAction(boolean encrypt) {
		this.encrpyt = encrypt;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw07.crypto.actions.ICryptoAction#execute(java.util.List)
	 */
	@Override
	public void execute(List<String> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		Path inputPath = Paths.get(arguments.get(0));
		Path outputPath = Paths.get(arguments.get(1));
		try {
			outputPath.toFile().createNewFile();
		} catch (IOException e1) {
			System.err.print("Failed to create the output file!");
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String keyText = null, ivText = null;
		try {
			System.out
					.format("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
			keyText = br.readLine();
			System.out
					.format("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
			ivText = br.readLine();
		} catch (IOException e) {
			System.err.print("Input error!");
			System.exit(1);
		}
		Cipher cipher = CryptoHelper.getAesCipher(keyText, ivText, encrpyt);

		fileCipher(cipher, inputPath, outputPath);

		System.out.format(((encrpyt) ? "En" : "De")
				+ "cryption completed. Generated file %s based on file %s.%n",
				outputPath.getFileName(), inputPath.getFileName());

	}

	/**
	 * Applies cipher on given input file and writes it to output path.
	 * @param cipher {@link javax.crypto.Cipher} object while will cipher input file
	 * @param inputPath path of input file
	 * @param outputPath path of output file
	 * @throws RuntimeException if any IO error occurs
	 */
	private static void fileCipher(Cipher cipher, Path inputPath, Path outputPath) {

		try (InputStream is = new BufferedInputStream(Files.newInputStream(
				inputPath, StandardOpenOption.READ));

				OutputStream os = new BufferedOutputStream(
						Files.newOutputStream(outputPath,
								StandardOpenOption.WRITE))) {

			byte[] buffer = new byte[1024];

			while (true) {
				int r = is.read(buffer);
				if (r < 1) {
					break;
				}
				os.write(cipher.update(buffer, 0, r));
			}

		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

}
