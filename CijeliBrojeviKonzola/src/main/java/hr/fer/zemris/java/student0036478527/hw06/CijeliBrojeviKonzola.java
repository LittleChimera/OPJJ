package hr.fer.zemris.java.student0036478527.hw06;

import java.io.*;

/**
 * Konzolni program koji provjerava unesena cijele brojeve za parnost, neparnost i jeli broj prost. Program se zavrsava unosom kljucne rijeci "quit".
 *
 */
public class CijeliBrojeviKonzola 
{
	/**
	* Poziva se pri pokretanju programa.
	* @param args argumenti iz komandne linije
	*/
	public static void main( String[] args )
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print( "Unesite broj> " );
			int trenutni = 0;
			try {
				String line = br.readLine();
				if (line.trim().equalsIgnoreCase("quit")) {
					System.exit(0);
				}
				trenutni = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Niste unjeli broj!");
				continue;
			} catch (IOException e) {
				System.out.println("Input error!");
				System.exit(1);
			}
			CijeliBrojevi cb = new CijeliBrojevi();
			String paran = zamijeniSaDAiliNE(cb.jeParan(trenutni));
			String neparan = zamijeniSaDAiliNE(cb.jeNeparan(trenutni));
			String prost = zamijeniSaDAiliNE(cb.jeProst(trenutni));
			System.out.format("Paran: %s, neparan: %s, prost: %s%n", paran, neparan, prost);
		}
	}

	/**
	* Zamijeni true sa "DA" i false sa "NE".
	* @param b boolean po kojem ce se dobiti "DA" ili "NE"
	* @return odgovarajuci string ("DA" ili "NE")
	*/
	private static String zamijeniSaDAiliNE(boolean b) {
		return b?"DA":"NE";
	}
}
