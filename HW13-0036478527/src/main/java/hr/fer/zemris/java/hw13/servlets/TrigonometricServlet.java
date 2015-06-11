package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="trigonometric", urlPatterns={"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {

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
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	public static class SinCosEntry {
		double sin;
		double cos;
		
		public SinCosEntry(double sin, double cos) {
			super();
			this.sin = sin;
			this.cos = cos;
		}
		
		public double getCos() {
			return cos;
		}
		
		public double getSin() {
			return sin;
		}
	}
	
}
