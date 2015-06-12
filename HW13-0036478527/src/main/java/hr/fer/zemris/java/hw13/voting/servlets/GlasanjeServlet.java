package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GlasanjeServlet loads definition of voting database and dispatches it to JSP
 * which generates a page from which can be voted.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {
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

		String fileName = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");

		Collection<VotingDefinitionEntry> definition = VotingDatabaseUtility
				.loadDatabaseDefintion(fileName).values();

		req.setAttribute("definition", definition);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(
				req, resp);

	}
}
