package hr.fer.zemris.java.tecaj_7;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
	
	private static final Runnable littleRedPill = () -> {};
	
	private LinkedBlockingQueue<Runnable> jobs = new LinkedBlockingQueue<Runnable>();
	private Thread[] workers;
	
	public ThreadPool() {
		this(Runtime.getRuntime().availableProcessors());
		System.out.println(workers.length);
	}
	
	public ThreadPool(int numberOFWorkers) {
		workers = new Thread[numberOFWorkers];
		for (int i = 0; i < numberOFWorkers; i++) {
			workers[i] = new Thread(() -> {
				while (true) {
					Runnable job = nextJob();
					if (job == littleRedPill) {
						return;
					}
					job.run();
				}
			}); 
			workers[i].start();
		}
	}
	
	public void addJob(Runnable job) {
		while (true) {
			try {
				jobs.put(job);
				return;
			} catch (InterruptedException e) {
			}
		}
	}
	
	private Runnable nextJob() {
		while (true) {
			try {
				return jobs.take();
			} catch (InterruptedException e) {
			}
		}
	}

	public void shutdown() {
		for (int i = 0; i < workers.length; i++) {
			addJob(littleRedPill);
		}
	}

}
