package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Program which enables querying through command line for existing student database.
 * @author Luka Škugor
 *
 */
public class StudentDB {
	
	/**
	 * Quit command (case insensitive).
	 */
	private static final String QUIT_COMMAND = "quit";
	/**
	 * Query command (case insensitive).
	 */
	private static final String QUERY_COMMAND = "query";
	/**
	 * Database with student records.
	 */
	private static StudentDatabase studentDatabase;
	
	/**
	 * Runs on program start.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			List<String> lines = Files.readAllLines(
					Paths.get("./database.txt"),
					StandardCharsets.UTF_8
					);
			studentDatabase = new StudentDatabase(lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while (true) {
			System.out.print("> ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String s = br.readLine();
				if (s.trim().equalsIgnoreCase(QUIT_COMMAND)) {
					break;
				} else if (s.trim().matches(String.format("^(?i)%s.*", QUERY_COMMAND))) {
					QueryCommand command = new QueryCommand(s, studentDatabase);
					System.out.print(command.formatQueryResult());
				} else {
					System.out.println("Unknown command.");
				}
			} catch (IOException | IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} 		
		}
		
	}
	

}
