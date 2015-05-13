package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti pravokutnika.
 * 
 * @author Domagoj Latečki
 *
 */
public class PravokutnikTest {
	
	/**
	 * Testira stvaratelja pravokutnika.
	 */
	@Test
	public void testirajStvaratelja() {
	
		GeometrijskiLik pravokutnik = Pravokutnik.STVARATELJ.stvoriIzStringa("10 10 20 30");
		Assert.assertTrue("Napravljeni geometrijski lik nije pravokutnik.",
				pravokutnik instanceof Pravokutnik);
	}
	
	/**
	 * Testira baca li stvaratelj pravokutnika iznimku kada je premali broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara1() {
	
		Pravokutnik.STVARATELJ.stvoriIzStringa("1 2 3");
	}
	
	/**
	 * Testira baca li stvaratelj pravokutnika iznimku kada je prevelik broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara2() {
	
		Pravokutnik.STVARATELJ.stvoriIzStringa("1 2 3 4 5");
	}
	
	/**
	 * Testira baca li stvaratelj pravokutnika iznimku kada je zadana širina
	 * negativna.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima1() {
	
		Pravokutnik.STVARATELJ.stvoriIzStringa("1 2 -3 6");
	}
	
	/**
	 * Testira baca li stvaratelj pravokutnika iznimku kada je zadana visina
	 * negativna.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima2() {
	
		Pravokutnik.STVARATELJ.stvoriIzStringa("1 2 3 -6");
	}
	
	/**
	 * Testira sadrži li stvaratelj ispravno ime za pravokutnik.
	 */
	@Test
	public void testirajImeLikaUStvaratelju() {
	
		Assert.assertEquals("Stvaratelj ima krivi naziv lika.", "PRAVOKUTNIK",
				Pravokutnik.STVARATELJ.nazivLika());
	}
	
	/**
	 * Testira metodu za sadržanje točke.
	 */
	@Test
	public void testirajSadrziTocku() {
	
		GeometrijskiLik pravokutnik = Pravokutnik.STVARATELJ.stvoriIzStringa("5 5 30 20");
		Assert.assertTrue("Pravokutnik bi trebao sadržati svoj gornji lijevi kut.",
				pravokutnik.sadrziTocku(5, 5));
		Assert.assertTrue("Pravokutnik bi trebao sadržati tocku unutar sebe.",
				pravokutnik.sadrziTocku(10, 10));
		Assert.assertTrue("Pravokutnik bi trebao sadržati tocku na svojoj granici.",
				pravokutnik.sadrziTocku(34, 5));
		Assert.assertFalse("Pravokutnik ne bi smio sadržati točku izvan sebe.",
				pravokutnik.sadrziTocku(4, 4));
		Assert.assertFalse("Pravokutnik ne bi smio sadržati točku izvan sebe.",
				pravokutnik.sadrziTocku(35, 5));
		Assert.assertFalse("Pravokutnik ne bi smio sadržati točku izvan sebe.",
				pravokutnik.sadrziTocku(5, 25));
		Assert.assertFalse("Pravokutnik ne bi smio sadržati točku izvan sebe.",
				pravokutnik.sadrziTocku(35, 25));
		Assert.assertTrue("Pravokutnik bi trebao sadržati svoj gornji desni kut.",
				pravokutnik.sadrziTocku(34, 5));
		Assert.assertTrue("Pravokutnik bi trebao sadržati svoj donji lijevi kut.",
				pravokutnik.sadrziTocku(5, 24));
		Assert.assertTrue("Pravokutnik bi trebao sadržati svoj donji desni kut.",
				pravokutnik.sadrziTocku(34, 24));
	}
	
	/**
	 * Testira metodu za popunjavanje pravokutnika kada se on u nalazi unutar
	 * slike.
	 */
	@Test
	public void testirajPopuniLikUnutarSlike() {
	
		GeometrijskiLik pravokutnik = Pravokutnik.STVARATELJ.stvoriIzStringa("1 1 3 8");
		Slika slika = new Slika(5, 10);
		pravokutnik.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....");
		
		for (int i = 0; i < 8; i++) {
			graditeljOcekivanogIzlaza.append(".***.");
		}
		
		graditeljOcekivanogIzlaza.append(".....");
		Assert.assertEquals("Slika za pravokutnik nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje pravokutnika kada se on djelomično nalazi
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLikIzvanSlike() {
	
		GeometrijskiLik pravokutnik = Pravokutnik.STVARATELJ.stvoriIzStringa("-5 -5 10 15");
		Slika slika = new Slika(5, 10);
		pravokutnik.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		
		for (int i = 0; i < 10; i++) {
			graditeljOcekivanogIzlaza.append("*****");
		}
		
		Assert.assertEquals("Slika za pravokutnik nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
}
