package hr.fer.zemris.java.tecaj6;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class ObradaTSVDatoteke {

	public static <T> T ucija(String datotake, IObrada<T> obrada)
			throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get(datotake), StandardCharsets.UTF_8)) {
			int ocekivaniBrojStupaca = obrada.brojStupaca();
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
				obrada.obradiRedak(element);
			}
		}
		return obrada.dohvatiRezultat();
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

}
