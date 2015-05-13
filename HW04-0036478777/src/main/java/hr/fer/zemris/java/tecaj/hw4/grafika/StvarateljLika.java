package hr.fer.zemris.java.tecaj.hw4.grafika;

/**
 * Sučelje koje definira koje metode moraju imate klase koje stvaraju likove.
 * 
 * @author Domagoj Latečki
 *
 */
public interface StvarateljLika {
	
	/**
	 * Metoda za dohvaćanje naziva lika.
	 * 
	 * @return Naziv lika.
	 */
	String nazivLika();
	
	/**
	 * Metoda koja će napraviti novi objekt za geometrijski lik sa zadanim
	 * parametrima.
	 * 
	 * @param parametri string koji sadrži parametre za novi geometrijski lik.
	 * @return Novi geometrijski lik koji je napravljen sa zadanim parametrima.
	 */
	GeometrijskiLik stvoriIzStringa(String parametri);
}
