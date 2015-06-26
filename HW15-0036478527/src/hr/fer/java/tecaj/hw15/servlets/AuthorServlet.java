package hr.fer.java.tecaj.hw15.servlets;

import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
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

		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		String requestedAuthorNick = getAuthorNick(req.getPathInfo());
		String loggedAuthorNick = (String) req.getSession().getAttribute(
				"current.user.nick");

		BlogEntry editingEntry = null;
		if (req.getPathInfo().substring(("/" + requestedAuthorNick).length())
				.equals("/new")) {
			editingEntry = parseNewEntryRequest(req, resp, requestedAuthorNick,
					loggedAuthorNick);
			if (editingEntry == null) {
				return;
			}
		}

		if (req.getPathInfo().substring(("/" + requestedAuthorNick).length())
				.startsWith("/edit?")) {

			editingEntry = parseEditingEntryRequest(req, resp, em,
					requestedAuthorNick, loggedAuthorNick);
			if (editingEntry == null) {
				return;
			}
		}

		if (editingEntry != null) {
			req.setAttribute("blogEntry", editingEntry);
			req.getRequestDispatcher("/WEB-INF/pages/entryForm.jsp").forward(
					req, resp);
			return;
		}

		dispatchEntries(em, req, resp);
		
		em.getTransaction().commit();
		em.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		String requestedAuthorNick = getAuthorNick(req.getPathInfo());
		String loggedAuthorNick = (String) req.getSession().getAttribute(
				"current.user.nick");

		if (req.getPathInfo().substring(("/" + requestedAuthorNick).length())
				.equals("/save")) {
			Long blogEntryId;
			try {
				blogEntryId = Long.parseLong(req.getParameter("id"));
			} catch (Exception invalidId) {
				req.setAttribute("error", "Invalid id");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(
						req, resp);
				return;
			}

			BlogEntry blogEntry = (blogEntryId >= 0) ? em.find(BlogEntry.class,
					blogEntryId) : new BlogEntry();
			blogEntry.setTitle(req.getParameter("title"));
			blogEntry.setText(req.getParameter("blogEntryBody"));
			if (blogEntry.getCreator() == null) {
				blogEntry.setCreator(em.find(BlogUser.class, req.getSession()
						.getAttribute("current.user.id")));
			}

			String entrysAuthorNick = blogEntry.getCreator().getNick();

			if ((!requestedAuthorNick.equals(entrysAuthorNick))
					|| !requestedAuthorNick.equals(loggedAuthorNick)) {
				req.setAttribute("error", "Illegal access");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(
						req, resp);
				return;
			}

			if (blogEntry.getCreatedAt() == null) {
				blogEntry.setCreatedAt(new Date());
			}
			blogEntry.setLastModifiedAt(new Date());

			em.persist(blogEntry);
			em.getTransaction().commit();
			em.close();

			resp.sendRedirect(req.getServletPath() + "/" + requestedAuthorNick);
		}
	}

	private BlogEntry parseEditingEntryRequest(HttpServletRequest req,
			HttpServletResponse resp, EntityManager em,
			String requestedAuthorNick, String loggedAuthorNick)
			throws ServletException, IOException {

		Long blogEntryId;
		try {
			blogEntryId = Long.parseLong(req.getParameter("id"));
		} catch (Exception invalidId) {
			req.setAttribute("error", "Invalid id");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return null;
		}

		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryId);

		String entrysAuthorNick = (blogEntry != null) ? blogEntry.getCreator()
				.getNick() : null;

		if (!requestedAuthorNick.equals(entrysAuthorNick)
				|| !requestedAuthorNick.equals(loggedAuthorNick)) {
			req.setAttribute("error", "Illegal access");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return null;
		}

		return blogEntry;
	}

	private BlogEntry parseNewEntryRequest(HttpServletRequest req,
			HttpServletResponse resp, String requestedAuthorNick,
			String loggedAuthorNick) throws ServletException, IOException {

		if (!requestedAuthorNick.equals(loggedAuthorNick)) {
			req.setAttribute("error", "Unauthorized access");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return null;
		}
		BlogEntry editingEntry = new BlogEntry();
		editingEntry.setId((long) -1);
		editingEntry.setText("");
		return editingEntry;
	}

	private String getAuthorNick(String pathInfo) {
		return pathInfo.substring(1).split("/")[0];
	}

	private void dispatchEntries(EntityManager em, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		String authorNick = getAuthorNick(req.getPathInfo());

		@SuppressWarnings("unchecked")
		List<BlogUser> results = em.createNamedQuery("BlogUser.requestUser")
				.setParameter("givenNick", authorNick).getResultList();

		BlogUser author = results.size() == 1 ? results.get(0) : null;
		req.setAttribute("author", author);
		if (author != null) {
			req.setAttribute("blogEntries", author.getEntries());
		}

		req.getRequestDispatcher("/WEB-INF/pages/author.jsp")
				.forward(req, resp);
		
		em.getTransaction().commit();
		em.close();

	}

}
