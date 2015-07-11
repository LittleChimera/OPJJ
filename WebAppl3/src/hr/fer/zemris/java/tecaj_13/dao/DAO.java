package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.util.List;

/**
 * Su�elje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohva�a sve postoje�e unose u bazi, ali puni samo dva podatka:
	 * id i title.
	 * 
	 * @return listu unosa
	 * @throws DAOException u slu�aju pogre�ke
	 */
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException;
	
	/**
	 * Dohva�a Unos za zadani id. Ako unos ne postoji, vra�a <code>null</code>.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Unos dohvatiUnos(long id) throws DAOException;
	
}
