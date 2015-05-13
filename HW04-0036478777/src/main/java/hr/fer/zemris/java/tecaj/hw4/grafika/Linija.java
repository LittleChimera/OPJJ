package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

/**
 * Klasa koja modelira liniju.
 * 
 * @author Domagoj Latečki
 *
 */
public class Linija extends GeometrijskiLik {
	
	/**
	 * x-koordinata početne točke.
	 */
	private int pocetniX;
	/**
	 * y-koordinata početne točke.
	 */
	private int pocetniY;
	/**
	 * x-koordinata završne točke.
	 */
	private int zavrsniX;
	/**
	 * y-koordinata završne točke.
	 */
	private int zavrsniY;
	/**
	 * Referenca na objekt koji služi za stvaranje linija.
	 */
	public static final StvarateljLika STVARATELJ = new LinijaStvaratelj();
	
	/**
	 * Konstruira novu liniju sa zadanim parametrima.
	 * 
	 * @param pocetniX x-koordinata početne točke.
	 * @param pocetniY y-koordinata početne točke.
	 * @param zavrsniX x-koordinata završne točke.
	 * @param zavrsniY y-koordinata završne točke.
	 */
	private Linija(int pocetniX, int pocetniY, int zavrsniX, int zavrsniY) {
	
		this.pocetniX = pocetniX;
		this.pocetniY = pocetniY;
		this.zavrsniX = zavrsniX;
		this.zavrsniY = zavrsniY;
	}
	
	@Override
	public boolean sadrziTocku(int x, int y) {
	
		int deltaX = zavrsniX - pocetniX;
		int deltaY = zavrsniY - pocetniY;
		int manjiX;
		int veciX;
		int manjiY;
		int veciY;
		if (pocetniX > zavrsniX) {
			manjiX = zavrsniX;
			veciX = pocetniX;
		} else {
			manjiX = pocetniX;
			veciX = zavrsniX;
		}
		if (pocetniY > zavrsniY) {
			manjiY = zavrsniY;
			veciY = pocetniY;
		} else {
			manjiY = pocetniY;
			veciY = zavrsniY;
		}
		if (deltaX != 0) {
			int izraz = deltaY * x / deltaX + pocetniY - deltaY * pocetniX / deltaX;
			int ispravak = Math.abs(deltaY / deltaX);
			ispravak += ispravak == 0 ? 1 : 0;
			if (y >= izraz && y < izraz + ispravak && y >= manjiY && y <= veciY && x >= manjiX
					&& x <= veciX) {
				return true;
			}
		} else if (y >= manjiY && y <= veciY && x == manjiX) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void popuniLik(Slika slika) {
	
		int sirina = slika.getSirina();
		int visina = slika.getVisina();
		int x = staviUnutarSlike(pocetniX, sirina);
		int y = staviUnutarSlike(pocetniY, visina);
		int krajX = staviUnutarSlike(zavrsniX, sirina);
		int krajY = staviUnutarSlike(zavrsniY, visina);
		int deltaX = krajX > x ? 1 : -1;
		int deltaY = krajY > y ? 1 : -1;
		
		while (true) {
			if (x >= sirina || y >= visina || x < 0 || y < 0) {
				break;
			}
			slika.upaliTocku(x, y);
			if (sadrziTocku(x + deltaX, y)) {
				x += deltaX;
			} else if (sadrziTocku(x, y + deltaY)) {
				y += deltaY;
			} else if (sadrziTocku(x + deltaX, y + deltaY)) {
				x += deltaX;
				y += deltaY;
			} else {
				break;
			}
		}
	}
	
	/**
	 * Ako je točka izvan zadane slike, postavlja koordinate točke unutar slike.
	 * 
	 * @param koordinata koordinata točke koja će biti provjerena.
	 * @param granica maksimalna vrijednost koordinate koju točka smije imati.
	 * @return Koordinatu točke unutar slike.
	 */
	protected int staviUnutarSlike(int koordinata, int granica) {
	
		int ispravljenaKoordinata = koordinata >= 0 ? koordinata : 0;
		ispravljenaKoordinata = ispravljenaKoordinata < granica ? ispravljenaKoordinata
				: granica - 1;
		return ispravljenaKoordinata;
	}
	
	/**
	 * Klasa koja služi za stvaranje linija. Ova klasa implementira sučelje
	 * <code>StvarateljLika</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class LinijaStvaratelj implements StvarateljLika {
		
		@Override
		public String nazivLika() {
		
			return "LINIJA";
		}
		
		/**
		 * Metoda koja će napraviti novi objekt za liniju sa zadanim
		 * parametrima. Linija ima 4 parametra, redom: x-koordinata početne
		 * točke, y-koordinata početne točke, x-koordinata završne točke,
		 * y-koordinata završne točke.
		 * 
		 * @param parametri string koji sadrži parametre za novu liniju.
		 * @return Nova linija koji je napravljena sa zadanim parametrima.
		 * @throws IllegalArgumentException Ako je zadan broj parametara
		 *             različit od 4.
		 */
		@Override
		public GeometrijskiLik stvoriIzStringa(String parametri) {
		
			String argumenti[] = parametri.trim().split("\\s+");
			if (argumenti.length != 4) {
				throw new IllegalArgumentException("Za liniju se ocekuju 4 broja u stringu.");
			}
			
			return new Linija(Integer.parseInt(argumenti[0]), Integer.parseInt(argumenti[1]),
					Integer.parseInt(argumenti[2]), Integer.parseInt(argumenti[3]));
		}
	}
}
