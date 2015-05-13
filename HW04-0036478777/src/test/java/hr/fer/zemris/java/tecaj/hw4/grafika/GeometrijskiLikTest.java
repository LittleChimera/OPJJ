package hr.fer.zemris.java.tecaj.hw4.grafika;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti razreda <code>GeometrijskiLik</code>.
 * 
 * @author Domagoj Latečki
 *
 */
public class GeometrijskiLikTest {
	
	/**
	 * Širina slika na kojoj će se testirati.
	 */
	public static int SIRINA = 10;
	/**
	 * Visina slike na kojoj će se testirati.
	 */
	public static int VISINA = 10;
	
	/**
	 * Pomoćni razred koji omogućuje testiranje metoda iz apstraktnog razreda
	 * <code>GeometrijskiLik</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class ApstraktniLikZaTestiranje extends GeometrijskiLik {
		
		@Override
		public boolean sadrziTocku(int x, int y) {
		
			if (x == y) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Testira metodu za popunjavanje geometrijskog lika.
	 */
	@Test
	public void testirajPopuniLik() {
	
		Slika slika = new Slika(SIRINA, VISINA);
		GeometrijskiLik lik = new ApstraktniLikZaTestiranje();
		OutputStream izlaz = new ByteArrayOutputStream();
		lik.popuniLik(slika);
		slika.nacrtajSliku(izlaz);
		char[] izlazniNizZnakova = izlaz.toString().toCharArray();
		
		int x = 0;
		int y = 0;
		for (char c : izlazniNizZnakova) {
			if (c == '\n') {
				y++;
				x = 0;
			} else {
				if (x == y) {
					Assert.assertEquals('*', c);
				} else {
					if (c != '\r' && c != '\t') {
						Assert.assertEquals('.', c);
					}
				}
				x++;
			}
		}
	}
}
