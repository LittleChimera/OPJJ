package hr.fer.zemris.java.tecaj6;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpisiBolji2 {
	private static class Student {
		private String jmbag;
		private String prezime;
		private String ime;

		public Student(String jmbag, String prezime, String ime) {
			this.jmbag = jmbag;
			this.prezime = prezime;
			this.ime = ime;
		}

		public String toString() {
			return "[" + jmbag + "]" + " " + ime + " " + prezime;
		}
	}

	public static class Predmet {
		private String sifra;
		private String naziv;

		public Predmet(String sifra, String naziv) {
			this.sifra = sifra;
			this.naziv = naziv;
		}

		public String toString() {
			return naziv + "[" + sifra + "]";
		}
	}

	private static class Upis {
		private Student student;
		private Predmet predmet;

		public Upis(Student student, Predmet predmet) {
			this.student = student;
			this.predmet = predmet;
		}

		public String toString() {
			return student.toString() + "=" + predmet.toString();
		}
	}

	public static void main(String[] args) throws IOException {
		Map<String, Student> studenti = ucitajStudente();
		Map<String, Predmet> predmeti = ucitajPredmete();
		List<Upis> upisi = ucitajUpise(studenti, predmeti);
		for (Upis u : upisi) {
			System.out.println(u);
		}
	}

	private static String ukloniKomentart(String redak) {
		int indeks = redak.indexOf('#');
		if (indeks != -1) {
			redak = redak.substring(0, indeks).trim();
			if (redak.isEmpty())
				return "";
		}
		indeks = redak.indexOf('%');
		if (indeks != -1) {
			redak = redak.substring(0, indeks).trim();
			if (redak.isEmpty())
				return "";
		}
		if (redak.contains("REM")) {
			return "";
		}
		return redak;
	}

	private static List<Upis> ucitajUpise(Map<String, Student> studenti,
			Map<String, Predmet> predmeti) throws IOException {
		List<Upis> lista = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(
						Files.newInputStream(Paths.get("./data/upisi.txt"),
								StandardOpenOption.READ)),
						StandardCharsets.UTF_8))) {
			while (true) {
				String redak = reader.readLine();
				if (redak == null)
					break;
				redak = redak.trim();
				redak = ukloniKomentart(redak.trim());
				if (redak.isEmpty())
					continue;
				String[] element = redak.split("\t");
				lista.add(new Upis(studenti.get(element[0]), predmeti
						.get(element[1])));
			}
		}
		return lista;
	}

	private static Map<String, Predmet> ucitajPredmete() throws IOException {
		Map<String, Predmet> mapa = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(Files.newInputStream(
						Paths.get("./data/predmeti.txt"),
						StandardOpenOption.READ)), StandardCharsets.UTF_8))) {
			while (true) {
				String redak = reader.readLine();
				if (redak == null)
					break;
				redak = redak.trim();
				redak = ukloniKomentart(redak.trim());

				if (redak.isEmpty())
					continue;
				String[] element = redak.split("\t");
				mapa.put(element[0], new Predmet(element[0], element[1]));
			}
		}
		return mapa;
	}

	private static Map<String, Student> ucitajStudente() throws IOException {

		return new ObradaTSVDatoteke2<Map<String, Student>>(
				"./data/studenti.txt") {

			private Map<String, Student> mapa = new HashMap<String, UpisiBolji2.Student>();

			@Override
			protected int brojStupaca() {
				return 3;
			}

			@Override
			protected void obradiRedak(String[] elementi) {
				mapa.put(elementi[0], new Student(elementi[0], elementi[1],
						elementi[2]));
			}

			@Override
			protected Map<String, Student> dohvatiRezultat() {
				return mapa;
			}
		}.ucitaj();
	}
}