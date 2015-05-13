package hr.fer.zemris.java.tecaj_1.brojevi;

/**
 * Ovaj razred sadrži razne funkcije za rad s prirodnim brojevima. Trenutno ne postoji
 * konsenzus što su točno prirodni brojevi -- jesu li to to pozitivni cijeli brojevi ili
 * su to nenegativni cijeli brojevi; razlika između ove dvije definicije je u broju
 * nula. Više detalja može se pogledati na, primjerice, 
 * <a href="http://mathworld.wolfram.com/NaturalNumber.html">ovoj adresi</a>. 
 * U okviru ovog razreda prirodnim brojevima će se smatrati pozitivni cijeli brojevi
 * (dakle, nula se neće tretirati kao pozitivan cijeli broj).
 *
 * <p>Evo i jednostavnog primjera uporabe. Da biste postavili vrijednost zastavice 
 * {@code prirodni} na {@code true} ako je predani broj {@code n} prirodni, možete
 * se poslužiti sljedećim isječkom koda.</p>
 * <pre>
 *   int n = 153;
 *   boolean prirodni = Prirodni.jePrirodni(n);
 *   System.out.println("Broj " + n + " je prirodni? " + prirodni);
 * </pre>
 *
 * @since 1.0
 * @author Marko Čupić
 */
public class Prirodni {

	/**
	 * Funkcija ispituje je li predani broj prirodni broj.
	 * @param n cijeli broj koji se ispituje
	 * @return <code>true</code> ako je predani cijeli broj ujedno i prirodni broj; inače vraća <code>false</code>.
	 */
	public static boolean jePrirodni(int n) {
		return n > 0;
	}

	/**
	 * Funkcija vraća sljedbenika od predanog broja.
	 * @param n broj čijeg sljedbenika treba vratiti
	 * @return <code>true</code> ako je predani cijeli broj ujedno i prirodni broj; inače vraća <code>false</code>.
	 */
	public static int sljedbenik(int n) {
		return n+1;
	}
	
}
