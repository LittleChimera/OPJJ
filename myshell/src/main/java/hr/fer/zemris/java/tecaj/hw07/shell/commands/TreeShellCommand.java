package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * The tree command expects a single argument: directory name and prints a tree
 * of the directory.
 * 
 * @author Luka Skugor
 *
 */
public class TreeShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Tree command.
	 */
	public TreeShellCommand() {
		name = "tree";
		description
				.add("Lists all directory files recursively in tree format.");
		description.add("");
		description.add("Usage: tree [ROOT DIRECTORY]");
		description.add("");
		description.add("Examples of usage:");
		description.add("  tree examples/inner");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand#executeCommand
	 * (hr.fer.zemris.java.tecaj.hw07.shell.Environment, java.lang.String)
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argumentsList = parseArguments(arguments, true);

		if (!checkExpectedArguments(env, argumentsList, 1, 1)) {
			return ShellStatus.CONTINUE;
		}

		Path dirPath = Paths.get(argumentsList.get(0));
		if (!Files.isDirectory(dirPath)) {
			writelnToEnvironment(env,
					"Given argument is not an existing directory.");
			return ShellStatus.CONTINUE;
		}

		TreeWriter treeWriter = new TreeWriter(env);
		try {
			Files.walkFileTree(dirPath, treeWriter);
			writelnToEnvironment(
					env,
					String.format("%d directories, %d files",
							treeWriter.getDirectoryCount(),
							treeWriter.getFileCount()));
		} catch (IOException e) {
			writelnToEnvironment(env, "File not found.");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Implementation of {@link java.nio.file.FileVisitor} which prints files in
	 * tree format.
	 * 
	 * @author Luka Skugor
	 *
	 */
	private class TreeWriter implements FileVisitor<Path> {

		/**
		 * Level of line indentation.
		 */
		private int indentationLevel;
		/**
		 * Environment on which output stream files will be written.
		 */
		private Environment env;
		/**
		 * Number of visited directories.
		 */
		private int directoryCount;
		/**
		 * Number of visited files.
		 */
		private int fileCount;

		/**
		 * Creates a new TreeWriter which writes to given environment.
		 * 
		 * @param env
		 *            environment on which output tree will be written
		 */
		public TreeWriter(Environment env) {
			this.env = env;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object,
		 * java.io.IOException)
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			indentationLevel -= 2;
			return FileVisitResult.CONTINUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object,
		 * java.nio.file.attribute.BasicFileAttributes)
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			directoryCount++;
			if (indentationLevel == 0) {
				writelnToEnvironment(env, dir.getFileName().toString());
			} else {
				env.write(String.format("%" + indentationLevel + "s%s%n", "",
						dir.getFileName()));
			}
			indentationLevel += 2;
			return FileVisitResult.CONTINUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.nio.file.FileVisitor#visitFile(java.lang.Object,
		 * java.nio.file.attribute.BasicFileAttributes)
		 */
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			fileCount++;
			writelnToEnvironment(
					env,
					String.format("%" + indentationLevel + "s%s", "",
							file.getFileName()));
			return FileVisitResult.CONTINUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object,
		 * java.io.IOException)
		 */
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Gets visited directory count.
		 * 
		 * @return directory count
		 */
		public int getDirectoryCount() {
			return directoryCount;
		}

		/**
		 * Gets visited file count.
		 * 
		 * @return file count
		 */
		public int getFileCount() {
			return fileCount;
		}
	}

}
