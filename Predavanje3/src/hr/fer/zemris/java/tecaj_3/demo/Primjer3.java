package hr.fer.zemris.java.tecaj_3.demo;

import hr.fer.zemris.java.tecaj_3.GeometrijskiLik;
import hr.fer.zemris.java.tecaj_3.Pravokutnik;
import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Primjer3 {
	

	public static void main(String[] args) {
		
		Slika slika = new Slika(100, 100);
		
		GeometrijskiLik[] likovi = new GeometrijskiLik[] {
				new Pravokutnik("p1", 1, 1, 1, 98),
				new Pravokutnik("p2", 10, 10, 70, 30),
				new Pravokutnik("p3", 10, 50, 70, 40)
		};
		
		for (GeometrijskiLik lik : likovi) {
			lik.popuniLik(slika);
		}
		
		slika.nacrtajSliku(System.out);
		
		PrikaznikSlike.prikaziSliku(slika, 4);
		
	}

}
