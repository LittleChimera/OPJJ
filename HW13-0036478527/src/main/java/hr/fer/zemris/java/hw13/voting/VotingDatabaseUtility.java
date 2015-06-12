package hr.fer.zemris.java.hw13.voting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * VotingDatabaseUtility is a utility for loading voting definition and
 * loading/storing results. It ensures thread-safe reading and writing of voting
 * results file.
 * 
 * @author Luka Skugor
 *
 */
public class VotingDatabaseUtility {

	/**
	 * Gets voting results for given path to results file.
	 * 
	 * @param resultsPath
	 *            path of file where results are written
	 * @return loaded results
	 * @throws IOException
	 *             if IOExcepiton occurs
	 */
	public static synchronized Collection<VotingResultEntry> getResults(
			String resultsPath) throws IOException {

		Collection<VotingResultEntry> results = loadResults(resultsPath)
				.values();

		final Comparator<VotingResultEntry> sortByVotesDesc = (re1, re2) -> re2.votes
				- re1.votes;
		final Comparator<VotingResultEntry> sortByIDAsc = (re1, re2) -> re2.ID
				- re1.ID;
		Set<VotingResultEntry> sortedResults = new TreeSet<>(
				sortByVotesDesc.thenComparing(sortByIDAsc));

		sortedResults.addAll(results);
		return sortedResults;
	}

	/**
	 * Creates results file for given results destination and voting definition.
	 * 
	 * @param resultsPath
	 *            destination path where results will be stored
	 * @param definition
	 *            voting definition
	 * @throws IOException
	 *             if IOException occurs
	 */
	public static synchronized void createResultsFile(String resultsPath,
			Collection<Integer> definition) throws IOException {

		if (Files.exists(Paths.get(resultsPath))) {
			return;
		}

		storeResults(resultsPath, definition, new HashMap<>());
	}

	/**
	 * Gives a single vote to the band with given ID.
	 * 
	 * @param entryID
	 *            ID of the band in the voting definition
	 * @param resultsPath
	 *            path to the file where voting results are stored
	 * @throws IOException
	 *             if IOException occurs
	 */
	public static synchronized void giveVoteTo(int entryID, String resultsPath)
			throws IOException {

		Map<Integer, VotingResultEntry> results = loadResults(resultsPath);
		results.get(entryID).votes++;
		storeResults(resultsPath, results.keySet(), results);
	}

	/**
	 * Stores voting results.
	 * 
	 * @param resultsPath
	 *            destination results path
	 * @param IDs
	 *            collection of all voting IDs
	 * @param results
	 *            collection of voting results entries
	 * @throws IOException
	 *             if IOException occurs
	 */
	private static void storeResults(String resultsPath,
			Collection<Integer> IDs, Map<Integer, VotingResultEntry> results)
			throws IOException {

		StringBuilder resultsBuilder = new StringBuilder();
		for (Integer ID : IDs) {
			resultsBuilder
					.append(ID)
					.append("\t")
					.append(results.containsKey(ID) ? results.get(ID).votes : 0)
					.append("\n");
		}
		Files.write(Paths.get(resultsPath),
				resultsBuilder.toString().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Loads map of voting results. Map's keys are bands' IDs.
	 * 
	 * @param resultsPath
	 *            path of the file where results are stored
	 * @return voting results
	 * @throws IOException
	 *             if IOException occurs
	 */
	private static Map<Integer, VotingResultEntry> loadResults(
			String resultsPath) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get(resultsPath));
		Map<Integer, VotingResultEntry> entries = new HashMap<>();

		for (String line : lines) {
			String[] entryArray = line.split("\\t");
			Integer ID = Integer.parseInt(entryArray[0]);
			entries.put(ID,
					new VotingResultEntry(ID, Integer.parseInt(entryArray[1])));
		}
		return entries;
	}

	/**
	 * Loads voting database's definition.
	 * 
	 * @param definitionPath
	 *            path of the file where definition is stored
	 * @return loaded definition
	 * @throws IOException
	 *             if IOException occurs
	 */
	public static Map<Integer, VotingDefinitionEntry> loadDatabaseDefintion(
			String definitionPath) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get(definitionPath));
		Map<Integer, VotingDefinitionEntry> entries = new HashMap<>();

		for (String line : lines) {
			String[] entryArray = line.split("\\t");
			Integer ID = Integer.parseInt(entryArray[0]);
			entries.put(ID, new VotingDefinitionEntry(ID, entryArray[1],
					entryArray[2]));
		}
		return entries;
	}

	/**
	 * This class represents a single entry in voting database's definition.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public static class VotingDefinitionEntry {

		/**
		 * Entry ID.
		 */
		private int ID;
		/**
		 * Entry's band.
		 */
		private String bandName;
		/**
		 * Entry's band's preview song URL.
		 */
		private String previewSongURL;

		/**
		 * Creates a new VotingDefinitionEntry with given parameters.
		 * 
		 * @param ID
		 *            entry ID
		 * @param bandName
		 *            entry's band
		 * @param previewSong
		 *            entry's band's preview song URL
		 */
		public VotingDefinitionEntry(int ID, String bandName, String previewSong) {
			super();
			this.ID = ID;
			this.bandName = bandName;
			this.previewSongURL = previewSong;
		}

		/**
		 * Gets entry's band name.
		 * 
		 * @return entry's bands name
		 */
		public String getBandName() {
			return bandName;
		}

		/**
		 * Gets entry's ID.
		 * 
		 * @return entry's ID
		 */
		public int getID() {
			return ID;
		}

		/**
		 * Gets entry's band's preview song URL.
		 * 
		 * @return entry's band's preview song URL
		 */
		public String getPreviewSongURL() {
			return previewSongURL;
		}

	}

	/**
	 * This class represents a single entry in voting database's results.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public static class VotingResultEntry {

		/**
		 * ID of the entry of the voting database's definition to which votes
		 * are mapped.
		 */
		private int ID;
		/**
		 * Entry's votes count.
		 */
		private int votes;

		/**
		 * Creates a new VotingResultEntry with given parameters.
		 * 
		 * @param ID
		 *            ID of the entry to which votes are mapped
		 * @param votes
		 *            entry's votes count
		 */
		public VotingResultEntry(int ID, int votes) {
			super();
			this.ID = ID;
			this.votes = votes;
		}

		/**
		 * Gets votes count.
		 * 
		 * @return votes count
		 */
		public int getVotes() {
			return votes;
		}

		/**
		 * Gets entry's ID.
		 * 
		 * @return entry's ID
		 */
		public int getID() {
			return ID;
		}

	}

}
