package hr.fer.zemris.java.tecaj6;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Brojanje {
	
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
		List<File> allJavaFiles = new LinkedList<File>();
		System.out.println(root.getAbsolutePath());
		branch(root, allJavaFiles);
		
		long length = 0;
		for (File file : allJavaFiles) {
			length += file.length();
		}
		System.out.println(allJavaFiles.size());
		System.out.println(length);
	}
	
	private static void branch(File root, List<File> fileList) {
		for (File file : root.listFiles()) {
			if (file.getName().toLowerCase().endsWith(".java")) {
				fileList.add(file);
			}
			if (file.isDirectory()) {
				branch(file, fileList);
			}
		}
	}

	
	/*public static void main(String[] args) {
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
		List<File> allJavaFiles = new LinkedList<File>();
		
		Lister.branchAndConsume(root, 0, new CounterConsumer(allJavaFiles, 0));
	}
	
	public class CounterConsumer implements Consumer<File> {
		
		private List<File> fileList;
		
		public CounterConsumer(List<File> fileList, int level) {
			this.fileList = fileList;
		}

		@Override
		public void accept(File t) {
			if (t.getName().matches(".*\\.java$")) {
				fileList.add(t);
			}
		}
		
	}*/
}
