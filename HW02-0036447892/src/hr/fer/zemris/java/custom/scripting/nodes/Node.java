package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.*;

/**
 * Razred <code>Node</code> sadrži tagove koji imaju dijecu.
 * @author Rade Bebek
 * @since 2015-03-25  
 */
public class Node {


	ArrayBackedIndexedCollection dijeca=null;

	/**
	 * Metoda <code>addChildNode</code> dodaje dijete nekom određenom čvoru.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public void addChildNode(Node dijete){
		if (dijeca==null) {
			dijeca = new ArrayBackedIndexedCollection();
			dijeca.add(dijete);
		} else {
			dijeca.add(dijete);
		}
	}

	/**
	 * Metoda <code>numberOfChildren</code> broj djece određenog čvora.
	 * @return broj dijece tipa int.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public int numberOfChildren(){
		return dijeca.size();	
	}

	/**
	 * Metoda <code>getChild</code> dohvaća dijete na nekom indeksu.
	 * @return dijete.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public Node getChild(int index){
		return (Node) dijeca.get(index);
	}

	/**
	 * Metoda <code>getchildren/code> provjerava je li čvor ima dijece.
	 * @return null ako nema dijece.
	 * @author Rade Bebek <rade.bebek@icloud.com>
	 * @since 2015-03-25 
	 */
	public ArrayBackedIndexedCollection getchildren(){
		return dijeca;
	}

}

