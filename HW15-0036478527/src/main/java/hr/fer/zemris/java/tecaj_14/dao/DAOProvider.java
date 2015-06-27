package hr.fer.zemris.java.tecaj_14.dao;

import hr.fer.zemris.java.tecaj_14.dao.jpa.JPADAOImpl;

/**
 * Provides a DAO subsystem.
 * 
 * @see DAO
 * 
 * @author Luka Skugor
 *
 */
public class DAOProvider {

	/**
	 * JPA DAO.
	 */
	private static DAO dao = new JPADAOImpl();

	/**
	 * Gets DAO subsystem.
	 * @return DAO
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}
