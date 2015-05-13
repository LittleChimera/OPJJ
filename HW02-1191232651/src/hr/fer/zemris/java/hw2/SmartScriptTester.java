package hr.fer.zemris.java.hw2;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;;

/**
 * Serves for testing. Accepts only one command line argument that is the path
 * to text document.
 * 
 * @author Erik Banek
 */
public class SmartScriptTester {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Wrong number of command line arguments!"
					+ "\nThere should be exactly one path to document.");
		}
		
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Wrong filepath!");
			System.exit(1);
		}
		
		//System.out.println(docBody);
		//SmartScriptParser parser = new SmartScriptParser(docBody);
		
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		
		System.out.println();
		System.out.println("This is the reconstructed document body:");
		System.out.println(originalDocumentBody); // should write something like original
		// content of docBody
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		System.out.println();
		System.out.println("Now this should be the same.");
		System.out.println(createOriginalDocumentBody(document2));
		//document2 and document1 should be structurally the same, with same tokens
		//and nodes, maybe make and isEqual method?
	}

	private static String createOriginalDocumentBody(DocumentNode document) {
		return document.generateText();
	}

}
