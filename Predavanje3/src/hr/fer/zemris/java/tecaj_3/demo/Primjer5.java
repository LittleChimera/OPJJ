package hr.fer.zemris.java.tecaj_3.demo;

import java.util.Arrays;

public class Primjer5 {

	public static void main(String[] args) {
		
		double[] polje = new double[] {1,2,3,4,5,6,7,8,9,10};
		
		double[] rezultati = transformiraj(polje);
		
		System.out.println(Arrays.toString(rezultati));
	}
	
	
	private static double[] transformiraj(double[] polje) {
		double[] rezultati = new double[polje.length];
		
		for (int i = 0; i < polje.length; i++) {
			double value = Math.sin(polje[i]);
			rezultati[i] = value;
		}
		
		return rezultati;
	}
}
