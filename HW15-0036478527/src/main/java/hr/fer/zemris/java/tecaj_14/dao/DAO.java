package hr.fer.zemris.java.tecaj_14.dao;

import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

/**
 * Interface of subsystem for keeping data persistency.
 * 
 * @author Luka Skugor
 *
 */
public interface DAO {

	/**
	 * Gets entry with given id. If such doesn't exist returns <code>null</code>.
	 * 
	 * @param id entry id
	 * @return entry or <code>null</code> if such doesn't exist
	 * @throws DAOException if DAOException occurs
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
}
