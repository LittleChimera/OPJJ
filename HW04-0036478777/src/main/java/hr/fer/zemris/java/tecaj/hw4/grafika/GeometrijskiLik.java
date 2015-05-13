package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

/**
 * Klasa koja modelira apstraktni geometrijski lik.
 * 
 * @author Domagoj Latečki
 *
 */
public abstract class GeometrijskiLik {
	
	/**
	 * Metoda koja određuje da li zadani geometrijski lik sadrži točku sa
	 * navedenim koordinatama.
	 * 
	 * @param x x-koordinata točke.
	 * @param y y-koordinata točke.
	 * @return Istinu ako je točka sadržana u trenutnom liku, laž u suprotnom.
	 */
	public abstract boolean sadrziTocku(int x, int y);
	
	/**
	 * Metoda koja crta lik na zadanoj slici.
	 * 
	 * @param slika slika na kojoj će lik biti iscrtan.
	 */
	public void popuniLik(Slika slika) {
	
		for (int x = 0, sirina = slika.getSirina(), visina = slika.getVisina(); x < sirina; x++) {
			for (int y = 0; y < visina; y++) {
				if (sadrziTocku(x, y)) {
					slika.upaliTocku(x, y);
				}
			}
		}
	}
}
