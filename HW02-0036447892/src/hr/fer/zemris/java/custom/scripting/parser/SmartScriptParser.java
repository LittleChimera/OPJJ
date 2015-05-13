package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;

/**
 * Razred <code>SmartScriptParser</code> je implementacija parsera za zadani string.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class SmartScriptParser {


	private String kod;
	private ObjectStack stog = new ObjectStack();
	private DocumentNode cvor = new DocumentNode();
	private ArrayBackedIndexedCollection lista= new ArrayBackedIndexedCollection();
	private Node vrhstoga;

	/**
	 * Konstruktor poziva metode za parsiranje stringa.
	 * @param kod string za parsiranje.
	 * @author Rade Bebek
	 * @since 2015-03-25  
	 */
	public SmartScriptParser(String kod) throws IllegalAccessException {
		this.kod = kod;
		stog.push(cvor); 
		napravilistu(this.kod); 
		parsirajlistu(lista);
	}

	/**
	 * Metoda <code>getCvor</code> dohvaca Document node.
	 * @return cvor Documennode cvor za potrebe ispisa.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public DocumentNode getCvor() {
		return cvor;
	}

	/**
	 * Metoda <code>izreziString</code> razdvaja tag elemente
	 * i tekst elemente i vraca pozicije tih elemenata
	 * @param kod string za rezanje
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private int[] izreziString(String kod){
		int[] polje={0,0};
		String SubStr1 = new String("{$");
		String SubStr2 = new String("$}");
		polje[0]=kod.indexOf(SubStr1);
		polje[1]=kod.indexOf(SubStr2);
		return polje;		
	}

	/**
	 * Metoda <code>napravilistu</code> pravi listu sa svim tagovima
	 * i tekst cvorovima. 
	 * @param kod string za razdvajanje u listu.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private void napravilistu(String kod) throws IllegalAccessException{

		int indeks=0;
		while(indeks<kod.length()){	
			int polje[]=null;
			polje=izreziString(kod);
			if(polje[0]!=polje[1]){
				lista.add(kod.substring(indeks, polje[0]));
				lista.add(kod.substring(polje[0], polje[1]+2));
				kod=kod.substring(polje[1]+2, kod.length());
			}
			else{
				lista.add(kod.substring(0,kod.length()));
				kod="";
			}

		}		
	}

	/**
	 * Metoda <code>parsirajlistu</code> parsira sve clanove liste 
	 * ovisno je li tag, ili je tekst.
	 * @param lista lista za parsiranje.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private void parsirajlistu(ArrayBackedIndexedCollection lista){

		int velicinaliste=lista.getVelicina();
		for(int indeks=0; indeks<velicinaliste; indeks++){
			String clan=(String) lista.get(indeks);
			if(clan.startsWith("{$")){

				String tag=clan.substring(2, clan.length()-2);
				String podniz=tag.trim();
				podniz=podniz.toUpperCase();
				int jefor=podniz.indexOf("FOR");
				if(podniz.startsWith("=")){
					String echostring=posebniSlucajevi(tag);		
					parsirajECHO(echostring);
				}
				else if(jefor==-1){
					parsirajEND();
				}
				else{
					String forstring=posebniSlucajevi(tag);	
					if(cvor!=(Node) stog.peek()){
						System.out.println("Svaki FOR mora imati svoj END");
						System.exit(-1);
					}
					parsirajFOR(forstring);
				}
			}
			else{
				String noviclan=posebniSlucajevi(clan);
				TextNode tekst= new TextNode(noviclan);
				vrhstoga=(Node) stog.peek();
				vrhstoga.addChildNode(tekst);
			}			
		}		
	}

	/**
	 * Metoda <code>parsirajEND</code> parsira END tag.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private void parsirajEND (){
		stog.pop();		
	}


	/**
	 * Metoda <code>parsirajFOR</code> parsira FOR tag.
	 * @param tag tekst unutar for taga
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private void parsirajFOR (String tag){

		String[] polje=null;

		boolean jelivariajbla=true;
		boolean iznimka=true;

		String pomocniniz = tag.toUpperCase();	
		int tagpocetak=pomocniniz.indexOf("FOR");
		tag=tag.substring(tagpocetak+3, tag.length());	
		polje=tag.split(" ");
		int  velicinapolja= polje.length;
		Token[] tokeni = new Token[velicinapolja];
		int brojtokena=0;
		for(int indeks=0; indeks<velicinapolja; indeks++){

			if(!polje[indeks].equals("")){

				if(jelivariajbla){
					TokenVariable varijabla= new TokenVariable(polje[indeks]);
					jelivariajbla=false;
					tokeni[brojtokena]=varijabla;
					brojtokena++;
				}
				else{

					if (iznimka){

						try{
							int clan=Integer.parseInt(polje[indeks]);
							TokenConstantInteger broj= new TokenConstantInteger(clan);
							tokeni[brojtokena]=broj;
							brojtokena++;
							iznimka=false;

						}catch(NumberFormatException ex){

						}
					}

					if (iznimka){

						try{
							double clan=Double.parseDouble(polje[indeks]);
							TokenConstantDouble broj= new TokenConstantDouble(clan);
							tokeni[brojtokena]=broj;
							brojtokena++;
							iznimka=false;

						}catch(NumberFormatException ex){

						}
					}
					if (iznimka){

						String clan=(polje[indeks]);
						if( '"'==clan.charAt(0)){
							TokenString string= new TokenString (clan);
							tokeni[brojtokena]=string;
							brojtokena++;
						}
						if(true==clan.matches("[a-zA-Z]+")){

							TokenVariable string= new TokenVariable(clan);
							tokeni[brojtokena]=string;
							brojtokena++;
						}
						else{
							System.out.println("U FOR  petlji ne mogu biti funkcije ni operatori");
							System.exit(-1);
						}
					}			
					iznimka=true;	
				}						
			}
			else{

			}			
		}
		if(brojtokena==4){
			ForLoopNode forpetlja= new ForLoopNode(tokeni[0],tokeni[1],tokeni[2],tokeni[3]);
			vrhstoga=(Node) stog.peek();
			vrhstoga.addChildNode(forpetlja);
			stog.push(forpetlja);
		}
		else if(brojtokena==3){
			ForLoopNode forpetlja= new ForLoopNode(tokeni[0],tokeni[1],tokeni[2],null);
			vrhstoga=(Node) stog.peek();
			vrhstoga.addChildNode(forpetlja);
			stog.push(forpetlja);	
		}
		else{
			System.out.println("Broj tokena je pogresan u jednom od FOR tagova");
		}

	}

	/**
	 * Metoda <code>parsirajECHO</code> parsira ECHO tag.
	 * @param tag tekst unutar ECHO taga
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private void parsirajECHO (String tag){

		String[] polje=null;

		boolean iznimka=true;
		tag=tag.substring(1, tag.length());	
		tag=tag.trim();
		polje=tag.split("\\s+");
		int  velicinapolja= polje.length;
		Token[] tokeni = new Token[velicinapolja];

		int brojtokena=0;

		for(int indeks=0; indeks<velicinapolja; indeks++){

			if(!polje[indeks].equals("")){

				if (iznimka){

					try{
						int clan=Integer.parseInt(polje[indeks]);
						TokenConstantInteger broj= new TokenConstantInteger(clan);
						tokeni[brojtokena]=broj;
						brojtokena++;
						iznimka=false;

					}catch(NumberFormatException ex){

					}
				}
				if (iznimka){

					try{
						double clan=Double.parseDouble(polje[indeks]);
						TokenConstantDouble broj= new TokenConstantDouble(clan);
						tokeni[brojtokena]=broj;
						brojtokena++;
						iznimka=false;

					}catch(NumberFormatException ex){

					}
				}
				if (iznimka){

					String clan=(polje[indeks]);
					if( '"'==clan.charAt(0)){
						TokenString string= new TokenString (clan);
						tokeni[brojtokena]=string;
						brojtokena++;
					}
					else if(clan.startsWith("@")){
						TokenFunction string= new TokenFunction (clan);
						tokeni[brojtokena]=string;
						brojtokena++;	
					}
					else if( ( ('%'==clan.charAt(0)) || ('/'==clan.charAt(0)) || ('-'==clan.charAt(0)) || ('*'==clan.charAt(0)) || ('+'==clan.charAt(0)) ) && clan.length()==1){
						TokenOperator string= new TokenOperator (clan);
						tokeni[brojtokena]=string;
						brojtokena++;
					}
					else{
						TokenVariable varijabla= new TokenVariable(polje[indeks]);
						tokeni[brojtokena]=varijabla;
						brojtokena++;
					}

				}
				iznimka=true;
			}
			else{

			}			
		}

		EchoNode echo= new EchoNode(tokeni);
		vrhstoga=(Node) stog.peek();
		vrhstoga.addChildNode(echo);


	}

	/**
	 * Metoda <code>posebniSlucajevi</code> izbaciva \\ iz teksta.
	 * @param tekst tekst za obradu.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	private String posebniSlucajevi(String tekst){	
		tekst=tekst.replaceAll("\\\\\\\\", "\\\\");
		return tekst;

	}



}	


