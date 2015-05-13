package hr.fer.zemris.java.tecaj_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

public class RacunanjePi2 {

	public static void main(String[] args) {

		final int NUMBER_OF_ATTEMPTS = 1000;
		final int numberOfSamples = 50_000;

		ForkJoinPool pool = new ForkJoinPool();
		
		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			// double pi = izracunajslijedno(numberOfSamples);
			double pi = izracunajParalelno2(pool, numberOfSamples);
			if (i == 0) {
				System.out.println(pi);
			}
		}

		long t0 = System.currentTimeMillis();
		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			// double pi = izracunajslijedno(numberOfSamples);
			double pi = izracunajParalelno2(pool, numberOfSamples);
		}
		long t1 = System.currentTimeMillis();

		System.out.println("Trajalo je " + (t1 - t0)
				/ (double) NUMBER_OF_ATTEMPTS + " ms.");
		
		pool.shutdown();
	}

	private static double izracunajParalelno2(ForkJoinPool pool,
			int numberOfSamples) {
		class Posao extends RecursiveAction {
			
			int samples;
			int inside;

			public Posao(int samples) {
				super();
				this.samples = samples;
			}
			
			@Override
			protected void compute() {
				if (samples > 1_000) {
					Posao p1 = new Posao(samples/2);
					Posao p2 = new Posao(samples - samples/2);
					invokeAll(p1, p2);
					inside += p1.inside;
					inside += p2.inside;
				} else {
					computeDirect();
				}
			}
			
			private void computeDirect() {
				Random random = new Random();
				for (int i = 0; i < samples; i++) {
					double x = 2 * random.nextDouble() - 1;
					double y = 2 * random.nextDouble() - 1;
					if (x * x + y * y <= 1) {
						inside++;
					}
				}
			}
			
		}
		Posao posao = new Posao(numberOfSamples);
		pool.invoke(posao);
		
		return 4 * posao.inside / (double) numberOfSamples;
	}

	private static double izracunajslijedno(int numberOfSamples) {
		int inside = 0;
		Random random = new Random();
		for (int i = 0; i < numberOfSamples; i++) {
			double x = 2 * random.nextDouble() - 1;
			double y = 2 * random.nextDouble() - 1;
			if (x * x + y * y <= 1) {
				inside++;
			}
		}

		return 4 * inside / (double) numberOfSamples;
	}

}
