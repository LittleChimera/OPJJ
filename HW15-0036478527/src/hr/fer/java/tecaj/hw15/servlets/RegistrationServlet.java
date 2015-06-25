package hr.fer.java.tecaj.hw15.servlets;

import hr.fer.java.tecaj.hw15.util.AuthenticationUtility;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/register")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlogUser user = new BlogUser();
		String password = req.getParameter("password");
		user.setFirstName(req.getParameter("firstName"));
		user.setLastName(req.getParameter("lastName"));
		user.setNick(req.getParameter("nick"));
		user.setEmail(req.getParameter("email"));
		
		List<String> errors = validateRegisterData(user, password);
		if (errors.size() != 0) {
			req.setAttribute("registerErrors", errors);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}

		String passwordHash = AuthenticationUtility.encryptPassword(password);
		user.setPasswordHash(passwordHash);
		
		EntityManagerFactory emf = (EntityManagerFactory) req
				.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> results = em.createNamedQuery("BlogUser.requestUser")
				.setParameter("givenNick", user.getNick()).getResultList();

		if (!results.isEmpty()) {
			errors.add("Nick already taken");
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			
		} else {
			//add user
			em.getTransaction().begin();
			
			em.persist(user);
			
		}
		em.getTransaction().commit();
		em.close();
	}
	
	private List<String> validateRegisterData(BlogUser user, String password) {
		List<String> errors = new ArrayList<String>();
		
		if (user.getFirstName() == null || user.getFirstName().length() < 2) {
			errors.add("First name has to have at least 2 letters");
		}
		if (user.getLastName() == null || user.getLastName().length() < 2) {
			errors.add("Last name has to have at least 2 letters");
		}
		if (user.getNick() == null || user.getNick().length() < 2) {
			errors.add("Nick has to have at least 2 letters");
		}
		
		/*^							#start of the line
		*  [_A-Za-z0-9-\\+]+		#  must start with string in the bracket [ ], must contains one or more (+)
		*  (						#   start of group #1
		*    \\.[_A-Za-z0-9-]+		#     follow by a dot "." and string in the bracket [ ], must contains one or more (+)
		*  )*						#   end of group #1, this group is optional (*)
		*    @						#     must contains a "@" symbol
		*     [A-Za-z0-9-]+      	#       follow by string in the bracket [ ], must contains one or more (+)
		*      (					#         start of group #2 - first level TLD checking
		*       \\.[A-Za-z0-9]+  	#           follow by a dot "." and string in the bracket [ ], must contains one or more (+)
		*      )*					#         end of group #2, this group is optional (*)
		*      (					#         start of group #3 - second level TLD checking
		*       \\.[A-Za-z]{2,}  	#           follow by a dot "." and string in the bracket [ ], with minimum length of 2
		*      )					#         end of group #3
		*$							#end of the line
		*/
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (user.getEmail() == null || !user.getEmail().matches(EMAIL_PATTERN)) {
			errors.add("Invalid email");
		}
		if (password == null || password.length() < 8) {
			errors.add("Passwords has to have at least 8 characters");
		}
		
		return errors;
	}

}
