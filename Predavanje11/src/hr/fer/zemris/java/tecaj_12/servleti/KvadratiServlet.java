package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="kvadrati", urlPatterns={"/kvadrati"})
public class KvadratiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
			endAt = 20;
		}
		
		if (startFrom > endAt) {
			Integer tmp = startFrom;
			startFrom = endAt;
			endAt = tmp;
		}
		
		List<Zapis> rezultati = new ArrayList<KvadratiServlet.Zapis>();
		for (int i = startFrom, n = endAt; i <= n; i++) {
			rezultati.add(new Zapis(i, i*i));
		}
		
		req.setAttribute("rezultati", rezultati);
		
		req.getRequestDispatcher("/WEB-INF/pages/pogled.jsp").forward(req, resp);
	}
	
	public static class Zapis {
		private int broj;
		private int kvadrat;
		public Zapis(int broj, int kvadrat) {
			super();
			this.broj = broj;
			this.kvadrat = kvadrat;
		}
		
		public int getBroj() {
			return broj;
		}
		
		public int getKvadrat() {
			return kvadrat;
		}
		
	}

}