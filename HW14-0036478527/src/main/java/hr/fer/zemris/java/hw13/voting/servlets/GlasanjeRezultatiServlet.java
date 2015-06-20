package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.dao.DAOProvider;
import hr.fer.zemris.java.hw13.model.PollEntry;
import hr.fer.zemris.java.hw13.voting.VotingHelper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GlasanjeRezultatiServlet gets voting results and send them to JSP which
 * generates a result page.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "glasanje-rezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

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
		
		Long pollId = VotingHelper.verifyId("pollId", req, resp);
		if (pollId == null) {
			return;
		}
		
		List<PollEntry> results = DAOProvider.getDao().getPollEntries(pollId);
		req.setAttribute("results", results);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,
				resp);
	}

}
