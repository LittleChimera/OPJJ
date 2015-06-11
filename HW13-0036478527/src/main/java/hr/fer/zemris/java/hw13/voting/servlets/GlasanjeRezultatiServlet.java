package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingResultEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "glasanje-rezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String definitionPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		Map<Integer, VotingDefinitionEntry> definition = VotingDatabaseUtility
				.loadDatabaseDefintion(definitionPath);
		req.setAttribute("definition", definition);

		// Pročitaj rezultate iz /WEB-INF/glasanje-rezultati.txt
		String resultsPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		// Napravi datoteku ako je potrebno; inače je samo pročitaj...
		if (!Files.exists(Paths.get(resultsPath))) {
			VotingDatabaseUtility.createResultsFile(resultsPath,
					definition.keySet());
		}
		Collection<VotingResultEntry> results = VotingDatabaseUtility
				.getResults(resultsPath);
		req.setAttribute("results", results);

		// ...
		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,
				resp);
	}

}
