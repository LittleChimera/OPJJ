package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

/**
 * Klasa koja modelira pravokutnik.
 * 
 * @author Domagoj Latečki
 *
 */
public class Pravokutnik extends GeometrijskiLik {
	
	/**
	 * x-koordinata točke u gornjem lijevom kutu pravokutnika.
	 */
	private int gornjiLijeviX;
	/**
	 * y-koordinata točke u gornjem lijevom kutu pravokutnika.
	 */
	private int gornjiLijeviY;
	/**
	 * Širina pravokutnika.
	 */
	private int sirina;
	/**
	 * Visina pravokutnika.
	 */
	private int visina;
	/**
	 * Referenca na objekt koji služi za stvaranje pravokutnika.
	 */
	public static final StvarateljLika STVARATELJ = new PravokutnikStvaratelj();
	
	/**
	 * Konstruira novi pravokutnik sa zadanim parametrima.
	 * 
	 * @param gornjiLijeviX x-koordinata gornjeg lijevog kuta pravokutnika.
	 * @param gornjiLijeviY y-koordinata gornjeg lijevog kuta pravokutnika.
	 * @param sirina širina pravokutnika.
	 * @param visina visina pravokutnika.
	 * @throws IllegalArgumentException Ako je širina ili visina manja od 0.
	 */
	protected Pravokutnik(int gornjiLijeviX, int gornjiLijeviY, int sirina, int visina) {
	
		if (sirina < 0 || visina < 0) {
			throw new IllegalArgumentException("Duljina stranice ne moze biti negativna.");
		}
		
		this.gornjiLijeviX = gornjiLijeviX;
		this.gornjiLijeviY = gornjiLijeviY;
		this.sirina = sirina;
		this.visina = visina;
	}
	
	@Override
	public boolean sadrziTocku(int x, int y) {
	
		if (x >= gornjiLijeviX && x < gornjiLijeviX + sirina && y >= gornjiLijeviY
				&& y < gornjiLijeviY + visina) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void popuniLik(Slika slika) {
	
		int pocetniY = gornjiLijeviY >= 0 ? gornjiLijeviY : 0;
		int zavrsniX = gornjiLijeviX + sirina < slika.getSirina() ? gornjiLijeviX + sirina : slika
				.getSirina();
		int zavrsniY = gornjiLijeviY + visina < slika.getVisina() ? gornjiLijeviY + visina : slika
				.getVisina();
		
		for (int x = gornjiLijeviX >= 0 ? gornjiLijeviX : 0; x < zavrsniX; x++) {
			for (int y = pocetniY; y < zavrsniY; y++) {
				slika.upaliTocku(x, y);
			}
		}
	}
	
	/**
	 * Klasa koja služi za stvaranje pravokutnika. Ova klasa implementira
	 * sučelje <code>StvarateljLika</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class PravokutnikStvaratelj implements StvarateljLika {
		
		@Override
		public String nazivLika() {
		
			return "PRAVOKUTNIK";
		}
		
		/**
		 * Metoda koja će napraviti novi objekt za pravokutnik sa zadanim
		 * parametrima. Pravokutnik ima 4 parametra, redom: x-koordinata gornjeg
		 * lijevog kuta, y-koordinata gornjeg lijevog kuta, širina, visina.
		 * 
		 * @param parametri string koji sadrži parametre za novi pravokutnik.
		 * @return Novi pravokutnik koji je napravljen sa zadanim parametrima.
		 * @throws IllegalArgumentException Ako je zadan broj parametara
		 *             različit od 4, ili ako je širina ili visina manja od 0.
		 */
		@Override
		public GeometrijskiLik stvoriIzStringa(String parametri) {
		
			String argumenti[] = parametri.trim().split("\\s+");
			if (argumenti.length != 4) {
				throw new IllegalArgumentException("Za pravokutnik se ocekuju 4 broja u stringu.");
			}
			
			return new Pravokutnik(Integer.parseInt(argumenti[0]), Integer.parseInt(argumenti[1]),
					Integer.parseInt(argumenti[2]), Integer.parseInt(argumenti[3]));
		}
	}
}
