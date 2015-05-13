package hr.fer.zemris.java.hw2;

/**
 * Tester program for parsing and recreation of document with {$ $} tags.
 * Program gets one argument from command line: path to the document.
 * 
 * For loop tag must have a variable and 3-4 variables\numbers representing
 * start, end, and step (which is optional) of an expression
 * 
 * Empty tag consists of various numbers, variables, operators, functions or
 * strings
 * @author Borna Feldšar
 * @version 1.0
 */
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SmartScriptTester {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.exit(1);
		}
		String filepath = args[0];
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(filepath)),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("You need to enter file path!");
			System.exit(1);
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
		System.out.println(document);
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		
		System.out.println("Documents are equals:"+document.toString().equals(document2.toString()));
	}
	
	/**
	 * Creates original document body from all childrens od given node.
	 * @param node document node of document tree
	 * @return String original document body.
	 */
	private static String createOriginalDocumentBody(Node node) {
		
		return node.asText();
	}
}
