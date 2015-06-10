package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrviServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		
		Writer out = resp.getWriter();
		
		out.write("<html>\n");
		out.write("   <head>\n");
		out.write("      <title>Moja prva servlet-generirana aplikacija</title>\n");
		out.write("   </head>\n");
		out.write("   <body bgcolor=\"#FFFF00\">\n");
		out.write("      <h1>Moj prvi dokument</h1>\n");
		out.write("      <p>Ovo je moja prva stranica koju generira <i>servlet</i>.</p>\n");
		for (int i = 0; i < 3; i++) {
			out.write("      <p>Hello World!</p>\n");
		}
		out.write("   </body>\n");
		out.write("</html>\n");
		
		out.flush();
	}

}
