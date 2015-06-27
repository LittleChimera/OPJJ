package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/azuriraj" })
public class AzurirajServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}
	
	protected void obradi(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer counter = (Integer) req.getSession().getAttribute("counter");
		if (counter == null) {
			counter = 0;
		}

		Integer a = Integer.valueOf(req.getParameter("a"));
		Integer b = Integer.valueOf(req.getParameter("b"));

		counter = counter + a - b;

		req.getSession().setAttribute("counter", counter);
		
		resp.sendRedirect("pogled");
		//req.setAttribute("counter", counter);
		
		//req.getRequestDispatcher("/WEB-INF/pages/prikazAzuriranja.jsp").forward(req, resp);
	}

}
