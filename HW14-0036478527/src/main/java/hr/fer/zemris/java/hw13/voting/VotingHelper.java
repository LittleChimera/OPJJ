package hr.fer.zemris.java.hw13.voting;

import hr.fer.zemris.java.hw13.model.PollEntry;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Helper class for doing useful functions in voting system.
 * 
 * @author Luka Skugor
 *
 */
public class VotingHelper {

	/**
	 * Verifies if the request parameter with given parameter name is legal ID.
	 * If not request is redirected to an error page.
	 * 
	 * @param id
	 *            name of the parameter
	 * @param req
	 *            HTTP request
	 * @param resp
	 *            HTTP response
	 * @return parsed ID or null if error occured
	 * @throws IOException
	 *             if IOException occurs
	 * @throws ServletException
	 *             if servlet exception occurs
	 */
	public static Long verifyId(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		try {
			return Long.parseLong(req.getParameter(id));
		} catch (NumberFormatException e) {
			req.setAttribute("error", "Illegal poll ID");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return null;
		}
	}

	/**
	 * Sorts results first by votes count descending then by title.
	 * 
	 * @param results
	 *            results to sort
	 */
	public static void sortResults(List<PollEntry> results) {
		Comparator<PollEntry> byVotes = (r1, r2) -> r1.getVotesCount()
				.compareTo(r2.getVotesCount());
		Comparator<PollEntry> byTitle = (r1, r2) -> r1.getOptionTitle()
				.compareTo(r2.getOptionTitle());
		Collections.sort(results, byVotes.reversed().thenComparing(byTitle));
	}

}
