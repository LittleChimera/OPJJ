package hr.fer.zemris.java.custom.collections;

public class ObrisiMe {
	public static void main(String[] args) {
		String docBody = "{$Example$} \\{$=1$}. Now actually write one {$=1$}";
		ArrayBackedIndexedCollection collection = new ArrayBackedIndexedCollection();
		int indexFirst$ = 0;
		int indexSec$ = 0;
		String part1 = "";
		String part2 = "";
		docBody = docBody.replaceAll("(\\s)+", "$1");
		while (!docBody.isEmpty()) {

			indexFirst$ = docBody.indexOf("{$");
			indexSec$ = docBody.indexOf("$}");
			try {
				while (indexFirst$ > 0
						&& docBody.charAt(indexFirst$ - 1) == '\\') {
					indexFirst$ = docBody.indexOf("{$", indexFirst$ + 2);
				}
			} catch (IndexOutOfBoundsException e) {

			}
			if (indexFirst$ > 0) {
				part1 = docBody.substring(0, indexFirst$);
				System.out.println(part1);
				collection.add(part1);
				docBody = docBody.substring(indexFirst$);
				indexFirst$ = 0;
				indexSec$ = docBody.indexOf("$}");
			} else if (indexFirst$ < 0) {
				part1 = docBody;
				System.out.println(part1);
				collection.add(part1);
				docBody = "";
				break;
			}

			if (indexSec$ > 0) {
				part2 = docBody.substring(indexFirst$, indexSec$ + 2);
				collection.add(part2);
				System.out.println(part2);
				docBody = docBody.substring(indexSec$ + 2);

			} else {
				throw new IllegalArgumentException();
			}
		}
		
		
		ArrayBackedIndexedCollection col = new ArrayBackedIndexedCollection();
		some(col);
		System.out.println(col.size());
		System.out.println("0".matches("(\\d+.\\d+).*"));
	
	}
	
	
	public static void some(ArrayBackedIndexedCollection col){
			col.add("jajaj");
			col.add("jfjfjf");
	}
}
