package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="colorsetter", urlPatterns={"/setcolor"})
public class ColorSetterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String BG_COLOR = "pickedBgColor";
	public static final String DEFAULT_COLOR = "FFFFFF";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String color = req.getParameter(BG_COLOR);
		
		if (color != null && (color.length() == 6 || color.length() == 3) && color.matches("[0-9a-fA-F]+")) {
			req.getSession().setAttribute(BG_COLOR, color);			
		}
		
		req.getRequestDispatcher("/colors.jsp").forward(req, resp);
	}

}
