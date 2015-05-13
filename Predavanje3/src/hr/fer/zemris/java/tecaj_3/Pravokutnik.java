package hr.fer.zemris.java.tecaj_3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Pravokutnik extends GeometrijskiLik {
	
	private int vrhX;
	private int vrhY;
	private int sirina;
	private int visina;
	
	public Pravokutnik(String ime, int vrhX, int vrhY, int sirina, int visina) {
		super(ime);
		this.vrhX = vrhX;
		this.vrhY = vrhY;
		this.sirina = sirina;
		this.visina = visina;
	}
	
	
	@Override
	public boolean sadrziTocku(int x, int y) {
		if( x < vrhX || x >= vrhX + sirina) return false;
		if( y < vrhY || y >= vrhY + visina) return false;
		return true;
	}
	
	@Override
	public void popuniLik(Slika slika) {
		for (int y = vrhY, ye = vrhY + visina; y < ye; y++) {
			for (int x = vrhX, xe = vrhX + sirina; x < xe; x++) {
				slika.upaliTocku(x, y);
			}
		}
	}
	
}
