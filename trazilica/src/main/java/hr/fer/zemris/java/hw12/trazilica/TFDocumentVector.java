package hr.fer.zemris.java.hw12.trazilica;

import hr.fer.zemris.java.hw12.trazilica.DocumentLibrary.IdfValues;
import hr.fer.zemris.linearna.Vector;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TFDocumentVector {

	private Map<String, Integer> wordsFrequencies;

	public TFDocumentVector(String[] words) {
		wordsFrequencies = new HashMap<String, Integer>();
		for (String word : words) {
			Integer value = wordsFrequencies.get(word);
			wordsFrequencies.put(word, value != null ? ++value : 1);
		}
	}

	public int getWordFrequency(String word) {
		Integer value = wordsFrequencies.get(word);
		return value != null ? value : 0;
	}

	public boolean containsWord(String word) {
		return wordsFrequencies.containsKey(word);
	}

	public double calcTfIdfSimilarity(TFDocumentVector other,
			IdfValues idfValues) {
		Set<String> vocabulary = new LinkedHashSet<String>();
		vocabulary.addAll(this.wordsFrequencies.keySet());
		vocabulary.addAll(other.wordsFrequencies.keySet());

		return getTfIdfVector(vocabulary, idfValues).cosine(
				other.getTfIdfVector(vocabulary, idfValues));
	}

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
