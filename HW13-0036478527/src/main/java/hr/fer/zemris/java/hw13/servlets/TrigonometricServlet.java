package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TrigonometricServlet calculates sinuses and cosinuses of integers from a to b
 * (both inclusive). a and b are given through GET request parameters.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "trigonometric", urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {

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

		Integer startFrom = null;
		Integer endAt = null;

		try {
			startFrom = Integer.valueOf(req.getParameter("a"));
		} catch (Exception e) {
			startFrom = 0;
		}

		try {
			endAt = Integer.valueOf(req.getParameter("b"));
		} catch (Exception e) {
			endAt = 360;
		}

		if (startFrom > endAt) {
			Integer tmp = startFrom;
			startFrom = endAt;
			endAt = tmp;
		}

		final int fullCircle = 360;
		if (endAt > startFrom + fullCircle) {
			endAt = startFrom + 360;
		}

		List<SinCosEntry> results = new ArrayList<SinCosEntry>();
		for (int i = startFrom, n = endAt; i <= n; i++) {
			double angle = i * 2 * Math.PI / 360;
			results.add(new SinCosEntry(Math.sin(angle), Math.cos(angle)));
		}

		req.setAttribute("start", startFrom);
		req.setAttribute("end", endAt);
		req.setAttribute("results", results);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(
				req, resp);
	}

	/**
	 * SinCosEntry holds values of sin(x) and cos(x) for an angle x.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public static class SinCosEntry {
		/**
		 * sin(x)
		 */
		double sin;
		/**
		 * cos(x)
		 */
		double cos;

		/**
		 * Creates a new SinCosEntry with given sin(x) and cos(x).
		 * @param sin sin(x)
		 * @param cos cos(x)
		 */
		public SinCosEntry(double sin, double cos) {
			super();
			this.sin = sin;
			this.cos = cos;
		}

		/**
		 * Gets cos(x).
		 * @return cos(x)
		 */
		public double getCos() {
			return cos;
		}

		/**
		 * Gets sin(x).
		 * @return sin(x)
		 */
		public double getSin() {
			return sin;
		}
	}

}
