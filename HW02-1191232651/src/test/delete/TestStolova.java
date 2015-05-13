package test.delete;

import hr.fer.zemris.java.custom.collections.ObjectStack; 

public class TestStolova {
	private static ObjectStack arr;
	
	public static void main(String[] args) {
		arr = new ObjectStack();
		Stol praviStol = new Stol();
		Stol sitniStol = new MaliStol();
		Stol velkiStol = new VelikStol();
		
		arr.push(praviStol); arr.push(sitniStol); arr.push(velkiStol);
		for(int i=0; i<3; i++) {
			ispisi((Stol) arr.pop());
		}
		System.out.println("{$=");
		
	}
	
	private static void ispisi(Stol nekiStol) {
		nekiStol.print();
	}

}
