package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.dao.DAOProvider;
import hr.fer.zemris.java.hw13.model.PollDefinition;
import hr.fer.zemris.java.hw13.voting.VotingHelper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet responsible for generating welcome page. It lists all polls.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "polls", urlPatterns = { "/index.html" })
public class PollsServlet extends HttpServlet {

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

		List<PollDefinition> polls = DAOProvider.getDao().getPolls();

		req.setAttribute("polls", polls);

		req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);

	}
}
