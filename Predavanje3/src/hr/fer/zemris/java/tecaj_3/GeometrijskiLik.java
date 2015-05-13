package hr.fer.zemris.java.tecaj_3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public abstract class GeometrijskiLik implements SadrziocTocaka {
	
	private String ime;
	
	public GeometrijskiLik(String ime) {
		super();
		this.ime = ime;
	}
	
	public String getIme() {
		return ime;
	}
	
	public double getOpseg() {
		return 0.0;
	}
	
	public double getPovrsina() {
		return 0.0;
	}
	
	// public abstract boolean sadrziTocku(int x, int y);
	
	public void popuniLik(Slika slika) {
		for (int y = 0, visina = slika.getVisina(), sirina = slika.getSirina(); y < visina; y++) {
			for (int x = 0; x < sirina; x++) {
				if (this.sadrziTocku(x, y)) {
					slika.upaliTocku(x, y);
				}
			}
		}
	}
}
