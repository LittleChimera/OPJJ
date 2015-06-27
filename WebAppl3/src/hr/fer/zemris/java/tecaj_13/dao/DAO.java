package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.util.List;

/**
 * Suèelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaæa sve postojeæe unose u bazi, ali puni samo dva podatka:
	 * id i title.
	 * 
	 * @return listu unosa
	 * @throws DAOException u sluèaju pogreške
	 */
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException;
	
	/**
	 * Dohvaæa Unos za zadani id. Ako unos ne postoji, vraæa <code>null</code>.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Unos dohvatiUnos(long id) throws DAOException;
	
}
