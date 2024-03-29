package hr.fer.zemris.java.fractals.threads;

import java.util.concurrent.ThreadFactory;

/**
 * Implementation of a {@link ThreadFactory} which creates daemon threads.
 * 
 * @author Luka Skugor
 *
 */
public class DaemonicThreadFactory implements ThreadFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread daemonic = new Thread(r);
		daemonic.setDaemon(true);
		return daemonic;
	}

}
