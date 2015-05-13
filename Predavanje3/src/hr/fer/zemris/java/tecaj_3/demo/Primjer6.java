package hr.fer.zemris.java.tecaj_3.demo;

import java.util.Arrays;

public class Primjer6 {

	public static void main(String[] args) {
		
		double[] polje = new double[] {1,2,3,4,5,6,7,8,9,10};
		
		double[] rezultati = transformiraj(polje, new SineTrasnform());
		
		System.out.println(Arrays.toString(rezultati));
	}
	
	interface ITrasnform {
		double transform(double x);
	}
	
	static class SineTrasnform implements ITrasnform {
		@Override
		public double transform(double x) {
			return Math.sin(x);
		}
	}
	
	static class CosineTransform implements ITrasnform {
		@Override
		public double transform(double x) {
			return Math.cos(x);
		}
	}
	
	private static double[] transformiraj(double[] polje, ITrasnform trasnformer) {
		double[] rezultati = new double[polje.length];
		
		for (int i = 0; i < polje.length; i++) {
			double value = trasnformer.transform(polje[i]);
			rezultati[i] = value;
		}
		
		return rezultati;
	}
}
