package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Ls command writes a directory listing (not recursive). It takes a single
 * argument - directory. The output consists of 4 columns. First column
 * indicates if current object is directory (d), readable (r), writable (w)
 * and executable (x). Second column contains object size in bytes that is right
 * aligned and occupies 10 characters. Follows file creation date/time and
 * finally file name.
 * 
 * @author Luka Skugor
 *
 */
public class LsShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new Ls command.
	 */
	public LsShellCommand() {
		name = "ls";
		description
				.add("List information about the FILEs in the ROOT_DIRECTORY.");
		description.add("");
		description.add("Usage: ls [ROOT_DIRECTORY]");
		description.add("");
		description.add("Sorts entries alphabetically by FILE name.");
		description.add("");
		description.add("Examples of usage:");
		description.add("  ls examples/innerdir");
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
			writelnToEnvironment(env, "Given argument is not a directory.");
			return ShellStatus.CONTINUE;
		}

		List<Path> dirFilesList = new LinkedList<Path>();
		try (DirectoryStream<Path> directoryStream = Files
				.newDirectoryStream(dirPath)) {
			for (Path path : directoryStream) {
				dirFilesList.add(path);
			}
		} catch (IOException e) {
			writelnToEnvironment(env, "Directory not found.");
			return ShellStatus.CONTINUE;
		}

		Collections.sort(dirFilesList);
		for (Path path : dirFilesList) {
			try {
				env.writeln(formatLine(path));
			} catch (Exception e) {
				System.err.println("Output error.");
				System.exit(1);
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Formats file creation date (i.e. third column).
	 * @param path file path
	 * @return formatted creation date
	 */
	private String formatCreationDate(Path path) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(path,
				BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = null;

		try {
			attributes = faView.readAttributes();
		} catch (IOException e) {
			return sdf.toPattern().replaceAll("[^-]", ".");
		}
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

		return formattedDateTime;
	}

	/**
	 * Formats file properties (i.e. first column).
	 * @param path file path
	 * @return formatted properties
	 */
	private String formatFileProperties(Path path) {
		/*
		 * Properties are as followed: d - is file a directory r - is file
		 * readable w - is file writable x - is file executable if file doesn't
		 * satisfies a property a '-' is put instead
		 */
		char[] properties = new char[4];
		Arrays.fill(properties, '-');
		if (Files.isDirectory(path)) {
			properties[0] = 'd';
		}
		if (Files.isReadable(path)) {
			properties[1] = 'r';
		}
		if (Files.isWritable(path)) {
			properties[2] = 'w';
		}
		if (Files.isExecutable(path)) {
			properties[3] = 'x';
		}

		return new String(properties);
	}

	/**
	 * Formats file line.
	 * @param path file path
	 * @return formatted properties
	 */
	private String formatLine(Path path) {
		String fileName = path.getFileName().toString();
		long fileSize = 0;
		try {
			fileSize = Files.size(path);
		} catch (IOException | SecurityException e) {
			fileSize = -1;
		}
		String fileProperties = formatFileProperties(path);
		String fileCreationDate = formatCreationDate(path);

		return String.format("%s %10d %s %s", fileProperties, fileSize,
				fileCreationDate, fileName);
	}

}
