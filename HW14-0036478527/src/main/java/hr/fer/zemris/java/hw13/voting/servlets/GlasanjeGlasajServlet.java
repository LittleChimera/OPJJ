package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.dao.DAOProvider;
import hr.fer.zemris.java.hw13.voting.VotingHelper;

import java.io.IOException;

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

		Long id = VotingHelper.verifyId("id", req, resp);
		if (id == null) {
			return;
		}

		DAOProvider.getDao().giveVoteTo(id);
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati?pollId="
				+ DAOProvider.getDao().getPollEntry(id).getPollId());
	}

}
