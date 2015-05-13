package hr.fer.zemris.java.tecaj.hw1;

/**
 * Demo using tree structure and its methods.
 * @author Luka Skugor
 * @version 1.0
 */
class ProgramStabla {
	static class CvorStabla {
		CvorStabla lijevi;
		CvorStabla desni;
		String podatak;
	}
	
	/**
	 * Called on program start
	 * @param args arguments from command line
	 */
	public static void main(String[] args) {
		CvorStabla cvor = null;
		
		cvor = ubaci(cvor, "Jasna");
		cvor = ubaci(cvor, "Ana");
		cvor = ubaci(cvor, "Ivana");
		cvor = ubaci(cvor, "Anamarija");
		cvor = ubaci(cvor, "Vesna");
		cvor = ubaci(cvor, "Kristina");
		
		System.out.println("Ispisujem stablo inorder:");
		ispisiStablo(cvor);
		
		int vel = velicinaStabla(cvor);
		System.out.println("Stablo sadrzi elemenata: " + vel);
		
		boolean pronaden = sadrziPodatak(cvor, "Ivana");
		System.out.println("Trazeni podatak je pronaden: " + pronaden);
	}
	
	/**
	 * Searches the tree for given element.
	 * @param korijen root of the tree
	 * @param podatak value to find
	 * @return true if value is found, else false
	 */
	static boolean sadrziPodatak(CvorStabla korijen, String podatak) {
		if(korijen == null) {
			return false;
		}
		
		CvorStabla propagateTo;
		int comparePodatci = korijen.podatak.compareTo(podatak);
		
		if(comparePodatci > 0) {
			propagateTo = korijen.lijevi;
		} else if (comparePodatci < 0) {
			propagateTo = korijen.desni;
		} else {
			return true;
		}
		
		return sadrziPodatak(propagateTo, podatak);
	}
	
	/**
	 * Counts number of elements in the tree.
	 * @param cvor root of the tree
	 * @return number of elements in the tree
	 */
	static int velicinaStabla(CvorStabla cvor) {
		int size = 1;
		
		if(cvor != null) {
			size += velicinaStabla(cvor.lijevi);
			size += velicinaStabla(cvor.desni);
			return size;
		} else {
			return 0;
		}
	}
	
	/**
	 * Adds an element to the tree.
	 * @param korijen root of the tree
	 * @param podatak data property of new element
	 * @return added element or existing if value of podatak is already in the tree
	 */
	static CvorStabla ubaci(CvorStabla korijen, String podatak) {
		if(podatak == null) {
			return korijen;
		}
		
		if(korijen == null) {
			korijen = new CvorStabla();
			korijen.podatak = podatak;
		}
		
		int comparePodatci = korijen.podatak.compareTo(podatak);
		
		if(comparePodatci > 0) {
			korijen.lijevi = ubaci(korijen.lijevi, podatak);
		} else if (comparePodatci < 0) {
			korijen.desni = ubaci(korijen.desni, podatak);
		} 
		
		
		return korijen;
	}
	
	/**
	 * Prints the three in-order.
	 * @param cvor root of the tree
	 */
	static void ispisiStablo(CvorStabla cvor) {
		if(cvor != null) {
			ispisiStablo(cvor.lijevi);
			System.out.println(cvor.podatak);
			ispisiStablo(cvor.desni);
		}
		
	}
}
