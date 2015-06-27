package hr.fer.zemris.java.tecaj_14.dao.jpa;

import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOException;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

/**
 * JPA implementation of {@link DAO}.
 * 
 * @author Luka Skugor
 *
 */
public class JPADAOImpl implements DAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.tecaj_14.dao.DAO#getBlogEntry(java.lang.Long)
	 */
	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(
				BlogEntry.class, id);
		return blogEntry;
	}

}
