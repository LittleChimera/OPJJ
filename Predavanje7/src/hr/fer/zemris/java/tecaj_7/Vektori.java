package hr.fer.zemris.java.tecaj_7;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Vektori {

	public static void main(String[] args) {

		final int vSize = 500_000;
		double[] a = new double[vSize];
		double[] b = new double[vSize];
		double[] rez = new double[vSize];

		Random random = new Random();
		for (int i = 0; i < vSize; i++) {
			a[i] = random.nextDouble();
			b[i] = random.nextDouble();
		}
		ThreadPool pool = new ThreadPool();
		
		final int NUMBER_OF_ATTEMPTS = 1000;

		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			parallelMul2(pool, a, b, rez);
			//parallelMul(a, b, rez);
			// sequentialMul(a, b, rez);
		}

		long t0 = System.currentTimeMillis();
		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			sequentialMul(a, b, rez);
		}
		long t1 = System.currentTimeMillis();

		System.out.println("Trajalo je " + (t1 - t0)
				/ (double) NUMBER_OF_ATTEMPTS + " ms.");
		
		pool.shutdown();

	}

	private static void parallelMul2(ThreadPool pool, double[] a, double[] b, double[] rez) {
		Semaphore sem = new Semaphore(0);
		class Posao implements Runnable {

			int fromIndex;
			int toIndex;

			public Posao(int fromIndex, int toIndex) {
				super();
				this.fromIndex = fromIndex;
				this.toIndex = toIndex;
			}

			@Override
			public void run() {
				for (int i = fromIndex; i < toIndex; i++) {
					rez[i] = a[i] * b[i];
				}
				sem.release();
			}

		}
		final int brojDretvi = 2;
		
		Posao[] poslovi = new Posao[brojDretvi];
		for (int i = 0; i < brojDretvi; i++) {
			poslovi[i]= new Posao(i*a.length/brojDretvi, (i+1)*a.length/brojDretvi); 
			pool.addJob(poslovi[i]);
		}
		
		sem.acquireUninterruptibly(poslovi.length);
	}

	private static void parallelMul(double[] a, double[] b, double[] rez) {
		class Posao implements Runnable {

			int fromIndex;
			int toIndex;

			public Posao(int fromIndex, int toIndex) {
				super();
				this.fromIndex = fromIndex;
				this.toIndex = toIndex;
			}

			@Override
			public void run() {
				for (int i = fromIndex; i < toIndex; i++) {
					rez[i] = a[i] * b[i];
				}
			}

		}
		final int brojDretvi = 2;
		
		Posao[] poslovi = new Posao[brojDretvi];
		for (int i = 0; i < brojDretvi; i++) {
			poslovi[i]= new Posao(i*a.length/brojDretvi, (i+1)*a.length/brojDretvi); 
		}
		Thread[] dretve = new Thread[brojDretvi];
		for (int i = 0; i < brojDretvi; i++) {
			dretve[i] = new Thread(poslovi[i]);
			dretve[i].start();
		}
		for (int i = 0; i < dretve.length; i++) {
			while (true) {
				try {
					dretve[i].join();
					break;
				} catch (InterruptedException e) {			
				}
			}
		}
	}

	private static void sequentialMul(double[] a, double[] b, double[] rez) {
		for (int i = 0; i < a.length; i++) {
			rez[i] = a[i] * b[i];
		}

	}
	
	private static class Spremnik<T> {
		private LinkedList<T> red = new LinkedList<>(); 
		
		public synchronized void umetni(T t) {
			red.add(t);
			notifyAll();
		}
		
		public synchronized T izvadi() {
			while (red.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			T elem = red.removeFirst();
			return elem;
		}
	}

}
