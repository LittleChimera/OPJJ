package hr.fer.zemris.java.tecaj.hw4.collections.demo;

import hr.fer.zemris.java.tecaj.hw4.collections.SimpleHashtable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * This class demonstrates usage of <code>SimpleHashtable</code> class and
 * objects created from it.
 * 
 * @see SimpleHashtable
 * @author Domagoj Latečki
 *
 */
public class SimpleHashtableDemo {
	
	/**
	 * Main method which is called when program is executed. Arguments from the
	 * command line are ignored.
	 * 
	 * @param args arguments from the command line.
	 */
	public static void main(String[] args) {
	
		// create collection:
		SimpleHashtable examMarks = new SimpleHashtable(2);
		// fill data:
		examMarks.put("Ivana", Integer.valueOf(2));
		examMarks.put("Ante", Integer.valueOf(2));
		examMarks.put("Jasna", Integer.valueOf(2));
		examMarks.put("Kristina", Integer.valueOf(5));
		examMarks.put("Ivana", Integer.valueOf(5)); // overwrites old grade for
													// Ivana
		// query collection:
		Integer kristinaGrade = (Integer) examMarks.get("Kristina");
		System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes:
																			// 5
		// What is collection's size? Must be four!
		System.out.println("Number of stored pairs: " + examMarks.size()); // writes:
																			// 4
		System.out.println("");
		
		// create new collection:
		examMarks = new SimpleHashtable(2);
		// fill data:
		examMarks.put("Ivana", Integer.valueOf(2));
		examMarks.put("Ante", Integer.valueOf(2));
		examMarks.put("Jasna", Integer.valueOf(2));
		examMarks.put("Kristina", Integer.valueOf(5));
		examMarks.put("Ivana", Integer.valueOf(5)); // overwrites old grade for
													// Ivana
		for (SimpleHashtable.TableEntry pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
		System.out.println("");
		
		// cartesian product
		for (SimpleHashtable.TableEntry pair1 : examMarks) {
			for (SimpleHashtable.TableEntry pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", pair1.getKey(), pair1.getValue(),
						pair2.getKey(), pair2.getValue());
			}
		}
		System.out.println("");
		
		// iterator removal
		Iterator<SimpleHashtable.TableEntry> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove();
			}
		}
		System.out.println("Successfully removed entity while iterating.");
		for (SimpleHashtable.TableEntry pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
		System.out.println("");
		
		try {
			iter = examMarks.iterator();
			while (iter.hasNext()) {
				SimpleHashtable.TableEntry pair = iter.next();
				if (pair.getKey().equals("Kristina")) {
					iter.remove();
					iter.remove();
				}
			}
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException was cought as expected.");
		}
		// reset collection:
		examMarks = new SimpleHashtable(2);
		// fill data:
		examMarks.put("Ivana", Integer.valueOf(2));
		examMarks.put("Ante", Integer.valueOf(2));
		examMarks.put("Jasna", Integer.valueOf(2));
		examMarks.put("Kristina", Integer.valueOf(5));
		examMarks.put("Ivana", Integer.valueOf(5)); // overwrites old grade for
													// Ivana
		try {
			iter = examMarks.iterator();
			while (iter.hasNext()) {
				SimpleHashtable.TableEntry pair = iter.next();
				if (pair.getKey().equals("Ivana")) {
					examMarks.remove("Ivana");
				}
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("ConcurrentModificationException was cought as expected.");
		}
	}
}
