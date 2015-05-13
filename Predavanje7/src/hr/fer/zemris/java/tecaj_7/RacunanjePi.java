package hr.fer.zemris.java.tecaj_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RacunanjePi {

	public static void main(String[] args) {

		final int NUMBER_OF_ATTEMPTS = 1000;
		final int numberOfSamples = 50_000;

		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			// double pi = izracunajslijedno(numberOfSamples);
			double pi = izracunajParalelno1(pool, numberOfSamples);
			if (i == 0) {
				System.out.println(pi);
			}
		}

		long t0 = System.currentTimeMillis();
		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			// double pi = izracunajslijedno(numberOfSamples);
			double pi = izracunajParalelno1(pool, numberOfSamples);
		}
		long t1 = System.currentTimeMillis();

		System.out.println("Trajalo je " + (t1 - t0)
				/ (double) NUMBER_OF_ATTEMPTS + " ms.");
		
		pool.shutdown();
	}

	private static double izracunajParalelno1(ExecutorService pool,
			int numberOfSamples) {
		class Posao implements Callable<Integer> {
			
			int samples;

			public Posao(int samples) {
				super();
				this.samples = samples;
			}

			@Override
			public Integer call() throws Exception {
				int inside = 0;
				Random random = new Random();
				for (int i = 0; i < samples; i++) {
					double x = 2 * random.nextDouble() - 1;
					double y = 2 * random.nextDouble() - 1;
					if (x * x + y * y <= 1) {
						inside++;
					}
				}
				return inside;
			}
			
		}
		List<Future<Integer>> rezultatiFutures = new ArrayList<>();
		rezultatiFutures.add(pool.submit(new Posao(numberOfSamples/2)));
		rezultatiFutures.add(pool.submit(new Posao(numberOfSamples - numberOfSamples/2)));
		
		int inside = 0;
		for (Future<Integer> f : rezultatiFutures) {
			while (true) {
				try {
					inside += f.get();
					break;
				} catch (InterruptedException | ExecutionException e) {				
				}				
			}
		}
		
		return 4 * inside / (double) numberOfSamples;
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
