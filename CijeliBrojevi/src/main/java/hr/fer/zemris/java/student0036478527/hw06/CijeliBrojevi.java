package hr.fer.zemris.java.student0036478527.hw06;

/**
 * Razred koji provjerava neka svojstva cijelih brojeva.
 *
 */
public class CijeliBrojevi 
{
	
	/**
	* Provjerava je li dani broj neparan.
	* @param broj broj koji ce se provjeriti za neparnost
	* @return true ako je neparan, inace false 
	*/
	public boolean jeNeparan(int broj) {
		return broj % 2 != 0;
	}
	/**
	* Provjerava je li dani broj paran.
	* @param broj broj koji ce se provjeriti za parnost
	* @return true ako je paran, inace false 
	*/
	public boolean jeParan(int broj) {
		return broj % 2 == 0;
	}
	/**
	* Provjerava je li dani broj prost.
	* @param broj broj koji ce se provjeriti
	* @return true ako je prost, inace false 
	*/
	public boolean jeProst(int broj) {
		if (broj % 2 == 0) {
			return false;
		}
		for (int b = 3; b < Math.sqrt(broj) + 1; b+=2) {
			if (broj % b == 0) {
				return false;
			}
		}
		return true;
	}
}
