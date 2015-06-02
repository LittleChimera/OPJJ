package hr.fer.zemris.java.hw12.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DocumentLibrary {

	private static final Path LIBRARY_PATH = Paths.get("clanci");
	private static final Path STOP_WORDS_PATH = Paths.get("hr_stopRijeci.txt");
	private static List<String> stopWords;

	private Set<String> vocabulary;
	private Map<Path, TFDocumentVector> tfDocumentVectors;

	public class IdfValues {
		private Map<String, Double> values = new HashMap<String, Double>();

		public double getIdfValue(String word) {
			return values.get(word);
		}
	}

	IdfValues idfValues = new IdfValues();

	private DocumentLibrary() {
		tfDocumentVectors = new HashMap<Path, TFDocumentVector>();
		vocabulary = new HashSet<String>();
		
		long start = System.currentTimeMillis();

		try {
			stopWords = Files.readAllLines(STOP_WORDS_PATH);
			Files.walkFileTree(LIBRARY_PATH, new DocumentLoader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Vocabulary size: " + vocabulary.size());

		for (String word : vocabulary) {
			idfValues.values.put(word, calcIdf(word));
		}
		
		System.out.println("Init time: " + (System.currentTimeMillis() - start));
	}

	private static DocumentLibrary instance = new DocumentLibrary();

	private class DocumentLoader extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			String document = new String(Files.readAllBytes(file),
					StandardCharsets.UTF_8);
			String[] words = document.toLowerCase().split("[^\\p{L}]+");
			List<String> wordsList = new ArrayList<String>(Arrays.asList(words));

			wordsList.removeAll(stopWords);

			vocabulary.addAll(wordsList);
			tfDocumentVectors.put(
					file,
					new TFDocumentVector(wordsList.toArray(new String[wordsList
							.size()])));
			return FileVisitResult.CONTINUE;
		}
	}

	public static DocumentLibrary getInstance() {
		return instance;
	}

	public class QueryResult {
		Double similarity;
		Path filePath;

		public QueryResult(Double similarity, Path filePath) {
			super();
			this.similarity = similarity;
			this.filePath = filePath;
		}
	}

	public Set<QueryResult> getQueryResults(int resultCount, String... words) {
		TFDocumentVector request = new TFDocumentVector(words);
		Comparator<QueryResult> bySimilarity = (qr1, qr2) -> {
			return qr1.similarity.compareTo(qr2.similarity);
		};
		Comparator<QueryResult> byPath = (qr1, qr2) -> {
			return qr1.filePath.compareTo(qr2.filePath);
		};

		TreeSet<QueryResult> results = new TreeSet<QueryResult>(bySimilarity
				.reversed().thenComparing(byPath));

		for (Entry<Path, TFDocumentVector> documentVector : tfDocumentVectors
				.entrySet()) {
			double similarity = documentVector.getValue().calcTfIdfSimilarity(
					request, idfValues);
			if (Math.abs(similarity) < 1e-9) {
				continue;
			}
			results.add(new QueryResult(similarity, documentVector.getKey()));
		}

		return results;
	}

	private double calcIdf(String word) {
		// number of documents containing the word
		long containgWord = tfDocumentVectors.values().stream()
				.filter(dv -> dv.containsWord(word)).count();
		return Math.log(tfDocumentVectors.size() / containgWord);
	}
}
