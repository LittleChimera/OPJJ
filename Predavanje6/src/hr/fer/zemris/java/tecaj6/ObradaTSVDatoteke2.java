package hr.fer.zemris.java.tecaj6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ObradaTSVDatoteke2<T> {
	
	private String datoteka;
	
	public ObradaTSVDatoteke2(String datoteka) {
		super();
		this.datoteka = datoteka;
	}

	public final T ucitaj() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get(datoteka), StandardCharsets.UTF_8)) {
			int ocekivaniBrojStupaca = brojStupaca();
			while (true) {
				String redak = reader.readLine();
				if (redak == null)
					break;
				redak = redak.trim();
				redak = ukloniKomentart(redak.trim());

				if (redak.isEmpty())
					continue;
				String[] element = redak.split("\t");
				if (element.length != ocekivaniBrojStupaca) {
					throw new IOException();
				}
				obradiRedak(element);
			}
		}
		return dohvatiRezultat();
	}

	protected abstract int brojStupaca();

	protected abstract void obradiRedak(String[] elementi);

	protected abstract T dohvatiRezultat();

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

}
