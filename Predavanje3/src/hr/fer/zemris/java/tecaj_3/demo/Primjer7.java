package hr.fer.zemris.java.tecaj_3.demo;

import java.util.Arrays;

public class Primjer7 {

	public static void main(String[] args) {

		double[] polje = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		double[] rezultati = transformiraj(polje, new SineTrasnform());
		double[] rezultati2 = transformiraj(polje, new ITrasnform() {

			@Override
			public double transform(double x) {
				return 1 / x;
			}
		});
		double[] rezultati3 = transformiraj(polje, new SquareOffsetTransform(4));
		double[] rezultati4 = transformiraj(polje, new OffsetTrasnform(4) {
			@Override
			public double transform(double x) {
				return Math.pow(super.transform(x), 2);
			}
		});

		double[] rezultati5 = transformiraj(polje, a -> 1.0 / a); 
		double[] rezultati6 = transformiraj(polje, a -> {
			double pom = 1.0/a;
			return pom;
		}); 
		double[] rezultati7 = transformiraj(polje, a -> Funkcije.cubePlus42(a)); 
		double[] rezultati8 = transformiraj(polje, Funkcije::cubePlus42); 
		
		System.out.println(Arrays.toString(rezultati));
		System.out.println(Arrays.toString(rezultati2));
		System.out.println(Arrays.toString(rezultati3));
		System.out.println(Arrays.toString(rezultati4));

	}
	
	@FunctionalInterface
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

	static class OffsetTrasnform implements ITrasnform {
		private double offset;

		public OffsetTrasnform(double offset) {
			super();
			this.offset = offset;
		}

		@Override
		public double transform(double x) {
			return x + offset;
		}
	}

	static class SquareOffsetTransform extends OffsetTrasnform {

		public SquareOffsetTransform(double offset) {
			super(offset);
		}

		@Override
		public double transform(double x) {
			return Math.pow(super.transform(x), 2);
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
