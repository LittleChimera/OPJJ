package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ColorSetterServlet is a servlet which changes background color of all
 * application's pages. Color is set through parameter with name
 * {@link #BG_COLOR}.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "colorsetter", urlPatterns = { "/setcolor" })
public class ColorSetterServlet extends HttpServlet {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parameter name through which background color is set.
	 */
	public static final String BG_COLOR = "pickedBgColor";
	/**
	 * Default background color value.
	 */
	public static final String DEFAULT_COLOR = "FFFFFF";

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

		String color = req.getParameter(BG_COLOR);

		if (color != null && (color.length() == 6 || color.length() == 3)
				&& color.matches("[0-9a-fA-F]+")) {
			req.getSession().setAttribute(BG_COLOR, color);
		}

		req.getRequestDispatcher("/colors.jsp").forward(req, resp);
	}

}
