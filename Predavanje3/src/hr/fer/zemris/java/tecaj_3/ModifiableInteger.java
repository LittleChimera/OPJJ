package hr.fer.zemris.java.tecaj_3;

public class ModifiableInteger {
	
	private static int count;
	private int value;

	public ModifiableInteger(int value) {
		count++;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public static int getCount() {
		return count;
	}
	
	public static void main(String[] args) {
		ModifiableInteger i1 = new ModifiableInteger(7);
		ModifiableInteger i2 = new ModifiableInteger(7);
		ModifiableInteger i3 = new ModifiableInteger(42);
	
		System.out.println(ModifiableInteger.getCount());
	}
}
