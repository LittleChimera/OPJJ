package hr.fer.zemris.java.hw13.voting;

import hr.fer.zemris.java.hw13.model.PollDefinition;
import hr.fer.zemris.java.hw13.model.PollEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading database definitions from files.
 * @author Luka Skugor
 *
 */
public class DatabaseLoaderUtility {

	/**
	 * Reads poll options from given file.
	 * @param entriesPath entries file path
	 * @return list of read poll options
	 * @throws IOException if IOException occurs
	 */
	public static List<PollEntry> loadPollEntries(
			String entriesPath) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get(entriesPath));
		List<PollEntry> entries = new ArrayList<PollEntry>(lines.size());

		for (String line : lines) {
			String[] entryArray = line.split("\\t");
			
			PollEntry entry = new PollEntry();
			entry.setOptionTitle(entryArray[0]);
			entry.setOptionLink(entryArray[1]);
			entry.setPollId(Long.parseLong(entryArray[2]));
			entry.setVotesCount(Long.valueOf(0));
			
			entries.add(entry);
		}
		return entries;
	}

	/**
	 * Reads poll definitions from given file.
	 * @param definitionPath definitions file path
	 * @return list of read poll definitions
	 * @throws IOException if IOException occurs
	 */
	public static List<PollDefinition> loadPolls(
			String definitionPath) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get(definitionPath));
		List<PollDefinition> entries = new ArrayList<PollDefinition>(lines.size());

		for (String line : lines) {
			
			String[] entryArray = line.split("\\t");
			
			PollDefinition definition = new PollDefinition();
			definition.setId(Long.parseLong(entryArray[0]));
			definition.setTitle(entryArray[1]);
			definition.setMessage(entryArray[2]);
			entries.add(definition);
		}
		return entries;
	}

}
