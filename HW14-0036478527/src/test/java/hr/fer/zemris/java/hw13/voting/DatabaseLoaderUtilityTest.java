package hr.fer.zemris.java.hw13.voting;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.java.hw13.model.PollEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DatabaseLoaderUtilityTest {

	@Test
	public void testDefinitionLoad() throws IOException {
		final String entriesPath = "web/aplikacija5/WEB-INF/polls-options.txt";
		List<PollEntry> options = DatabaseLoaderUtility
				.loadPollEntries(entriesPath);

		final int entriesCount = 11;
		assertEquals(entriesCount, options.size());

		List<String> generatedDefinition = new ArrayList<String>(entriesCount);
		for (PollEntry entry : options) {

			StringBuilder lineBuilder = new StringBuilder();
			lineBuilder.append(entry.getOptionTitle()).append("\t")
					.append(entry.getOptionLink()).append("\t")
					.append(entry.getPollId());
			generatedDefinition.add(lineBuilder.toString());
		}
		List<String> loadedDefinition = Files.readAllLines(Paths
				.get(entriesPath));

		assertEquals(loadedDefinition, generatedDefinition);
	}
}
