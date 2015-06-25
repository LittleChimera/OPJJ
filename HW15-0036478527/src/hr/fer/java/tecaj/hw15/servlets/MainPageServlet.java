package hr.fer.java.tecaj.hw15.servlets;


import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/main")
public class MainPageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogUser> authors = em.createNamedQuery("BlogUser.listUsers").getResultList();
		req.setAttribute("blogAuthors", authors);
		
		/*em.getTransaction().commit();
		em.close();*/
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
}	
