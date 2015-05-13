package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Razred za testiranje funkcionalnosti linije.
 * 
 * @author Domagoj Latečki
 *
 */
public class LinijaTest {
	
	/**
	 * Testira stvaratelja linije.
	 */
	@Test
	public void testirajStvaratelja() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("10 10 20 30");
		Assert.assertTrue("Napravljeni geometrijski lik nije linija.", linija instanceof Linija);
	}
	
	/**
	 * Testira baca li stvaratelj linije iznimku kada je premali broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara1() {
	
		Linija.STVARATELJ.stvoriIzStringa("1 2 3");
	}
	
	/**
	 * Testira baca li stvaratelj linije iznimku kada je prevelik broj
	 * parametara.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testirajStvarateljaSaKrivimBrojemParametara2() {
	
		Linija.STVARATELJ.stvoriIzStringa("1 2 3 4 5");
	}
	
	/**
	 * Testira sadrži li stvaratelj ispravno ime za liniju.
	 */
	@Test
	public void testirajImeLikaUStvaratelju() {
	
		Assert.assertEquals("Stvaratelj ima krivi naziv lika.", "LINIJA",
				Linija.STVARATELJ.nazivLika());
	}
	
	/**
	 * Testira metodu za sadržanje točke.
	 */
	@Test
	public void testirajSadrziTocku() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("5 5 30 20");
		Assert.assertTrue("Linija bi trebala sadržati svoju početnu točku.",
				linija.sadrziTocku(5, 5));
		Assert.assertTrue("Linija bi trebala sadržati svoju završnu točku.",
				linija.sadrziTocku(30, 20));
		Assert.assertTrue("Linija bi trebala sadržati točku kojom prolazi.",
				linija.sadrziTocku(10, 8));
		Assert.assertFalse("Linija ne bi smijela sadržati točku kojom ne prolazi.",
				linija.sadrziTocku(10, 7));
		
		linija = Linija.STVARATELJ.stvoriIzStringa("0 0 30 30");
		for (int x = 0; x <= 30; x++) {
			for (int y = 0; y <= 30; y++) {
				if (x == y) {
					Assert.assertTrue(linija.sadrziTocku(x, y));
				} else {
					Assert.assertFalse(linija.sadrziTocku(x, y));
				}
			}
		}
		Assert.assertFalse(linija.sadrziTocku(-1, -1));
		Assert.assertFalse(linija.sadrziTocku(31, 31));
		
		linija = Linija.STVARATELJ.stvoriIzStringa("0 30 30 0");
		for (int x = 0; x <= 30; x++) {
			for (int y = 0; y <= 30; y++) {
				if (x + y == 30) {
					Assert.assertTrue(linija.sadrziTocku(x, y));
				} else {
					Assert.assertFalse(linija.sadrziTocku(x, y));
				}
			}
		}
		Assert.assertFalse(linija.sadrziTocku(-1, 30));
		Assert.assertFalse(linija.sadrziTocku(31, -1));
		
		linija = Linija.STVARATELJ.stvoriIzStringa("30 30 0 0");
		for (int x = 0; x <= 30; x++) {
			for (int y = 0; y <= 30; y++) {
				if (x == y) {
					Assert.assertTrue(linija.sadrziTocku(x, y));
				} else {
					Assert.assertFalse(linija.sadrziTocku(x, y));
				}
			}
		}
		
		linija = Linija.STVARATELJ.stvoriIzStringa("30 0 0 30");
		for (int x = 0; x <= 30; x++) {
			for (int y = 0; y <= 30; y++) {
				if (x + y == 30) {
					Assert.assertTrue(linija.sadrziTocku(x, y));
				} else {
					Assert.assertFalse(linija.sadrziTocku(x, y));
				}
			}
		}
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike1() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 0 5 5");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append(".....*");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike2() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 5 5 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....*");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike3() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("5 5 0 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append(".....*");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike4() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("5 0 0 5");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....*");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike5() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 0 5 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("******");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike6() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("5 0 0 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("******");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike7() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 0 0 5");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi unutar slike.
	 */
	@Test
	public void testirajPopuniLinijuUnutarSlike8() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 5 0 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike1() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("-1 -1 6 6");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append(".....*");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike2() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("-1 6 6 -1");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....*");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike3() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("6 6 -1 -1");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append(".....*");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike4() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("6 -1 -1 6");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append(".....*");
		graditeljOcekivanogIzlaza.append("....*.");
		graditeljOcekivanogIzlaza.append("...*..");
		graditeljOcekivanogIzlaza.append("..*...");
		graditeljOcekivanogIzlaza.append(".*....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike5() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 0 6 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("******");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike6() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("6 0 0 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("******");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		graditeljOcekivanogIzlaza.append("......");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike7() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 0 0 6");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
	
	/**
	 * Testira metodu za popunjavanje linije kada se on u nalazi djelomično
	 * izvan slike.
	 */
	@Test
	public void testirajPopuniLinijuIzvanSlike8() {
	
		GeometrijskiLik linija = Linija.STVARATELJ.stvoriIzStringa("0 6 0 0");
		Slika slika = new Slika(6, 6);
		linija.popuniLik(slika);
		OutputStream izlaz = new ByteArrayOutputStream();
		slika.nacrtajSliku(izlaz);
		StringBuilder graditeljOcekivanogIzlaza = new StringBuilder();
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		graditeljOcekivanogIzlaza.append("*.....");
		Assert.assertEquals("Slika za liniju nije dobro nacrtana.",
				graditeljOcekivanogIzlaza.toString(), izlaz.toString().replaceAll("\\s", ""));
	}
}
