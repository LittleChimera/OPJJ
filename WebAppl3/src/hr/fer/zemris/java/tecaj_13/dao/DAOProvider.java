package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.sql.SQLDAO;

/**
 * Singleton razred koji zna koga treba vratiti kao pružatelja
 * usluge pristupa podsustavu za perzistenciju podataka.
 * Uoèite da, iako je odluka ovdje hardkodirana, naziv
 * razreda koji se stvara mogli smo dinamièki proèitati iz
 * konfiguracijske datoteke i dinamièki uèitati -- time bismo
 * implementacije mogli mijenjati bez ikakvog ponovnog kompajliranja
 * koda.
 * 
 * @author marcupic
 *
 */
public class DAOProvider {

	private static DAO dao = new SQLDAO();
	
	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}
