package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.sql.SQLDAO;

/**
 * Singleton razred koji zna koga treba vratiti kao pru�atelja
 * usluge pristupa podsustavu za perzistenciju podataka.
 * Uo�ite da, iako je odluka ovdje hardkodirana, naziv
 * razreda koji se stvara mogli smo dinami�ki pro�itati iz
 * konfiguracijske datoteke i dinami�ki u�itati -- time bismo
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
