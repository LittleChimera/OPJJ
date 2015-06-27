package hr.fer.zemris.java.tecaj_14.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider of JPA {@link javax.persistence.EntityManagerFactory}.
 * 
 * @author Luka Skugor
 *
 */
public class JPAEMFProvider {

	/**
	 * Singleton factory.
	 */
	public static EntityManagerFactory emf;

	/**
	 * Gets an instance of {@link javax.persistence.EntityManagerFactory}.
	 * @return instance of entity manager factory
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets instance of {@link javax.persistence.EntityManagerFactory}.
	 * @param emf set entity manager factory
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
