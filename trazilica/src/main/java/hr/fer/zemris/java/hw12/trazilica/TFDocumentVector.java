package hr.fer.zemris.java.hw12.trazilica;

import hr.fer.zemris.java.hw12.trazilica.DocumentLibrary.IdfValues;
import hr.fer.zemris.linearna.Vector;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a TF-vector of a document.
 * 
 * @author Luka Skugor
 *
 */
public class TFDocumentVector {

	/**
	 * Map of frequencies of each word in a document.
	 */
	private Map<String, Integer> wordsFrequencies;

	/**
	 * Creates a new TF-vector from given array of words which documents
	 * contains.
	 * 
	 * @param words
	 *            words of the document
	 */
	public TFDocumentVector(String[] words) {
		wordsFrequencies = new HashMap<String, Integer>();
		for (String word : words) {
			Integer value = wordsFrequencies.get(word);
			wordsFrequencies.put(word, value != null ? ++value : 1);
		}
	}

	/**
	 * Gets frequency of a word.
	 * @param word requested word
	 * @return word's frequency
	 */
	public int getWordFrequency(String word) {
		Integer value = wordsFrequencies.get(word);
		return value != null ? value : 0;
	}

	/**
	 * Checks if document contains a word.
	 * @param word requested word
	 * @return true if document contains requested word, otherwise false
	 */
	public boolean containsWord(String word) {
		return wordsFrequencies.containsKey(word);
	}

	/**
	 * Calculates TF-IDF similarity (number in [0,1]) of this document with another for given IDF values of a library.
	 * @param other comparing document
	 * @param idfValues IDF values of a library in which document is contained
	 * @return calculated TF-IDf similarity
	 */
	public double calcTfIdfSimilarity(TFDocumentVector other,
			IdfValues idfValues) {
		Set<String> vocabulary = new LinkedHashSet<String>();
		vocabulary.addAll(this.wordsFrequencies.keySet());
		vocabulary.addAll(other.wordsFrequencies.keySet());

		return getTfIdfVector(vocabulary, idfValues).cosine(
				other.getTfIdfVector(vocabulary, idfValues));
	}

	/**
	 * Gets TF-IDF vector for the given vocabulary and IDF values.
	 * @param vocabulary all words which are contained in union of two documents which are compared
	 * @param idfValues library's IDF values
	 * @return TF-IDF vector of the document
	 */
	private Vector getTfIdfVector(Set<String> vocabulary, IdfValues idfValues) {
		double[] vectorValues = new double[vocabulary.size()];
		int counter = 0;
		for (String word : vocabulary) {
			vectorValues[counter] = getWordFrequency(word)
					* idfValues.getIdfValue(word);
			++counter;
		}
		return new Vector(vectorValues);
	}
}
