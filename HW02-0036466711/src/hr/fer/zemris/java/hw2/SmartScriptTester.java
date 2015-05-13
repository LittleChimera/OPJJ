package hr.fer.zemris.java.hw2;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * A class intended for usage of SmartScriptParser utility.
 * @author Mihovil Vinković
 *
 */
public class SmartScriptTester {

	/**
	 * See class description.
	 * @param args Path to the document to be parsed.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Wrong number of arguments.");
			System.exit(-1);
		}

		String docBody = "";
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])),
					StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.err.println("Couldn't open file.");
			System.exit(-1);
		}
		SmartScriptParser parser = null;

		try {
			parser = new SmartScriptParser(docBody);
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
													// content of docBod"

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String originalDocumentBody2 = createOriginalDocumentBody(document2);

		System.out.println();
		System.out.println(originalDocumentBody.equals(originalDocumentBody2));
	}

	/**
	 * Converts the tree representation of a document to a more human readable
	 * format.
	 * 
	 * @param nodeParent
	 *            The root of the document tree.
	 * @return String representation of the document.
	 */
	public static String createOriginalDocumentBody(Node nodeParent) {

		String ret = "";

		if (nodeParent != null) {
			ret += nodeParent.asText();

			int childrenNum = nodeParent.numberOfChildren();
			for (int i = 0; i < childrenNum; i++) {
				Node child = nodeParent.getChild(i);
				ret += createOriginalDocumentBody(child);
			}
		}
		if (nodeParent instanceof ForLoopNode) {
			ret += "{$END$}";
		}

		// replaces the \} values added when parsing the tree. See parse
		// function in SmartScriptParser.
		ret = ret.replaceAll("\\\\}", "}");

		return ret;
	}
}
