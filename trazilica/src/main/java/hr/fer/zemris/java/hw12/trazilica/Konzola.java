package hr.fer.zemris.java.hw12.trazilica;

import hr.fer.zemris.java.hw12.trazilica.DocumentLibrary.QueryResult;

import java.util.Set;

public class Konzola {

	public static void main(String[] args) {
		Set<QueryResult> results = DocumentLibrary.getInstance()
				.getQueryResults(10, "darovit", "glumac", "zadnje",
						"akademske", "klase");
		/*results = DocumentLibrary.getInstance()
				.getQueryResults(10, "predstava", "nogomet", "utakmica");*/
		for (QueryResult result : results) {
			System.out.format("(%.4f) %s%n", result.similarity, result.filePath);
		}
	}

}
