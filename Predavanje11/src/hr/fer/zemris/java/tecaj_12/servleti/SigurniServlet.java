package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SigurniServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.println("<h1>Sigurnost na djelu!</h1>");
		writer.println("<p>Bok "+req.getRemoteUser()+"</b>");
		writer.println("<p>request.isUserInRole(\"ADM\")? "+req.isUserInRole("ADM")+"</b>");
	}
}