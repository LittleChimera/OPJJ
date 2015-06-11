package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "glasanje-glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Zabiljezi glas...
		String resultsPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		// Napravi datoteku ako je potrebno; ažuriraj podatke koji su u njoj...
		if (!Files.exists(Paths.get(resultsPath))) {
			String definitionPath = req.getServletContext().getRealPath(
					"/WEB-INF/glasanje-definicija.txt");

			VotingDatabaseUtility.createResultsFile(resultsPath,
					VotingDatabaseUtility.loadDatabaseDefintion(definitionPath)
							.keySet());
		}
		try {
			VotingDatabaseUtility.giveVoteTo(
					Integer.parseInt(req.getParameter("id")), resultsPath);
		} catch (NumberFormatException e) {
			req.setAttribute("error", e.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}
		// ...
		// ...
		// Kad je gotovo, pošalji redirect pregledniku
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

}
