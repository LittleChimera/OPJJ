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

/**
 * DocumentLibrary is a library of documents which can be searched with
 * {@link Konzola} program. It can only have a single instance which is get via
 * {@link #getInstance()}.
 * 
 * @author Luka Skugor
 *
 */
public class DocumentLibrary {

	/**
	 * Folder where library files are located.
	 */
	private static final Path LIBRARY_PATH = Paths.get("clanci");
	/**
	 * Path of the file where stop words are located.
	 */
	private static final Path STOP_WORDS_PATH = Paths.get("hr_stopRijeci.txt");
	/**
	 * List of stop words.
	 */
	private static List<String> stopWords;

	/**
	 * Vocabulary of all documents in the library.
	 */
	private Set<String> vocabulary;
	/**
	 * Map of all documents. Key of the map is document's full path and value is
	 * their TF-vector.
	 */
	private Map<Path, TFDocumentVector> tfDocumentVectors;

	/**
	 * IdfValues represents a function for calculating and IDF value of pair
	 * (word,library). This library is used as is used as function argument and
	 * other is provided in {@link #getIdfValue(String)}.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public class IdfValues {
		/**
		 * Map of IDF values for each word in this library.
		 */
		private Map<String, Double> values = new HashMap<String, Double>();

		/**
		 * Gets IDF value for requested word in this library.
		 * 
		 * @param word
		 *            requested word
		 * @return IDF value of requested word
		 */
		public double getIdfValue(String word) {
			return values.containsKey(word) ? values.get(word) : 0;
		}
	}

	/**
	 * IDF values of this library.
	 */
	IdfValues idfValues = new IdfValues();

	/**
	 * Creates a new instance of DocumentLibrary. Used to create a singleton.
	 */
	private DocumentLibrary() {
		tfDocumentVectors = new HashMap<Path, TFDocumentVector>();
		vocabulary = new HashSet<String>();

		try {
			stopWords = Files.readAllLines(STOP_WORDS_PATH);
			Files.walkFileTree(LIBRARY_PATH, new DocumentLoader());
		} catch (IOException e) {
			System.out.println("Library reading error");
			System.exit(1);
		}

		System.out.println("Vocabulary size: " + vocabulary.size());

		for (String word : vocabulary) {
			idfValues.values.put(word, calcIdf(word));
		}

	}

	/**
	 * Singleton instance of DocumentLibrary.
	 */
	private static DocumentLibrary instance = new DocumentLibrary();

	/**
	 * DocumentLoader is a FileVisotr which adds all files in
	 * {@link DocumentLibrary#LIBRARY_PATH} and all its subfolders to this
	 * library.
	 * 
	 * @author Luka Skugor
	 *
	 */
	private class DocumentLoader extends SimpleFileVisitor<Path> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object,
		 * java.nio.file.attribute.BasicFileAttributes)
		 */
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

	/**
	 * Gets instance of DocumentLibrary.
	 * 
	 * @return instance of DocumentLibrary
	 */
	public static DocumentLibrary getInstance() {
		return instance;
	}

	/**
	 * This class represents a single query result which are given to
	 * {@link Konzola}.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public class QueryResult {
		/**
		 * Similarity of the document and the query.
		 */
		Double similarity;
		/**
		 * Path of the result file.
		 */
		Path filePath;

		/**
		 * Creates a new QueryResult with given parameters.
		 * 
		 * @param similarity
		 *            similarity of the document and query
		 * @param filePath
		 *            path of the result file
		 */
		public QueryResult(Double similarity, Path filePath) {
			super();
			this.similarity = similarity;
			this.filePath = filePath;
		}
	}

	/**
	 * Gets a set of maximum <code>resultCount</code> {@link QueryResult}s.
	 * 
	 * @param resultCount
	 *            maximum number of returned results
	 * @param words
	 *            query words
	 * @return results
	 */
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
			if (Math.abs(similarity) > 1e-9) {
				results.add(new QueryResult(similarity, documentVector.getKey()));
			}
		}

		return results;
	}

	/**
	 * Calculates IDFValue of a word for this library.
	 * 
	 * @param word
	 *            word which IDF will be calculated
	 * @return calculated IDF
	 */
	private double calcIdf(String word) {
		// number of documents containing the word
		long containgWord = tfDocumentVectors.values().stream()
				.filter(dv -> dv.containsWord(word)).count();
		return Math.log(tfDocumentVectors.size() / containgWord);
	}
}
