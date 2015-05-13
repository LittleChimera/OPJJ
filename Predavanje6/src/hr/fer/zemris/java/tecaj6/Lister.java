package hr.fer.zemris.java.tecaj6;

import java.io.File;


public class Lister {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected one argument!");
			System.exit(1);
		}
		String path = args[0];
		
		File root = new File(path);
		if (!root.isDirectory()) {
			System.out.println("Argument should be a directory.");
			System.exit(2);
		}
		if (!root.exists()) {
			System.out.println("Given file doesn't exist");
			System.exit(3);
		}
		System.out.println(root.getAbsolutePath());
		branchAndPrint(root, 0);
		
	}
	
	private static void branchAndPrint(File root, int level) {
		String indentation = generateIndentation(level);
		for (File file : root.listFiles()) {
			System.out.println(indentation + file.getName());
			if (file.isDirectory()) {
				branchAndPrint(file, level + 1);
			}
		}
	}
	
	private static String generateIndentation(int n) {
		StringBuilder indentationBuilder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			indentationBuilder.append("  ");
		}
		
		return indentationBuilder.toString();
	}
}
