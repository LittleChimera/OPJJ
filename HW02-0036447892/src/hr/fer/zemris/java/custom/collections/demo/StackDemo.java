package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Razred <code>StackDemo</code> je razred koji parsita ulazni string, stavlja na stog elemente. Te kao 
 * rezultat sve dobijemo konačun vrijednost parsiranog niza.
 * Kao args[0] prima znakovni niz operatora i brojeva.
 * Da bi program radio mora dobiti ispravan redoslijed znakova, inače baca iznimku. Također u slučaju većeg broja argumenata
 * u args polju, daje upozorenje korisniku.<p>
 * Primjer 1: “8 2 /” rezultat 8/2=4.<p>
 * Primjer 2: “-1 8 2 / +”  rezultat je: 8/2=4, pa onda -1 + 4, i kao konačni rezultat 3.<p>
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class StackDemo {

	/**
	 * Metoda <code>provjeriOperater</code> provjerava je dani string operator.
	 * @return <code>true</code> ako je operatro, inače
	 * <code>false</code>.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private static boolean provjeriOperater(String znak){
		String[] operatori = new String[] {"+","-","*","/","%"};
		for(int indeks=0; indeks<operatori.length; indeks++){
			if(znak.equals(operatori[indeks])){
				return true;
			}
		}
		return false;	
	}

	/**
	 * Metoda koja se poziva prilikom pokretanja programa.
	 * @author Rade Bebek
	 * @since 2015-03-14  
	 */
	public static void main(String[] args) throws IllegalAccessException{

		if(args.length==0){
			System.out.println("Morate imati jedan argument tipa string.");
		}
		else{

			ObjectStack stog = new ObjectStack();

			String ulazstring = new String();
			ulazstring=args[0];
			String[] poljestringova;
			poljestringova = ulazstring.split(" ");
			System.out.println("Ulazni znakovni niz za parsiranje: "+ ulazstring);

			for(int indeks=0; indeks<poljestringova.length; indeks++){

				if( provjeriOperater(poljestringova[indeks])){

					try{
						char operater=poljestringova[indeks].charAt(0);
						int broj1=(Integer) stog.pop();
						int broj2=(Integer) stog.pop();
						int rezultat=0;

						switch(operater){						
						case '+':
							rezultat=broj2+broj1;
							stog.push(new Integer (rezultat));	
							break;	
						case '-':
							rezultat=broj2-broj1;
							stog.push(new Integer (rezultat));	
							break;
						case '*':
							rezultat=broj2*broj1;
							stog.push(new Integer (rezultat));	
							break;
						case '/':
							rezultat=broj2/broj1;
							stog.push(new Integer (rezultat));	
							break;
						case '%':
							rezultat=broj2%broj1;
							stog.push(new Integer (rezultat));	
							break;	
						}

					}
					catch(EmptyStackException ex){
						System.out.println("\nDali ste krivi string za parsiranje");
						System.exit(-1);
					}


				}
				else if(!poljestringova[indeks].equals("")){
					stog.push( Integer.parseInt(poljestringova[indeks]) );
				}

			}

			System.out.println("Rezultat: "+ stog.pop());
		}			

	}


}