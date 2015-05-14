package hr.fer.zemris.java.gui.prim;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PrimListModel implements ListModel<Integer> {

	private List<Integer> prims;
	private Map<ListDataListener, ListDataListener> listeners;
	int lastPrim;

	public PrimListModel() {
		prims = new LinkedList<Integer>();
		listeners = new HashMap<ListDataListener, ListDataListener>();
		lastPrim = 1;
	}

	public int getSize() {
		return prims.size();
	}

	public Integer getElementAt(int index) {
		return prims.get(index);
	}

	public void addListDataListener(ListDataListener l) {
		listeners.put(l, l);
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

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
