package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/pogled"})
public class PogledServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer counter = (Integer) req.getSession().getAttribute("counter");
		
		if (counter == null) {
			counter = 0;
		}
		
		req.setAttribute("counter", counter);
		
		req.getRequestDispatcher("/WEB-INF/pages/prikazCountera.jsp").forward(req, resp);
	}

}
