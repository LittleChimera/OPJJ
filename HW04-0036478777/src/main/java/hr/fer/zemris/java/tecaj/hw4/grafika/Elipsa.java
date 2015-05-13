package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

/**
 * Klasa koja modelira elipsu.
 * 
 * @author Domagoj Latečki
 *
 */
public class Elipsa extends GeometrijskiLik {
	
	/**
	 * x-koordinata centra elipse.
	 */
	private int centarX;
	/**
	 * y-koordinata centra celipse.
	 */
	private int centarY;
	/**
	 * Radijus elipse u smijeru osi x.
	 */
	private int radijusX;
	/**
	 * Radijus elipse u smijeru osi y.
	 */
	private int radijusY;
	/**
	 * Referenca na objekt koji služi za stvaranje elipsa.
	 */
	public static final StvarateljLika STVARATELJ = new ElipsaStvaratelj();
	
	/**
	 * Konstruira novu elipsu sa zadanim parametrima.
	 * 
	 * @param centarX x-koordinata centra elipse.
	 * @param centarY y-koordinata centra elipse.
	 * @param radijusX radijus elipse u smijeru osi x.
	 * @param radijusY radijus elipse u smijeru osi y.
	 * @throws IllegalArgumentException Ako je bilo koji zadani radijus manji od
	 *             0.
	 */
	protected Elipsa(int centarX, int centarY, int radijusX, int radijusY) {
	
		if (radijusX < 0 || radijusY < 0) {
			throw new IllegalArgumentException("Radijus ne moze biti negativan.");
		}
		
		this.centarX = centarX;
		this.centarY = centarY;
		this.radijusX = radijusX;
		this.radijusY = radijusY;
	}
	
	@Override
	public boolean sadrziTocku(int x, int y) {
	
		double vrijednostX = Math.pow(x - centarX, 2) / Math.pow(radijusX, 2);
		double vrijednostY = Math.pow(y - centarY, 2) / Math.pow(radijusY, 2);
		
		if (vrijednostX + vrijednostY - 1.0 < 10E-7) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void popuniLik(Slika slika) {
	
		int krajX = centarX + radijusX < slika.getSirina() ? centarX + radijusX : slika.getSirina();
		for (int x = centarX - radijusX >= 0 ? centarX - radijusX : 0; x < krajX; x++) {
			int granica = (int) Math.sqrt(Math.pow(radijusY, 2)
					* (1 - Math.pow(x - centarX, 2) / Math.pow(radijusX, 2)));
			int krajnjiY = granica + centarY < slika.getVisina() ? granica + centarY : slika
					.getVisina();
			for (int y = centarY - granica >= 0 ? centarY - granica : 0; y < krajnjiY; y++) {
				slika.upaliTocku(x, y);
			}
		}
	}
	
	/**
	 * Klasa koja služi za stvaranje elipsa. Ova klasa implementira sučelje
	 * <code>StvarateljLika</code>.
	 * 
	 * @author Domagoj Latečki
	 *
	 */
	public static class ElipsaStvaratelj implements StvarateljLika {
		
		@Override
		public String nazivLika() {
		
			return "ELIPSA";
		}
		
		/**
		 * Metoda koja će napraviti novi objekt za elipsu sa zadanim
		 * parametrima. Elipsa ima 4 parametra, redom: x-koordinata centra
		 * elipse, y-koordinata centra elipse, radijus u smijeru osi x, radijus
		 * u smijeru osi y.
		 * 
		 * @param parametri string koji sadrži parametre za novu elipsu.
		 * @return Nova elipsa koji je napravljena sa zadanim parametrima.
		 * @throws IllegalArgumentException Ako je zadan broj parametara
		 *             različit od 4, ili ako je bilo koji zadani radijus manji
		 *             od 0.
		 */
		@Override
		public GeometrijskiLik stvoriIzStringa(String parametri) {
		
			String argumenti[] = parametri.trim().split("\\s+");
			if (argumenti.length != 4) {
				throw new IllegalArgumentException("Za elipsu se ocekuju 4 broja u stringu.");
			}
			
			return new Elipsa(Integer.parseInt(argumenti[0]), Integer.parseInt(argumenti[1]),
					Integer.parseInt(argumenti[2]), Integer.parseInt(argumenti[3]));
		}
	}
}
