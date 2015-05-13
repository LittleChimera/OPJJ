package hr.fer.zemris.java.tecaj.hw1;

/**
 * Demo using list structure and its methods.
 * @author Luka Skugor
 * @version 1.0
 */
class ProgramListe {
	
	static class CvorListe {
		CvorListe sljedeci;
		String podatak;
	}
	
	/**
	 * Called on program start
	 * @param args arguments from command line
	 */
	public static void main(String[] args) {
		CvorListe cvor = null;
		
		cvor = ubaci(cvor, Integer.toString(6));
		cvor = ubaci(cvor, Integer.toString(8));
		cvor = ubaci(cvor, Integer.toString(1));
		cvor = ubaci(cvor, Integer.toString(8));
		cvor = ubaci(cvor, Integer.toString(2));
		
		System.out.println("Ispisujem listu uz originalni poredak:");
		ispisiListu(cvor);
		
		cvor = sortirajListu(cvor);
		
		System.out.println("Ispisujem listu nakon sortiranja:");
		ispisiListu(cvor);
		
		int vel = velicinaListe(cvor);
		System.out.println("Lista sadrzi elemenata: " + vel);
	}
	
	/**
	 * Counts number of elements in list starting from cvor.
	 * @param cvor first element of counted list
	 * @return number of connected CvorListe
	 */
	static int velicinaListe(CvorListe cvor) {
		int size = 0;
		CvorListe next = cvor;
		
		while (next != null) {
			next = next.sljedeci;
			size++;
		}
		
		return size;
	}
	
	/**
	 * Inserts an element at the beginning of the list.
	 * @param prvi first CvorListe in the list
	 * @param podatak data property of inserted CvorListe
	 * @return first element of the list
	 */
	static CvorListe ubaci(CvorListe prvi, String podatak) {
		if(podatak == null) {
			return prvi;
		}
		
		CvorListe inserted = new CvorListe();
		inserted.podatak = podatak;
		inserted.sljedeci = prvi;
		
		return inserted;
		
	}
	
	/**
	 * Prints the list from the given element to the end.
	 * To print the list from beginning to end, arguments needs
	 * to be first element of the list;
	 * @param cvor first element of the list
	 */
	static void ispisiListu(CvorListe cvor) {
		CvorListe next = cvor;
		
		while(next != null) {
			System.out.println(next.podatak);
			next = next.sljedeci;
		}
	}
	
	/**
	 * Sorts the list for given first element. Algorithm used has 
	 * complexity of O(n^2).
	 * @param cvor
	 * @return first element of the list
	 */
	static CvorListe sortirajListu(CvorListe cvor) {
		
		int size = velicinaListe(cvor);
		CvorListe start = cvor;
		CvorListe previous = null;
		CvorListe current = cvor;
		CvorListe next;
				
		//bubble sort
		for (int i = 0; i < size; i++) {
			while(current != null && current.sljedeci != null) {
				next = current.sljedeci;
				if(current.podatak.compareTo(next.podatak) > 0) {
					CvorListe afterNext = next.sljedeci;
					if(previous == null) {
						start = next;
						next.sljedeci = current;
						current.sljedeci = afterNext;
					} else {
						previous.sljedeci = next;
						next.sljedeci = current;
						current.sljedeci = afterNext;
					}
				} else {
					break;
				}
			}
		}
		
		return start;
	}
}
