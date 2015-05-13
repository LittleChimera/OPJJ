package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti kvadrata.
 * 
 * @author Domagoj Latečki
 *
 */
public class KvadratTest {
	
	/**
	 * Testira stvaratelja kvadrata.
	 */
	@Test
	public void testirajStvaratelja() {
	
		GeometrijskiLik kvadrat = Kvadrat.STVARATELJ.stvoriIzStringa("10 10 20");
		Assert.assertTrue("Napravljeni geometrijski lik nije kvadrat.", kvadrat instanceof Kvadrat);
	}
	
	/**
	 * Testira baca li stvaratelj kvadrata iznimku kada je prevelik broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara1() {
	
		Kvadrat.STVARATELJ.stvoriIzStringa("1 2 3 4 5");
	}
	
	/**
	 * Testira baca li stvaratelj kvadrata iznimku kada je premali broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara2() {
	
		Kvadrat.STVARATELJ.stvoriIzStringa("1 2");
	}
	
	/**
	 * Testira baca li stvaratelj kvadrata iznimku kada je zadana duljina
	 * negativna.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima1() {
	
		Kvadrat.STVARATELJ.stvoriIzStringa("1 2 -3");
	}
	
	/**
	 * Testira baca li stvaratelj kvadrata iznimku kada je zadana duljina
	 * negativna, kao i četvrti parametar.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima2() {
	
		Kvadrat.STVARATELJ.stvoriIzStringa("1 2 -3 -3");
	}
	
	/**
	 * Testira baca li stvaratelj kvadrata iznimku kada je četvrti parametar
	 * razičit od duljine.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima3() {
	
		Kvadrat.STVARATELJ.stvoriIzStringa("1 2 3 4");
	}
	
	/**
	 * Testira sadrži li stvaratelj ispravno ime za kvadrat.
	 */
	@Test
	public void testirajImeLikaUStvaratelju() {
	
		Assert.assertEquals("Stvaratelj ima krivi naziv lika.", "KVADRAT",
				Kvadrat.STVARATELJ.nazivLika());
	}
	
	/**
	 * Testira metodu za sadržanje točke.
	 */
	@Test
	public void testirajSadrziTocku() {
	
		GeometrijskiLik kvadrat = Kvadrat.STVARATELJ.stvoriIzStringa("5 5 30");
		Assert.assertTrue("Kvadrat bi trebao sadržati svoj gornji lijevi kut.",
				kvadrat.sadrziTocku(5, 5));
		Assert.assertTrue("Kvadrat bi trebao sadržati tocku unutar sebe.",
				kvadrat.sadrziTocku(10, 10));
		Assert.assertTrue("Kvadrat bi trebao sadržati tocku na svojoj granici.",
				kvadrat.sadrziTocku(34, 5));
		Assert.assertFalse("Kvadrat ne bi smio sadržati točku izvan sebe.",
				kvadrat.sadrziTocku(4, 4));
		Assert.assertFalse("Kvadrat ne bi smio sadržati točku izvan sebe.",
				kvadrat.sadrziTocku(35, 5));
		Assert.assertFalse("Kvadrat ne bi smio sadržati točku izvan sebe.",
				kvadrat.sadrziTocku(5, 35));
		Assert.assertFalse("Kvadrat ne bi smio sadržati točku izvan sebe.",
				kvadrat.sadrziTocku(35, 35));
		Assert.assertTrue("Kvadrat bi trebao sadržati svoj gornji desni kut.",
				kvadrat.sadrziTocku(34, 5));
		Assert.assertTrue("Kvadrat bi trebao sadržati svoj donji lijevi kut.",
				kvadrat.sadrziTocku(5, 34));
		Assert.assertTrue("Kvadrat bi trebao sadržati svoj donji desni kut.",
				kvadrat.sadrziTocku(34, 34));
	}
	
	/**
	 * Testira metodu za popunjavanje kvadarata kada se on u nalazi unutar
	 * slike.
	 */
	@Test
	public void testirajPopuniLikUnutarSlike() {
	
		GeometrijskiLik kvadrat = Kvadrat.STVARATELJ.stvoriIzStringa("1 1 3 3");
		Slika slika = new Slika(5, 5);
		kvadrat.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....");
		
		for (int i = 0; i < 3; i++) {
			graditeljOcekivanogIzlaza.append(".***.");
		}
		
		graditeljOcekivanogIzlaza.append(".....");
		Assert.assertEquals("Slika za kvadrat nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje kvadrata kada se on djelomično nalazi
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLikIzvanSlike() {
	
		GeometrijskiLik kvadrat = Kvadrat.STVARATELJ.stvoriIzStringa("-5 -5 10 10");
		Slika slika = new Slika(5, 5);
		kvadrat.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		
		for (int i = 0; i < 5; i++) {
			graditeljOcekivanogIzlaza.append("*****");
		}
		
		Assert.assertEquals("Slika za kvadrat nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
}
