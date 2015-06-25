package hr.fer.java.tecaj.hw15.servlets;

import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String authorNick = req.getPathInfo();
		
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> results = em.createNamedQuery("BlogUser.requestUser")
				.setParameter("givenNick", authorNick).getResultList();

		req.setAttribute("author", results.size()==1?results.get(0):null);
		
		req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
		
	}

}
