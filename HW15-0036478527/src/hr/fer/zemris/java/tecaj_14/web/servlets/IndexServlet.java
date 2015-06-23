package hr.fer.zemris.java.tecaj_14.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jedina namjena ovog servleta je da korisnika koji 
 * unese u URL-u samo naziv aplikacije (ali ne i koji
 * servlet želi) redirekta na naš defaultni servlet
 * koji radi prikaz.
 * 
 * @author marcupic
 */
@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("servleti/prikazi");
	}
	
}
