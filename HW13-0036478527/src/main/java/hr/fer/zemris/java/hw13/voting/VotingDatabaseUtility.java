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

public class VotingDatabaseUtility {

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

	public static synchronized void createResultsFile(String resultsPath,
			Collection<Integer> definition) throws IOException {

		if (Files.exists(Paths.get(resultsPath))) {
			return;
		}

		storeResults(resultsPath, definition, new HashMap<>());
	}

	public static synchronized void giveVoteTo(int entryID, String resultsPath)
			throws IOException {

		Map<Integer, VotingResultEntry> results = loadResults(resultsPath);
		results.get(entryID).votes++;
		storeResults(resultsPath, results.keySet(), results);
	}

	private static void storeResults(String resultsPath,
			Collection<Integer> IDs, Map<Integer, VotingResultEntry> results)
			throws IOException {

		StringBuilder resultsBuilder = new StringBuilder();
		for (Integer ID : IDs) {
			resultsBuilder.append(ID).append("\t")
					.append(results.containsKey(ID) ? results.get(ID).votes : 0)
					.append("\n");
		}
		Files.write(Paths.get(resultsPath),
				resultsBuilder.toString().getBytes(StandardCharsets.UTF_8));
	}

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

	public static class VotingDefinitionEntry {

		private int ID;
		private String bandName;
		private String previewSongURL;

		public VotingDefinitionEntry(int ID, String bandName, String previewSong) {
			super();
			this.ID = ID;
			this.bandName = bandName;
			this.previewSongURL = previewSong;
		}

		public String getBandName() {
			return bandName;
		}

		public int getID() {
			return ID;
		}

		public String getPreviewSongURL() {
			return previewSongURL;
		}

	}

	public static class VotingResultEntry {

		private int ID;
		private int votes;

		public VotingResultEntry(int iD, int votes) {
			super();
			ID = iD;
			this.votes = votes;
		}

		public int getVotes() {
			return votes;
		}

		public int getID() {
			return ID;
		}

	}

}
