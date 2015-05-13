package hr.fer.zemris.java.tecaj.hw4.grafika.demo;

import hr.fer.zemris.java.tecaj.hw4.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw4.grafika.Elipsa;
import hr.fer.zemris.java.tecaj.hw4.grafika.GeometrijskiLik;
import hr.fer.zemris.java.tecaj.hw4.grafika.Kruznica;
import hr.fer.zemris.java.tecaj.hw4.grafika.Kvadrat;
import hr.fer.zemris.java.tecaj.hw4.grafika.Linija;
import hr.fer.zemris.java.tecaj.hw4.grafika.Pravokutnik;
import hr.fer.zemris.java.tecaj.hw4.grafika.StvarateljLika;
import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Klasa koja služi za crtanje likova po ekranu.
 * 
 * @author Domagoj Latečki
 *
 */
public class Crtalo {
	
	/**
	 * Glavna metoda koja se pokreće kada se upali program. Prima tri argumenta
	 * it komandne linije: naziv puta do datoteke iz koje će se čitati, širinu
	 * slike i visinu slike (u pikselima).
	 * 
	 * @param args argumenti iz komandne linije.
	 * @throws IOException Ako datoteka nije pronađena ili se ne može otvoriti,
	 *             ova iznimka će biti bačena.
	 */
	public static void main(String[] args) throws IOException {
	
		if (args.length != 3) {
			System.err.println("Ocekuju se 3 argumenta!");
			System.exit(-1);
		}
		
		SimpleHashtable stvaratelji = podesi(Kvadrat.class, Pravokutnik.class, Kruznica.class,
				Elipsa.class, Linija.class);
		String[] definicije = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8)
				.toArray(new String[0]);
		GeometrijskiLik[] likovi = new GeometrijskiLik[definicije.length];
		for (int i = 0; i < definicije.length; i++) {
			if (!definicije[i].trim().isEmpty()) {
				String[] isjeckanaLinija = definicije[i].trim().split("\\s", 2);
				if (isjeckanaLinija.length != 2) {
					System.err.println("Jedan od likova nema zadane nikakve parametre!");
					System.exit(-2);
				}
				String lik = isjeckanaLinija[0];
				String parametri = isjeckanaLinija[1];
				StvarateljLika stvaratelj = (StvarateljLika) stvaratelji.get(lik);
				
				if (stvaratelj == null) {
					System.err.println("Navedni lik ne postoji!");
					System.exit(-3);
				}
				
				likovi[i] = stvaratelj.stvoriIzStringa(parametri);
			}
		}
		Slika slika = new Slika(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		for (GeometrijskiLik lik : likovi) {
			if (lik != null) {
				lik.popuniLik(slika);
			}
		}
		PrikaznikSlike.prikaziSliku(slika, 2);
	}
	
	/**
	 * Metoda koja stavlja razrede za stvaranje geometrijskih likova u hash
	 * mapu.
	 * 
	 * @param razredi lista razreda likova za koje će biti dohvaćeni razredi
	 *            stvaratelji.
	 * @return Mapu koja sadrži razrede stvaratelje za navedene likove.
	 */
	private static SimpleHashtable podesi(Class<?>... razredi) {
	
		SimpleHashtable stvaratelji = new SimpleHashtable();
		for (Class<?> razred : razredi) {
			try {
				Field field = razred.getDeclaredField("STVARATELJ");
				StvarateljLika stvaratelj = (StvarateljLika) field.get(null);
				stvaratelji.put(stvaratelj.nazivLika(), stvaratelj);
			} catch (Exception ex) {
				throw new RuntimeException("Nije moguće doći do stvaratelja za razred "
						+ razred.getName() + ".", ex);
			}
		}
		return stvaratelji;
	}
	
}
