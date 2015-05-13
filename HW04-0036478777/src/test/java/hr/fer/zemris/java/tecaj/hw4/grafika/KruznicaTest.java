package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti kružnice.
 * 
 * @author Domagoj Latečki
 *
 */
public class KruznicaTest {
	
	/**
	 * Testira stvaratelja kružnice.
	 */
	@Test
	public void testirajStvaratelja() {
	
		GeometrijskiLik kruznica = Kruznica.STVARATELJ.stvoriIzStringa("10 10 20");
		Assert.assertTrue("Napravljeni geometrijski lik nije kružnica.",
				kruznica instanceof Kruznica);
	}
	
	/**
	 * Testira baca li stvaratelj kružnice iznimku kada je prevelik broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara1() {
	
		Kruznica.STVARATELJ.stvoriIzStringa("1 2 3 4");
	}
	
	/**
	 * Testira baca li stvaratelj kružnice iznimku kada je premali broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara2() {
	
		Kruznica.STVARATELJ.stvoriIzStringa("1 2");
	}
	
	/**
	 * Testira baca li stvaratelj elipse iznimku kada je zadani radijus
	 * negativan.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimParametrima() {
	
		Kruznica.STVARATELJ.stvoriIzStringa("1 2 -3");
	}
	
	/**
	 * Testira sadrži li stvaratelj ispravno ime za kružnicu.
	 */
	@Test
	public void testirajImeLikaUStvaratelju() {
	
		Assert.assertEquals("Stvaratelj ima krivi naziv lika.", "KRUG",
				Kruznica.STVARATELJ.nazivLika());
	}
	
	/**
	 * Testira metodu za sadržanje točke.
	 */
	@Test
	public void testirajSadrziTocku() {
	
		GeometrijskiLik kruznica = Kruznica.STVARATELJ.stvoriIzStringa("5 5 30");
		Assert.assertTrue("Kružnica bi trebala sadržati svoj centar.", kruznica.sadrziTocku(5, 5));
		Assert.assertTrue("Kružnica bi trebala sadržati tocku unutar sebe.",
				kruznica.sadrziTocku(10, 10));
		Assert.assertTrue("Kružnica bi trebala sadržati tocku na svojoj granici.",
				kruznica.sadrziTocku(35, 5));
		Assert.assertFalse("Kružnica ne bi smijela sadržati točku izvan sebe.",
				kruznica.sadrziTocku(36, 5));
	}
	
	/**
	 * Testira metodu za popunjavanje kružnice kada se ona u nalazi unutar
	 * slike.
	 */
	@Test
	public void testirajPopuniLikUnutarSlike() {
	
		GeometrijskiLik kruznica = Kruznica.STVARATELJ.stvoriIzStringa("2 2 2");
		Slika slika = new Slika(5, 4);
		kruznica.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("..*..");
		
		for (int i = 0; i < 2; i++) {
			graditeljOcekivanogIzlaza.append(".***.");
		}
		
		graditeljOcekivanogIzlaza.append("..*..");
		Assert.assertEquals("Slika za kružnicu nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje kružnice kada se ona djelomično nalazi
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLikIzvanSlike() {
	
		GeometrijskiLik kruznica = Kruznica.STVARATELJ.stvoriIzStringa("2 2 20");
		Slika slika = new Slika(5, 4);
		kruznica.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		
		for (int i = 0; i < 4; i++) {
			graditeljOcekivanogIzlaza.append("*****");
		}
		
		Assert.assertEquals("Slika za kružnicu nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
}
