package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Parses a Smart Script using a {@link SmartScriptParser} and recreates the
 * parsed document by printing it to standard output. Program takes a single
 * argument - a smart scripts which will go through the process.
 *
 * @author Luka Skugor
 *
 */
public class TreeWriter {

	/**
	 * Called on program start.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Expected a single file path.");
			return;
		}

		String docBody;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])));
		} catch (IOException e) {
			System.err.println("No such file: " + args[0]);
			return;
		}
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

	/**
	 * Recreates a document and writes it to standard output.
	 *
	 * @see INodeVisitor
	 *
	 * @author Luka Skugor
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitTextNode
		 * (hr.fer.zemris.java.custom.scripting.nodes.TextNode)
		 */
		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.toString());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitForLoopNode
		 * (hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode)
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node);
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
			System.out.println("{$END$}");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitEchoNode
		 * (hr.fer.zemris.java.custom.scripting.nodes.EchoNode)
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node.toString());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitDocumentNode
		 * (hr.fer.zemris.java.custom.scripting.nodes.DocumentNode)
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

	}
}
