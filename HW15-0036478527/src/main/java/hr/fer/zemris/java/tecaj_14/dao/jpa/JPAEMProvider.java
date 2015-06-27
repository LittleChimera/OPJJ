package hr.fer.zemris.java.tecaj_14.dao.jpa;

import hr.fer.zemris.java.tecaj_14.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Provider of JPA {@link javax.persistence.EntityManager}.
 * 
 * @author Luka Skugor
 *
 */
public class JPAEMProvider {

	/**
	 * Locals.
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Gets entity manager.
	 * 
	 * @return entity manager
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Closes provider.
	 * 
	 * @throws DAOException
	 *             if DAOException occurs
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null)
			throw dex;
	}

	/**
	 * Wrapper for {@link javax.persistence.EntityManager}.
	 * 
	 * @author Luka Skugor
	 *
	 */
	private static class LocalData {
		/**
		 * Entity manager.
		 */
		EntityManager em;
	}

}
