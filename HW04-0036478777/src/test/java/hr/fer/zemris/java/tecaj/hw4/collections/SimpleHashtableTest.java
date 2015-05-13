package hr.fer.zemris.java.tecaj.hw4.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class SimpleHashtableTest {
	

	@Test
	public void constructorRoundHigher() {
		assertEquals(8, (new SimpleHashtable(6)).capacity());
	}
	
	@Test
	public void constructorRoundSame() {
		assertEquals(32, (new SimpleHashtable(32)).capacity());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorSizeNegative() {
		new SimpleHashtable(0);
	}
	
	@Test
	public void constructorDefaultSize() {
		assertEquals(16, (new SimpleHashtable()).capacity());
	}
	
	@Test
	public void putAndGetEntry() {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		
		String key = "sing";
		String value = "shalalala";
		sampleHashtable.put(key, value);
		String getValue = (String)sampleHashtable.get(key);
		assertEquals(value, getValue);
	}
	
	@Test
	public void modifyValue() {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		
		String key = "sing";
		String value1 = "shalalala";
		String value2 = "lala lala shalalala";
		sampleHashtable.put(key, value1);
		sampleHashtable.put(key, value2);
		String getValue = (String)sampleHashtable.get(key);
		assertEquals(value2, getValue);
		assertEquals(1, sampleHashtable.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void putNullKey() {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		sampleHashtable.put(null, "asdfasd");
	}
	
	@Test
	public void getNullKey() {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		assertEquals(null, sampleHashtable.get(null));
	}
	
	@Test
	public void getNonexistingKey() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		assertEquals(null, sampleHashtable.get("shalala"));
	}
	
	@Test
	public void getExistingKeys() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(50, "la", "shh");
		
		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			keyBuilder.append("la");
			valueBuilder.append("shh");
			assertEquals(valueBuilder.toString(), sampleHashtable.get(keyBuilder.toString()));
		}
	}
	
	@Test
	public void containsNullKey() {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		assertEquals(false, sampleHashtable.containsKey(null));
	}
	
	@Test
	public void containsSomeKeyTrue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		
		assertEquals(true, sampleHashtable.containsKey("lalala"));
	}
	
	@Test
	public void containsSomeKeyFalse() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		
		assertEquals(false, sampleHashtable.containsKey("shalalala"));
	}
	
	@Test
	public void containsNullValueTrue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		sampleHashtable.put("ulalal", null);
		
		assertEquals(true, sampleHashtable.containsValue(null));
	}
	
	@Test
	public void containsNullValueFalse() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		assertEquals(false, sampleHashtable.containsValue(null));
	}
	
	@Test
	public void containsSomeValueTrue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		
		assertEquals(true, sampleHashtable.containsValue("shhshh"));
	}
	
	@Test
	public void containsSomeValueFalse() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		
		assertEquals(false, sampleHashtable.containsValue("shhhshh"));
	}
	
	@Test
	public void removeExistingValue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		sampleHashtable.remove("lala");
		assertEquals(9, sampleHashtable.size());
		assertEquals(null, sampleHashtable.get("lala"));
	}
	
	@Test
	public void removeAtLeastOneExistingNonFirstElement() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(767, "la", "shh");
		String currentKey = String.format("%0767" + "d", 0).replace("0","la");
		for (int i = 0; i < 767; i++) {
			sampleHashtable.remove(currentKey);
			currentKey = currentKey.substring(0, currentKey.length()-2);
		}
		sampleHashtable.remove("lala");
		assertEquals(0, sampleHashtable.size());
	}
	
	@Test
	public void removeNonexistingValue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		sampleHashtable.remove("lala sing");
		assertEquals(10, sampleHashtable.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void removeNullValue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		sampleHashtable.remove(null);
	}
	
	@Test
	public void isEmptyTrue() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(0, "la", "shh");
		assertEquals(true, sampleHashtable.isEmpty());
	}
	
	@Test
	public void isEmptyFalse() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(1, "la", "shh");
		assertEquals(false, sampleHashtable.isEmpty());
	}
	
	@Test
	public void clear() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(10, "la", "shh");
		sampleHashtable.clear();
		assertEquals(0, sampleHashtable.size());
		
		StringBuilder keyBuilder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			keyBuilder.append("la");
			assertEquals(null, sampleHashtable.get(keyBuilder.toString()));
		}
	}
	
	
	
	@Test
	public void overFlowTable() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(24, "la", "shh");
		assertEquals(64, sampleHashtable.capacity());
	}
	
	@Test
	public void getAsString() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(23, "la", "shh");
		sampleHashtable.toString();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createEntryWithNullKey() {
		new SimpleHashtable.TableEntry(null, "asdf", null);
	}
	
	@Test(expected=IllegalStateException.class)
	public void entryRemovedTwice() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(20, "la", "shh");
		Iterator<SimpleHashtable.TableEntry> iter1 = sampleHashtable.iterator();
		while (iter1.hasNext()) { 
			SimpleHashtable.TableEntry pair =iter1.next(); 
			if (pair.getKey().equals("lalala")) { 
				iter1.remove();
				iter1.remove(); 
			} 
		}
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void modificationMadeDuringIteration() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(20, "la", "shh");
		Iterator<SimpleHashtable.TableEntry> iter2 = sampleHashtable.iterator();
		while (iter2.hasNext()) {
			SimpleHashtable.TableEntry pair = iter2.next();
			if (pair.getKey().equals("lalala")) {
				sampleHashtable.remove("lalala");
			}
		}
		
	}
	
	/*@Test(expected=NoSuchElementException.class)
	public void endOfIterationBreached() {
		SimpleHashtable sampleHashtable = fillSimpleHashtable(20, "la", "shh");
		
		Iterator<SimpleHashtable.TableEntry> iter3 = sampleHashtable.iterator();
		while(true) {
			iter3.next();
		}
		
	}*/
	
	
	
	
	
	
	
	
	
	
	private static SimpleHashtable fillSimpleHashtable(int n, String keyPattern, String valuePattern) {
		SimpleHashtable sampleHashtable = new SimpleHashtable();
		
		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			keyBuilder.append(keyPattern);
			valueBuilder.append(valuePattern);
			sampleHashtable.put(keyBuilder.toString(), valueBuilder.toString());
		}
		
		return sampleHashtable;
	}
	
	
}
