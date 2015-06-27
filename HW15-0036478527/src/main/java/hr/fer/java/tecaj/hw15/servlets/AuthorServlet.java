package hr.fer.java.tecaj.hw15.servlets;

import hr.fer.zemris.java.tecaj_14.model.BlogComment;
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

/**
 * This servlet is used for handling getting displaying entries, authors and
 * adding new entries or comments to existing entries.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

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

		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		String requestedAuthorNick = getAuthorNick(req.getPathInfo());
		String loggedAuthorNick = (String) req.getSession().getAttribute(
				"current.user.nick");

		String authorAction = req.getPathInfo().substring(
				("/" + requestedAuthorNick).length());

		if (authorAction.matches("/\\d+")) {
			dispatchEntry(req, resp, em,
					Long.parseLong(authorAction.substring(1)));
			return;
		}

		BlogEntry editingEntry = null;
		if (authorAction.equals("/new")) {
			editingEntry = parseNewEntryRequest(req, resp, requestedAuthorNick,
					loggedAuthorNick);
			if (editingEntry == null) {
				return;
			}
		}

		if (authorAction.equals("/edit")) {

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

	/**
	 * This method handles request for adding comment.
	 * @param req request
	 * @param resp response
	 * @param em entity manager
	 */
	private void addComment(HttpServletRequest req, HttpServletResponse resp,
			EntityManager em) {
		Long blogEntryId = Long.parseLong(req.getParameter("blogId"));
		BlogEntry entry = em.find(BlogEntry.class, blogEntryId);

		BlogComment comment = new BlogComment();
		comment.setBlogEntry(entry);
		comment.setMessage(req.getParameter("message"));
		comment.setPostedOn(new Date());
		Long loggedUserId = (Long) req.getSession().getAttribute(
				"current.user.id");
		comment.setUsersEMail((loggedUserId != null) ? em.find(BlogUser.class,
				loggedUserId).getEmail() : req.getParameter("email"));

		em.persist(comment);
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * This method handles request for dispatching a requested entry.
	 * @param req request
	 * @param resp response
	 * @param em entity manager
	 * @param entryId id of the entry
	 * @throws ServletException if it occurs
	 * @throws IOException if it occurs
	 */
	private void dispatchEntry(HttpServletRequest req,
			HttpServletResponse resp, EntityManager em, long entryId)
			throws ServletException, IOException {

		String authorNick = getAuthorNick(req.getPathInfo());

		BlogEntry blogEntry = em.find(BlogEntry.class, entryId);
		if (!authorNick.equals(blogEntry.getCreator().getNick())) {
			req.setAttribute("error", "Invalid id");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return;
		}

		req.setAttribute("blogEntry", blogEntry);
		req.getRequestDispatcher("/WEB-INF/pages/blogDisplay.jsp").forward(req,
				resp);

		em.getTransaction().commit();
		em.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		String requestedAuthorNick = getAuthorNick(req.getPathInfo());

		String authorAction = req.getPathInfo().substring(
				("/" + requestedAuthorNick).length());
		if (authorAction.equals("/addComment")) {
			addComment(req, resp, em);
		}

		if (authorAction.equals("/save")) {
			if (!saveEntry(req, resp, em)) {
				return;
			}
		}
		resp.sendRedirect("../" + requestedAuthorNick);
	}

	/**
	 * This method handles request for saving an entry.
	 * @param req request
	 * @param resp response
	 * @param em entity manager
	 * @return true if entry was saved, otherwise false (i.e. if error occurs)
	 * @throws ServletException if it occurs
	 * @throws IOException if it occurs
	 */
	private boolean saveEntry(HttpServletRequest req, HttpServletResponse resp,
			EntityManager em) throws ServletException, IOException {
		String requestedAuthorNick = getAuthorNick(req.getPathInfo());
		String loggedAuthorNick = (String) req.getSession().getAttribute(
				"current.user.nick");

		Long blogEntryId;
		try {
			blogEntryId = Long.parseLong(req.getParameter("id"));
		} catch (Exception invalidId) {
			req.setAttribute("error", "Invalid id");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return false;
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
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,
					resp);
			return false;
		}

		if (blogEntry.getCreatedAt() == null) {
			blogEntry.setCreatedAt(new Date());
		}
		blogEntry.setLastModifiedAt(new Date());

		em.persist(blogEntry);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	/**
	 * This method handles request for editing entry.
	 * @param req request
	 * @param resp response
	 * @param em entity manager
	 * @param requestedAuthorNick nick of requested author
	 * @param loggedAuthorNick nick of logged author
	 * @return editing blog entry or null if error occurred
	 * @throws ServletException if it occurs
	 * @throws IOException if it occurs
	 */
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

	/**
	 * This method handles request for creating a new entry.
	 * @param req request 
	 * @param resp response
	 * @param requestedAuthorNick requested author nick
	 * @param loggedAuthorNick logged in author nick
	 * @return new blog entry or null if error occurs
	 * @throws ServletException if it occurs
	 * @throws IOException if it occurs
	 */
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

	/**
	 * Gets authors nick from requested path info.
	 * @param pathInfo requested path info
	 * @return requested author nick
	 */
	private String getAuthorNick(String pathInfo) {
		return pathInfo.substring(1).split("/")[0];
	}

	/**
	 * This method handles request for listing author's blog entries.
	 * @param req request
	 * @param resp response
	 * @param em entity manager
	 * @throws ServletException if it occurs
	 * @throws IOException if it occurs
	 */
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
