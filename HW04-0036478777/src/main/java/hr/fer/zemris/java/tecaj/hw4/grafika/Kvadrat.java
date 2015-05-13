package hr.fer.zemris.java.tecaj.hw4.grafika;

/**
 * Klasa koja modelira kvadrat.
 * 
 * @author Domagoj Latečki
 *
 */
public class Kvadrat extends Pravokutnik {
	
	/**
	 * Referenca na objekt koji služi za stvaranje kvadrata.
	 */
	public static final StvarateljLika STVARATELJ = new KvadratStvaratelj();
	
	/**
	 * Konstruira novi kvadrat sa zadanim prametrima.
	 * 
	 * @param gornjiLijeviX x-koordinata gornjeg lijevog kuta pravokutnika.
	 * @param gornjiLijeviY y-koordinata gornjeg lijevog kuta pravokutnika.
	 * @param duljinaStranice duljina stranice kvadrata.
	 * @throws IllegalArgumentException Ako je duljina stranice manja od 0.
	 */
	private Kvadrat(int gornjiLijeviX, int gornjiLijeviY, int duljinaStranice) {
	
		super(gornjiLijeviX, gornjiLijeviY, duljinaStranice, duljinaStranice);
	}
	
	/**
	 * Klasa koja služi za stvaranje kvadrata. Ova klasa implementira sučelje
	 * <code>StvarateljLika</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class KvadratStvaratelj implements StvarateljLika {
		
		@Override
		public String nazivLika() {
		
			return "KVADRAT";
		}
		
		/**
		 * Metoda koja će napraviti novi objekt za kvadrat sa zadanim
		 * parametrima. Kvadrat ima 3 parametra, redom: x-koordinata gornjeg
		 * lijevog kuta, y-koordinata gornjeg lijevog kuta i duljina stranice.
		 * Mogu se navesti i 4 parametra, ali zadnji parametar mora biti isti
		 * kao i duljina stranice.
		 * 
		 * @param parametri string koji sadrži parametre za novi kvadrat.
		 * @return Novi kvadrat koji je napravljen sa zadanim parametrima.
		 * @throws IllegalArgumentException Ako je zadan broj parametara
		 *             različit od 3 ili 4, ili ako duljina stranice i 4.
		 *             parametar nisu jednaki, ili ako je duljina stranice manja
		 *             od 0.
		 */
		@Override
		public GeometrijskiLik stvoriIzStringa(String parametri) {
		
			String argumenti[] = parametri.trim().split("\\s+");
			if (argumenti.length != 3 && argumenti.length != 4) {
				throw new IllegalArgumentException("Za kvadrat se ocekuju 3 ili 4 broja u stringu.");
			}
			if (argumenti.length == 4
					&& Integer.parseInt(argumenti[2]) != Integer.parseInt(argumenti[3])) {
				
				throw new IllegalArgumentException("Visina i sirina kvadrata moraju biti iste.");
			}
			
			return new Kvadrat(Integer.parseInt(argumenti[0]), Integer.parseInt(argumenti[1]),
					Integer.parseInt(argumenti[2]));
		}
	}
}
