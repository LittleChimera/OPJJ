package hr.fer.zemris.java.hw12.trazilica;

import hr.fer.zemris.java.hw12.trazilica.DocumentLibrary.QueryResult;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

/**
 * Trazilica is a prompt which enables searching documents with given words. 10
 * documents which have highest similarity will be returned from a query.
 * 
 * For querying, use command "query". E.g. query
 * "something else is interesting".
 * 
 * For getting results of the last query type "results".
 * 
 * Program exits by type "exit" in the prompt.
 * 
 * @author Luka Skugor
 *
 */
public class Konzola {

	/**
	 * Called on program start. Initializes prompt.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		Set<QueryResult> results = null;
		final int resultsCount = 10;

		Scanner scanner = new Scanner(System.in);
		String line;
		Query: while (true) {
			System.out.print("Enter command > ");

			line = scanner.nextLine();
			String[] commandSplit = line.split(" ", 2);
			String command = commandSplit[0];
			String[] arguments = (commandSplit.length > 1) ? commandSplit[1]
					.split("\\s+") : new String[0];

			switch (command) {
			case "query":
				results = DocumentLibrary.getInstance().getQueryResults(
						resultsCount, arguments);
				System.out.println("Query is " + Arrays.toString(arguments));
				System.out.println("Best 10 results:");

			case "results":
				if (results == null) {
					System.out.println("No query has been made");
					break;
				}
				for (QueryResult result : results) {
					System.out.format("(%.4f) %s%n", result.similarity,
							result.filePath);
				}
				break;

			case "exit":
				break Query;

			default:
				System.out.println("Unkown command: " + command);
				break;
			}
		}

		scanner.close();
	}

}
