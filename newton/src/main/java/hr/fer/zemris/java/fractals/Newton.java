package hr.fer.zemris.java.fractals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.threads.DaemonicThreadFactory;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * @author Luka Skugor
 *
 */
public class Newton {

	/**
	 * Rooted polynomial of Newton's fractal.
	 */
	private static ComplexRootedPolynomial rootedPolynomial;
	/**
	 * Normal polynomial with factors of Newton's fractal.
	 */
	private static ComplexPolynomial polynomial;
	/**
	 * Derivative of the polynomial forming Newton's fractal.
	 */
	private static ComplexPolynomial polynomialDerivative;

	/**
	 * Displays Newton's fractal which is calculated parallel.
	 */
	public static void showSequential() {
		FractalViewer.show(getSequentialFractalproducer());
	}

	
	/**
	 * Parallel implementation which calculates Newton's fractal. It enables
	 * calculation of results for the given range of y-coordinates (the rest of
	 * the data array is preserved).
	 * 
	 * @param reMin
	 *            minimum value on real axis
	 * @param reMax
	 *            maximum value on real axis
	 * @param imMin
	 *            minimum value on imaginary axis
	 * @param imMax
	 *            maximum value on imaginary axis
	 * @param width
	 *            width of window on which fractal is displayed
	 * @param height
	 *            height of window on which fractal is displayed
	 * @param m
	 *            divergence depth level
	 * @param ymin
	 *            y coordinate from which data is calculated (inclusive)
	 * @param ymax
	 *            y coordinate to which data is calculated (inclusive)
	 * @param data
	 *            array where results are stored
	 */
	public static void calculate(double reMin, double reMax, double imMin,
			double imMax, int width, int height, int m, int ymin, int ymax,
			short[] data) {

		int offset = ymin * width;
		for (int y = ymin; y <= ymax; y++) {
			for (int x = 0; x < width; x++) {

				double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
				double cim = ((height - 1) - y) / (height - 1.0)
						* (imMax - imMin) + imMin;
				Complex c = new Complex(cre, cim);
				Complex zn = c;
				double treshold = 0;
				int iters = 0;

				do {
					Complex zn1 = zn.sub(polynomial.apply(zn).divide(
							polynomialDerivative.apply(zn)));
					treshold = zn.sub(zn1).module();
					zn = zn1;
					iters++;
				} while (iters < m && treshold > 1e-3);

				int index = rootedPolynomial.indexOfClosestRootFor(zn, 2e-3);
				if (index == -1) {
					data[offset++] = 0;
				} else {
					data[offset++] = (short) index;
				}
			}
		}
	}

	/**
	 * Returns a parallel generator of Newton's fractal.
	 * @return parallel generator of Newton's fractal
	 */
	private static IFractalProducer getSequentialFractalproducer() {
		return new IFractalProducer() {

			/**
			 * Number of jobs on which this task will be split.
			 */
			int jobCount = 8 * Runtime.getRuntime().availableProcessors();
			/**
			 * Executor service which takes DataCalculators.
			 */
			ExecutorService executor = Executors.newFixedThreadPool(Runtime
					.getRuntime().availableProcessors(),
					new DaemonicThreadFactory());
			
			/* (non-Javadoc)
			 * @see hr.fer.zemris.java.fractals.viewer.IFractalProducer#produce(double, double, double, double, int, int, long, hr.fer.zemris.java.fractals.viewer.IFractalResultObserver)
			 */
			@Override
			public void produce(double reMin, double reMax, double imMin,
					double imMax, int width, int height, long requestNo,
					IFractalResultObserver observer) {

				short[] data = new short[width * height];
				int m = 16 * 16 * 16;

				long t0 = System.currentTimeMillis();

				/**
				 * Calculates data of Newton's fractal for given range of y-coordinates.
				 * @author Luka Skugor
				 *
				 */
				class DataCalculator implements Runnable {

					/**
					 * y coordinate from which data is calculated (inclusive)
					 */
					private int ymin;
					/**
					 * y coordinate to which data is calculated (inclusive)
					 */
					private int ymax;

					/**
					 * Creates a new DataCalculator with given range of y-coordinates.
					 * @param ymin y coordinate from which data is calculated (inclusive)
					 * @param ymax y coordinate to which data is calculated (inclusive)
					 */
					public DataCalculator(int ymin, int ymax) {
						super();
						this.ymin = ymin;
						this.ymax = ymax;
					}

					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						calculate(reMin, reMax, imMin, imMax, width, height, m,
								ymin, ymax, data);
					}

				}
				System.out.println("Započinjem izračune...");

				int eachJobHeight = height / jobCount;
				Future<?>[] jobs = new Future[jobCount];
				for (int i = 0; i < jobCount - 1; i++) {
					jobs[i] = executor.submit(new DataCalculator(i
							* eachJobHeight, (i + 1) * eachJobHeight - 1));
				}
				jobs[jobCount - 1] = executor.submit(new DataCalculator(
						(jobCount - 1) * eachJobHeight, height - 1));

				for (Future<?> future : jobs) {
					while (true) {
						try {
							future.get();
							break;
						} catch (InterruptedException | ExecutionException e) {
						}
					}
				}


				System.out.println("Izračuni gotovi...");
				observer.acceptResult(data, (short) (polynomial.order() + 1),
						requestNo);
				System.out.println("Dojava gotova...");
				long t1 = System.currentTimeMillis();
				System.out.println("Trajalo je: " + (t1 - t0) + "ms.");
			}

		};
	}

	/**
	 * Called on program start.
	 * @param args command line arguments
	 * @throws IOException if error occurs on standard system input.
	 */
	public static void main(String[] args) throws IOException {
		System.out
				.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out
				.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		List<Complex> roots = new LinkedList<Complex>();

		int rootNumber = 1;
		Input: while (true) {
			Complex readRoot = null;
			while (readRoot == null) {
				System.out.format("Root %d> ", rootNumber);

				line = reader.readLine();
				if (line.equalsIgnoreCase("done")) {
					break Input;
				}
				try {
					readRoot = Complex.parseComplex(line);
					rootNumber++;
				} catch (NumberFormatException e) {
					// loop
					System.out.println(e.getLocalizedMessage());
				}
			}
			roots.add(readRoot);
		}
		rootedPolynomial = new ComplexRootedPolynomial(
				roots.toArray(new Complex[rootNumber - 1]));
		polynomial = rootedPolynomial.toComplexPolynom();
		polynomialDerivative = polynomial.derive();
		showSequential();
	}

}
