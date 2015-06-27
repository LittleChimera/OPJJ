package hr.fer.java.tecaj.hw15.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet redirects root requests to the main page of this web app.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(urlPatterns = { "/index.jsp", "/servleti", "/." })
public class IndexServlet extends HttpServlet {

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
		resp.sendRedirect("servleti/main");
	}

}
