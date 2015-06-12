package hr.fer.zemris.java.hw13.voting;

import static org.junit.Assert.*;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class VotingDatabaseUtilityTest {

	@Test
	public void testDefinitionLoad() throws IOException {
		final String definitionPath = "web/aplikacija2/WEB-INF/glasanje-definicija.txt";
		Map<Integer, VotingDefinitionEntry> definition = VotingDatabaseUtility
				.loadDatabaseDefintion(definitionPath);

		final int entriesCount = 7;
		assertEquals(entriesCount, definition.size());

		List<String> generatedDefinition = new ArrayList<String>(entriesCount);
		for (Map.Entry<Integer, VotingDefinitionEntry> entry : definition
				.entrySet()) {
			VotingDefinitionEntry defEntry = entry.getValue();

			StringBuilder lineBuilder = new StringBuilder();
			lineBuilder.append(defEntry.getID()).append("\t")
					.append(defEntry.getBandName()).append("\t")
					.append(defEntry.getPreviewSongURL());
			generatedDefinition.add(lineBuilder.toString());
		}
		List<String> loadedDefinition = Files.readAllLines(Paths
				.get(definitionPath));

		assertEquals(loadedDefinition, generatedDefinition);
	}
}
