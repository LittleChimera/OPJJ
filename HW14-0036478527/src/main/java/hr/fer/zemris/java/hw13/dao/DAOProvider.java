package hr.fer.zemris.java.hw13.dao;

import hr.fer.zemris.java.hw13.sql.SQLDAO;


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
	 * SQL DAO.
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Gets DAO subsystem.
	 * @return DAO
	 */
	public static DAO getDao() {
		return dao;
	}
	
}
