package hr.fer.zemris.java.tecaj.hw4.grafika;

/**
 * Klasa koja modelira kružnicu.
 * 
 * @author Domagoj Latečki
 *
 */
public class Kruznica extends Elipsa {
	
	/**
	 * Referenca na objekt koji služi za stvaranje kružnica.
	 */
	public static final StvarateljLika STVARATELJ = new KruznicaStvaratelj();
	
	/**
	 * Konstruira novu kružnicu sa zadanim prametrima.
	 * 
	 * @param centarX x-koordinata centra kružnice.
	 * @param centarY y-koordinata centra kružnice.
	 * @param radijus radijus kružnice.
	 * @throws IllegalArgumentException Ako je radijus kružnice manji od 0.
	 */
	private Kruznica(int centarX, int centarY, int radijus) {
	
		super(centarX, centarY, radijus, radijus);
	}
	
	/**
	 * Klasa koja služi za stvaranje kružnica. Ova klasa implementira sučelje
	 * <code>StvarateljLika</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class KruznicaStvaratelj implements StvarateljLika {
		
		@Override
		public String nazivLika() {
		
			return "KRUG";
		}
		
		/**
		 * Metoda koja će napraviti novi objekt za kružnicu sa zadanim
		 * parametrima. Kružnica ima 3 parametra, redom: x-koordinata centra
		 * kružnice, y-koordinata centra kružnice, radijus.
		 * 
		 * @param parametri string koji sadrži parametre za novu kružnicu.
		 * @return Nova kružnica koja je napravljena sa zadanim parametrima.
		 * @throws IllegalArgumentException Ako je zadan broj parametara
		 *             različit od 3, ili ako je radijus kružnice manji od 0.
		 */
		@Override
		public GeometrijskiLik stvoriIzStringa(String parametri) {
		
			String argumenti[] = parametri.trim().split("\\s+");
			if (argumenti.length != 3) {
				throw new IllegalArgumentException("Za kruznicu se ocekuju 3 broja u stringu.");
			}
			
			return new Kruznica(Integer.parseInt(argumenti[0]), Integer.parseInt(argumenti[1]),
					Integer.parseInt(argumenti[2]));
		}
	}
}
