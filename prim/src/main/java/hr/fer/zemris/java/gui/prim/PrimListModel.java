package hr.fer.zemris.java.gui.prim;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * PrimListModel is a {@link ListModel} which stores and generates prime
 * numbers.
 * 
 * @author Luka Skugor
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/**
	 * List of prime numbers which are stored in the list mode.
	 */
	private List<Integer> prims;
	/**
	 * Listeners which are observing this model.
	 */
	private Map<ListDataListener, ListDataListener> listeners;
	/**
	 * Last generated prime number.
	 */
	int lastPrim;

	/**
	 * Creates a new PrimListModel.
	 */
	public PrimListModel() {
		prims = new LinkedList<Integer>();
		listeners = new HashMap<ListDataListener, ListDataListener>();
		lastPrim = 1;
		prims.add(lastPrim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return prims.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Integer getElementAt(int index) {
		return prims.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener
	 * )
	 */
	public void addListDataListener(ListDataListener l) {
		listeners.put(l, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.
	 * ListDataListener)
	 */
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	/**
	 * Generates next prime number and stores it in the model. Consequently, all
	 * listeners are notified.
	 * 
	 * @return generated prime number
	 */
	public int next() {
		boolean searching = true;
		Search: while (searching) {
			lastPrim += (lastPrim > 2) ? 2 : 1;
			searching = false;
			for (int divisor = 3; divisor <= Math.sqrt(lastPrim); divisor += 2) {
				if (lastPrim % divisor == 0) {
					searching = true;
					continue Search;
				}
			}
		}
		prims.add(lastPrim);
		for (ListDataListener listener : listeners.values()) {
			listener.intervalAdded(new ListDataEvent(this,
					ListDataEvent.INTERVAL_ADDED, prims.size() - 1, prims
							.size()));
		}
		return lastPrim;
	}

}
