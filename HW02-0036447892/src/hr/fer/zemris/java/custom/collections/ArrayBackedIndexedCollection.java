package hr.fer.zemris.java.custom.collections;


/**
 * Razred <code>ArrayBackedIndexedCollection</code> je implementacija promjenjive liste objekata, na kojima može raditi različitim metodama 
 * u stvarnom vremenu.<p> Složenost metoda add, size, isempty je O(1), metoda get, insert, remove, indexOf je O(n).
 * Zabranjeno je dodavanje null referenci u listu.
 * Instanca od <code>ArrayBackedIndexedCollection</code> sadrži kapacitet, velicinu i polje referenci objekata.
 * Ako kapacitet nije zadan kroz konstruktor, tada je 16, a inače je zadani broj. U slučaju da dođe do punog kapaciteta liste, dolazi do
 * automatskog povećavanja kapaciteta na duplu vrijednost, te to vrijeme nije uračunato u složenosti prethodno poisanih metoda.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class ArrayBackedIndexedCollection {

	private int velicina;
	private int kapacitet;
	private Object[] elementi;

	/**
	 * Konstruktor stvara instancu razreda u kojoj je kapcitet postavljen kao parametar.
	 * @param kapacitet kapacitet liste.
	 * @throws IllegalArgumentException - ako je zadan kapacitet manji od jedan.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public  ArrayBackedIndexedCollection(int kapacitet) throws IllegalArgumentException{	
		if(kapacitet<1){
			throw new IllegalArgumentException("Ne mozete stvoriti polje objekata kojima je velicina manja od 1");
		}
		else{
			this.velicina=0;	
			this.kapacitet = kapacitet;
			this.elementi= new Object[kapacitet];
		}
	}
	/**
	 * Konstruktor stvara instancu razreda u kojoj je kapcitet postavljen na 16 po defaultu.
	 * @throws IllegalArgumentException - ako je zadan kapacitet manji od jedan.
	 * @author Rade Bebek
	 * @since 2015-03-25 
	 */
	public  ArrayBackedIndexedCollection() throws IllegalArgumentException{
		this(16);
	}

	/**
	 * Metoda <code>prosiriKapacitet</code> automatski porširuje kapacitet kad lista bude puno,
	 * također je moguće i proširiti ručno kapcitet pozivajući ovu metodu a da lista i nije popunjena.
	 * @param  novikapacitet na kolko želimo proširiti.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25  
	 */
	public void prosiriKapacitet(int novikapacitet){
		Object[] novopolje;
		novopolje= new Object[kapacitet];
		for(int indeks=0; indeks<kapacitet; indeks++){
			novopolje[indeks]=elementi[indeks];
		}

		this.kapacitet = novikapacitet;
		this.elementi= new Object[novikapacitet];

		for(int indeks=0; indeks<novikapacitet/2; indeks++){
			elementi[indeks]=novopolje[indeks];
		}		
	}

	/**
	 * Metoda <code>add</code> dodaje referencu nekog objekta u listu.
	 * @param  vrijednost referenca koju treba dodati u listu.
	 * @throws IllegalArgumentException - ako se pokuša dodati null referenca.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public void add(Object vrijednost) throws IllegalArgumentException{
		if(vrijednost==null){
			throw new IllegalArgumentException("Ne mozete dodati null referencu");			
		}
		else{
			if(velicina==kapacitet){
				prosiriKapacitet(kapacitet*2);
			}		
			elementi[velicina]=vrijednost;
			velicina++;
		}	
	}

	/**
	 * Metoda <code>size</code> vraća veličinu liste.
	 * @return veličina liste.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int size(){
		return velicina;
	}

	/**
	 * Metoda <code>isEmpty</code> provjerava je li lista prazna.
	 * @return <code>true</code> ako je lista prazna,
	 * <code>false</code> ako ima elemenata u listi.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public boolean isEmpty(){
		if(velicina==0){
			return true;
		}
		else return false;
	}

	/**
	 * Metoda <code>Object get</code> dohvaća element koji je na zadanoj poziciji.
	 * @param  index element u listi koji treba dohvatiti.
	 * @throws IndexOutOfBoundsException - ako se pokuša dohvatiti nepostojeći indeks.
	 * @return vraća referencu dohvaćenog objekta.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Object get(int index) throws IndexOutOfBoundsException{
		if(index<0 || index>velicina-1){
			throw new IndexOutOfBoundsException("Niste unijeli dobar indeks.");
		}
		if(velicina==0){
			throw new IndexOutOfBoundsException("Lista je prazna.");
		}
		return elementi[index];		
	}

	/**
	 * Metoda <code>insert</code> ubacuje element na zadanoj poziciji u listi. Elemente na toj
	 * i većoj poziciji shifta za jedan u desno. U slučaju nestanka kapaciteta automatski povećava kapacitet.
	 * @param  vrijednost referenca koju je potrebno umetunuti.
	 * @param  pozicija pozicija na kojoj je potrebno umetnuti.
	 * @throws IndexOutOfBoundsException - ako se pokuša umetnuti u nepostojeći indeks liste.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public void insert(Object vrijednost, int pozicija) throws IndexOutOfBoundsException{
		if((pozicija<0 || pozicija>velicina) ){
			throw new IndexOutOfBoundsException("Niste unijeli dobaru poziciju.");
		}
		if(velicina==kapacitet){
			prosiriKapacitet(kapacitet*2);			
		}
		int zadnjiindeks=velicina-1;
		while(pozicija <= zadnjiindeks){
			elementi[zadnjiindeks+1]=elementi[zadnjiindeks];
			zadnjiindeks--;
		}
		elementi[pozicija]=vrijednost;
		velicina++;
	}

	/**
	 * Metoda <code>remove</code> briše element na zadanoj poziciji u listi.Prilikom
	 * brisanja reference sve članove shifta prema izbrisanoj poziciji, te zadnji postavlja 
	 * na null referencu.
	 * @param  pozicija pozicija na kojoj je potrebno izbrisati referencu.
	 * @throws IndexOutOfBoundsException - ako se pokuša izbrisati nepostojeća referenca.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public void remove(int pozicija) throws IndexOutOfBoundsException {
		if((pozicija<0 || pozicija>velicina) ){
			throw new IndexOutOfBoundsException("Niste unijeli dobaru poziciju.");
		}
		while(pozicija < velicina){
			elementi[pozicija]=elementi[pozicija+1];
			pozicija++;
		}
		velicina--;
	}

	/**
	 * Metoda <code>indexOf</code> vraća indeks elementa na kojem je zadana referenca.
	 * @param  objekt indeks traženog objekta.
	 * @return indeks ako indeks postoji, inače vraća -1.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int indexOf(Object objekt){
		for(int indeks=0; indeks<velicina; indeks++){
			if(objekt.equals(elementi[indeks])){
				return indeks;
			}
		}
		return -1;
	}

	/**
	 * Metoda <code>contains</code> provjerava jesu li objekti isti.
	 * @return <code>true</code> ako su identični,
	 * <code>false</code> ako su različiti.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public boolean contains(Object objekt){
		for(int indeks=0; indeks<velicina; indeks++){
			if(objekt.equals(elementi[indeks])){
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda <code>clear</code> briše sve elemente iz liste i postavlja ih na null vrijednost.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	void clear(){
		for(int indeks=0; indeks<velicina; indeks++){
			elementi[indeks]=null;
		}	
		this.velicina=0;
	}

	/**
	 * Metoda <code>getKapacitet</code> dohvaća trenutni kapacitet.
	 * @return kapacitet trenutni kapacitet.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int getKapacitet() {
		return kapacitet;
	}

	/**
	 * Metoda <code>getVelicina</code> dohvaća kolko trenutno ima elemenata.
	 * @return velicina vraća broj elemenata u listi.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int getVelicina() {
		return velicina;
	}

}