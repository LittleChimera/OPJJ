package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * QueryCommand performs a query on a StudentDatabase. It then offers to format
 * and print result on system output.
 * 
 * @author Luka Škugor
 *
 */
public class QueryCommand {

	/**
	 * Result of the query.
	 */
	private List<StudentRecord> result;

	/**
	 * Performs a query on the student database and remembers all results
	 * satisfying the query.
	 * 
	 * @param query
	 *            string query
	 * @param database
	 *            student database
	 * @throws IllegalArgumentException
	 *             if query is invalid
	 */
	public QueryCommand(String query, StudentDatabase database) {
		String conditionsString = query.replaceAll("^(?i)query", "");
		if (conditionsString.length() == query.length()) {
			throw new IllegalArgumentException(
					"Query must start with \"query\"");
		}
		QueryFilter queryFilter = null;
		try {
			queryFilter = new QueryFilter(conditionsString);			
		} catch (NoSuchElementException e) {
			result = new LinkedList<StudentRecord>();
			return;
		}

		Optional<String> studentJMBAG = queryFilter.getJMBAG();
		if (studentJMBAG.isPresent()) {
			StudentRecord record = database.forJMBAG(studentJMBAG.get());
			result = new LinkedList<StudentRecord>();

			if (queryFilter.accepts(record)) {
				result.add(record);
			}
		} else {
			result = database.filter(queryFilter);
		}
	}

	/**
	 * Gets formatted string result of the query.
	 * @return formatted query result.
	 */
	public String formatQueryResult() {
		StringBuilder formattedOutputBuilder = new StringBuilder();
		if (result.size() == 0) {
			return "";
		}
		// cell widths for values in this order: JMBAG, Last Name, First Name,
		// Final Grade
		int[] cellWidths = new int[4];
		Arrays.fill(cellWidths, 0);
		for (StudentRecord studentRecord : result) {
			String[] recordData = new String[4];
			recordData[0] = studentRecord.getJMBAG();
			recordData[1] = studentRecord.getLastName();
			recordData[2] = studentRecord.getFirstName();
			recordData[3] = Integer.toString(studentRecord.getFinalGrade());

			for (int i = 0; i < cellWidths.length; i++) {
				int currentCellWidth = recordData[i].length();
				if (currentCellWidth > cellWidths[i]) {
					cellWidths[i] = currentCellWidth;
				}
			}
		}
		StringBuilder headerBuilder = new StringBuilder();
		for (int width : cellWidths) {
			headerBuilder.append("+");
			headerBuilder.append(new String(new char[width + 2]).replace('\0',
					'='));
		}
		headerBuilder.append("+\n");
		String headerAndFooter = headerBuilder.toString();

		formattedOutputBuilder.append(headerAndFooter);
		for (StudentRecord studentRecord : result) {
			formattedOutputBuilder.append(String.format("| %-" + cellWidths[0]
					+ "s | " + "%-" + cellWidths[1] + "s | " + "%-"
					+ cellWidths[2] + "s | " + "%-" + cellWidths[3] + "s |%n",
					studentRecord.getJMBAG(), studentRecord.getLastName(),
					studentRecord.getFirstName(),
					Integer.toString(studentRecord.getFinalGrade())));
		}
		formattedOutputBuilder.append(headerAndFooter);
		return formattedOutputBuilder.toString();
	}
}
