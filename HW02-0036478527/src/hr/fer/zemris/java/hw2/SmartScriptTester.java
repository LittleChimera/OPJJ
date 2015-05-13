package hr.fer.zemris.java.hw2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Demo of SmartScriptParser. Takes a single argument - a path to the file which
 * is to be parsed.
 * 
 * @author Luka Skugor
 *
 */
public class SmartScriptTester {

	/**
	 * Recursively creates the document body from document tree model.
	 * 
	 * @param document
	 *            tree model from which document body is recreated
	 * @return document body
	 */
	private static String createOriginalDocumentBody(Node document) {
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append(document.toString());

		for (int index = 0, size = document.numberOfChildren(); index < size; index++) {
			Node childNode = document.getChild(index);
			bodyBuilder.append(createOriginalDocumentBody(childNode));
		}

		if (document instanceof ForLoopNode) {
			bodyBuilder.append("{$END$}");
		}

		return bodyBuilder.toString();
	}

	/**
	 * Main function demonstrating one example.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 *             if document path is invalid
	 */
	public static void main(String[] args) {
		String filename = null;
		try {
			filename = args[0];
		} catch (IndexOutOfBoundsException noPathGiven) {
			System.err.println("No path given");
			System.exit(1);
		}

		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(filename)),
					StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.err.println("Invalid path given");
			System.exit(1);
		}
		SmartScriptParser parser = null;

		try {
			parser = new SmartScriptParser(docBody);
			parser.parse();
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out
					.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like
													// original
		// content of docBody

	}

}
