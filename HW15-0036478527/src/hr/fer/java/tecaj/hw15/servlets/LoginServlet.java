package hr.fer.java.tecaj.hw15.servlets;

import hr.fer.java.tecaj.hw15.util.AuthenticationUtility;
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

@WebServlet("/servleti/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlogUser user = new BlogUser();

		String passwordHash = AuthenticationUtility.encryptPassword(req.getParameter("password"));
		user.setPasswordHash(passwordHash);

		user.setNick(req.getParameter("nick"));
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		@SuppressWarnings("unchecked")
		List<BlogUser> results = em.createNamedQuery("BlogUser.requestUser")
				.setParameter("givenNick", user.getNick()).getResultList();

		em.getTransaction().commit();
		em.close();
		
		List<String> loginErrors = new LinkedList<String>();
		req.setAttribute("loginErrors", loginErrors);
		
		if (!results.isEmpty() && passwordHash.equals(user.getPasswordHash())) {
			user = results.get(0);
			HttpSession session = req.getSession();
			session.setAttribute("current.user.id", user.getId());
			session.setAttribute("current.user.fn", user.getFirstName());
			session.setAttribute("current.user.ln", user.getLastName());
			session.setAttribute("current.user.nick", user.getNick());
			resp.sendRedirect("main");
		} else {
			loginErrors.add("Invalid nick or password.");
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
		}
		
	}

}
