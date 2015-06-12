package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GlasanjeGlasajServlet enables voting for a band. Voting is done by giving
 * band's ID through GET request's parameter(id).
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "glasanje-glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String resultsPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
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
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

}
