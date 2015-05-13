package hr.fer.zemris.java.tecaj6;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class Lister3 {

	private static class IndentiraniIspis implements FileVisitor<Path> {

		private int indentLevel;

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			if (indentLevel == 0) {
				System.out.println(dir.getFileName());
			} else {
				System.out.format("%" + indentLevel + "s%s%n", "",
						dir.getFileName());
			}

			indentLevel += 2;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			System.out.format("%" + indentLevel + "s%s%n", "", file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			indentLevel -= 2;
			return FileVisitResult.CONTINUE;
		}

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Expected one argument!");
			System.exit(1);
		}
		String path = args[0];

		Path root = Paths.get(path);
		if (!Files.isDirectory(root)) {
			System.out.println("Argument should be a directory.");
			System.exit(2);
		}
		if (!Files.exists(root)) {
			System.out.println("Given file doesn't exist");
			System.exit(3);
		}
		//System.out.println(root.getAbsolutePath());
		Files.walkFileTree(root, new IndentiraniIspis());

	}

}
