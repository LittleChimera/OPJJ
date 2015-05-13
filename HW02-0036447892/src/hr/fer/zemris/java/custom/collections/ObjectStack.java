package hr.fer.zemris.java.custom.collections;


/**
 * Razred <code>ObjectStack</code> je implementacija stoga preko liste, u kojem može raditi različitim metodama 
 * u stvarnom vremenu.<p> Složenost metoda push, pop i peek je O(1).
 * Instanca od <code>ObjectStack</code> sadrži listu ArrayBackedIndexedCollection koja glumi stog.
 * Kroz sami razred za realizaciju metoda stoga koristi ćemo metode iz razreda ArrayBackedIndexedCollection.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class ObjectStack {

	private ArrayBackedIndexedCollection stog;

	/**
	 * Konstruktor stvara instancu razreda stog u kojoj je kapcitet postavljen kao parametar.
	 * @param kapacitet kapacitet stoga.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public ObjectStack(int kapacitet){

		stog = new ArrayBackedIndexedCollection(kapacitet);		
	}

	/**
	 * Konstruktor stvara instancu razreda stog u kojoj je kapcitet postavljen na 16 po defaultu.
	 * @author Rade Bebek
	 * @since 2015-03-25 
	 */
	public ObjectStack(){		
		this(16);
	}

	/**
	 * Metoda <code>jePrazanStog</code> provjerava je li stog prazan.
	 * @return <code>true</code> ako je stog prazna,
	 * <code>false</code> ako ima elemenata u stogu.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public boolean jePrazanStog(){
		return stog.isEmpty();
	}

	/**
	 * Metoda <code>velicinaStoga</code> vraća veličinu stoga.
	 * @return veličina stoga.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int velicinaStoga(){
		return stog.size();
	}

	/**
	 * Metoda <code>obrisiStog</code> briše sve elemente iz stoga i postavlja ih na null vrijednost.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	void obrisiStog(){
		stog.clear();;
	}

	/**
	 * Metoda <code>push</code> dodaje referencu nekog objekta na stog.
	 * @param  vrijednost referenca koju treba dodati na stog.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public void push(Object vrijednost){
		stog.add(vrijednost);	
	}

	/**
	 * Metoda <code>pop</code> skida referencu nekog objekta sa stog.
	 * @param  vrijednost referenca koju treba skinuti sa stoga.
	 * @throws EmptyStackException - ako pokuša dohvatiti element s praznog stoga.
	 * @return vrhStoga vraća skinuti element.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Object pop() throws EmptyStackException{
		if(stog.getVelicina()==0){
			throw new EmptyStackException("Stog je prazan.");			
		}
		Object vrhStoga;
		vrhStoga=stog.get(stog.getVelicina()-1);
		stog.remove(stog.getVelicina()-1);
		return vrhStoga;		
	}

	/**
	 * Metoda <code>peek</code> vraća vrh stoga.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @return vrhStoga vraća vrh stoga
	 * @since 2015-03-25 
	 */
	public Object peek() {
		Object vrhStoga;
		vrhStoga=stog.get(stog.getVelicina()-1);
		return vrhStoga;		
	}
}
