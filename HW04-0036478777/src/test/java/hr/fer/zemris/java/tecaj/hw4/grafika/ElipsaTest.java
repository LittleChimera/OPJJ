package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti elipse.
 * 
 * @author Domagoj Latečki
 *
 */
public class ElipsaTest {
	
	/**
	 * Testira stvaratelja elipse.
	 */
	@Test
	public void testirajStvaratelja() {
	
		GeometrijskiLik elipsa = Elipsa.STVARATELJ.stvoriIzStringa("10 10 20 30");
		Assert.assertTrue("Napravljeni geometrijski lik nije elipsa.", elipsa instanceof Elipsa);
	}
	
	/**
	 * Testira baca li stvaratelj elipse iznimku kada je premali broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara1() {
	
		Elipsa.STVARATELJ.stvoriIzStringa("1 2 3");
	}
	
	/**
	 * Testira baca li stvaratelj elipse iznimku kada je prevelik broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara2() {
	
		Elipsa.STVARATELJ.stvoriIzStringa("1 2 3 4 5");
	}
	
	/**
	 * Testira baca li stvaratelj elipse iznimku kada je zadani radijus na osi x
	 * negativan.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima1() {
	
		Elipsa.STVARATELJ.stvoriIzStringa("1 2 -3 5");
	}
	
	/**
	 * Testira baca li stvaratelj elipse iznimku kada je zadani radijus na osi y
	 * negativan.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima2() {
	
		Elipsa.STVARATELJ.stvoriIzStringa("1 2 3 -5");
	}
	
	/**
	 * Testira sadrži li stvaratelj ispravno ime za elipsu.
	 */
	@Test
	public void testirajImeLikaUStvaratelju() {
	
		Assert.assertEquals("Stvaratelj ima krivi naziv lika.", "ELIPSA",
				Elipsa.STVARATELJ.nazivLika());
	}
	
	/**
	 * Testira metodu za sadržanje točke.
	 */
	@Test
	public void testirajSadrziTocku() {
	
		GeometrijskiLik elipsa = Elipsa.STVARATELJ.stvoriIzStringa("5 5 30 20");
		Assert.assertTrue("Elipsa bi trebala sadržati svoj centar.", elipsa.sadrziTocku(5, 5));
		Assert.assertTrue("Elipsa bi trebala sadržati tocku unutar sebe.",
				elipsa.sadrziTocku(10, 10));
		Assert.assertTrue("Elipsa bi trebala sadržati tocku na svojoj granici.",
				elipsa.sadrziTocku(35, 5));
		Assert.assertFalse("Elipsa ne bi smijela sadržati točku izvan sebe.",
				elipsa.sadrziTocku(36, 5));
	}
	
	/**
	 * Testira metodu za popunjavanje elipse kada se ona u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLikUnutarSlike() {
	
		GeometrijskiLik elipsa = Elipsa.STVARATELJ.stvoriIzStringa("2 5 2 5");
		Slika slika = new Slika(5, 10);
		elipsa.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("..*..");
		
		for (int i = 0; i < 8; i++) {
			graditeljOcekivanogIzlaza.append(".***.");
		}
		
		graditeljOcekivanogIzlaza.append("..*..");
		Assert.assertEquals("Slika za elipsu nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje elipse kada se ona djelomično nalazi izvan
	 * slike.
	 */
	@Test
	public void testirajPopuniLikIzvanSlike() {
	
		GeometrijskiLik elipsa = Elipsa.STVARATELJ.stvoriIzStringa("-5 -5 30 30");
		Slika slika = new Slika(5, 10);
		elipsa.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		
		for (int i = 0; i < 10; i++) {
			graditeljOcekivanogIzlaza.append("*****");
		}
		
		Assert.assertEquals("Slika za elipsu nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
}
