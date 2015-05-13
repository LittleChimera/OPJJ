package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalcLayout implements LayoutManager2 {

	private static final int ROWS = 5;
	private static final int COLUMNS = 7;

	private int spacing;
	private Map<RCPosition, Component> components;

	public CalcLayout() {
		this(0);
	}

	public CalcLayout(int spacing) {
		this.spacing = spacing;
		components = new HashMap<RCPosition, Component>();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.values().remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension preferredCellSize = new Dimension(0, 0);
		for (Component c : components.values()) {
			updateOneCellSize(preferredCellSize, c.getPreferredSize(), true);
		}

		return computeLayoutSizeFor(preferredCellSize, parent);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension minimumCellSize = new Dimension(0, 0);
		for (Component c : components.values()) {
			updateOneCellSize(minimumCellSize, c.getMinimumSize(), true);
		}

		return computeLayoutSizeFor(minimumCellSize, parent);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension maximumCellSize = components.values().stream().findFirst()
				.get().getMaximumSize();
		for (Component c : components.values()) {
			updateOneCellSize(maximumCellSize, c.getMaximumSize(), false);
		}

		return computeLayoutSizeFor(maximumCellSize, target);
	}

	private void updateOneCellSize(Dimension currentSize, Dimension compSize,
			boolean greater) {
		// if compenent's size doesn't matter
		if (compSize == null) {
			return;
		}
		
		if (compSize.width > currentSize.width == greater) {
			currentSize.width = compSize.width;
		}
		if (compSize.height > currentSize.height == greater) {
			currentSize.height = compSize.height;
		}
	}

	private Dimension computeLayoutSizeFor(Dimension cell, Container container) {
		Dimension layout = new Dimension();
		Insets containerInsets = container.getInsets();
		layout.height += (ROWS - 1) * spacing + ROWS * cell.height
				+ containerInsets.left + containerInsets.right;
		layout.width += (COLUMNS - 1) * spacing + COLUMNS * cell.width
				+ containerInsets.top + containerInsets.bottom;

		return layout;
	}

	@Override
	public void layoutContainer(Container parent) {
		Dimension parentSize = parent.getSize();
		Insets parentsInsets = parent.getInsets();
		parentSize.height -= (parentsInsets.bottom + parentsInsets.top);
		parentSize.width -= (parentsInsets.left + parentsInsets.right);
		
		Dimension cellSize = new Dimension(parentSize);
		cellSize.height -= (ROWS - 1)*spacing;
		cellSize.width -= (COLUMNS - 1)*spacing;
		cellSize.height /= ROWS;
		cellSize.width /= COLUMNS;
		
		for (Entry<RCPosition, Component> entry : components.entrySet()) {
			RCPosition rcPosition = entry.getKey();
			int row = rcPosition.getRow();
			int col = rcPosition.getColumn();
			Component component = entry.getValue();
			
			component.setLocation((col-1)*(spacing + cellSize.width), (row-1)*(spacing + cellSize.height));
			if (row == 1 && col == 1) {
				//element on index 1,1 spans across 5 cells
				component.setSize(cellSize.width*5 + 4*spacing, cellSize.height);
			} else {
				component.setSize(cellSize);
			}				
		}
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, name);
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition rcConstraints = processConstraints(constraints);

		if (components.containsKey(rcConstraints)) {
			throw new IllegalArgumentException(
					"Component with given constraints \"" + rcConstraints
							+ "\" already exists.");
		}

		components.put(rcConstraints, comp);
	}

	private RCPosition processConstraints(Object constraints) {
		RCPosition rcConstraints = null;

		if (constraints instanceof String) {
			rcConstraints = parseStringConstraints((String) constraints);
		} else {
			rcConstraints = (RCPosition) constraints;
		}
		int row = rcConstraints.getRow();
		int column = rcConstraints.getColumn();

		// checks if constraints are within matrix 5x7
		if ((row < 1 || row > ROWS) || (column < 1 && column > COLUMNS)) {
			throw new IllegalArgumentException();
		}

		// checks if any of non-existing elements of matrix are accessed
		if (row == 1 && (column >= 2 && column <= 5)) {
			throw new IllegalArgumentException();
		}

		return rcConstraints;
	}

	private RCPosition parseStringConstraints(String s) {
		String[] rowAndCol = ((String) s).split(",");
		if (rowAndCol.length > 2) {
			throw new IllegalArgumentException();
		}

		int row, column;
		try {
			row = Integer.parseInt(rowAndCol[0]);
			column = Integer.parseInt(rowAndCol[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		return new RCPosition(row, column);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub

	}

}
