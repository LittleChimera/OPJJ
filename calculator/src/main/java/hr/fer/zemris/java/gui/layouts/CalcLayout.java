package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Layout of a simple calculator which is actually a matrix of {@link ROWS}x
 * {@link COLUMNS} with one element spanning across cells (1,1) to (1,5). Cells
 * are separated from each other by fixed spacing which can be given in a
 * constructor.
 * 
 * @author Luka Skugor
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Number of rows.
	 */
	private static final int ROWS = 5;
	/**
	 * Number of columns.
	 */
	private static final int COLUMNS = 7;

	/**
	 * Spacing between cells.
	 */
	private int spacing;
	/**
	 * 
	 */
	private Map<RCPosition, Component> components;

	/**
	 * Creates a new CalcLayout with spacing set to zero.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Creates a new CalcLayout with given spacing between cells.
	 * 
	 * @param spacing
	 *            spacing between cells
	 */
	public CalcLayout(int spacing) {
		this.spacing = spacing;
		components = new HashMap<RCPosition, Component>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		components.values().remove(comp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension preferredCellSize = new Dimension(0, 0);
		for (Component c : components.values()) {
			updateOneCellSize(preferredCellSize, c.getPreferredSize(), true);
		}

		return computeLayoutSizeFor(preferredCellSize, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension minimumCellSize = new Dimension(0, 0);
		for (Component c : components.values()) {
			updateOneCellSize(minimumCellSize, c.getMinimumSize(), true);
		}

		return computeLayoutSizeFor(minimumCellSize, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension maximumCellSize = components.values().stream().findFirst()
				.get().getMaximumSize();
		for (Component c : components.values()) {
			updateOneCellSize(maximumCellSize, c.getMaximumSize(), false);
		}

		return computeLayoutSizeFor(maximumCellSize, target);
	}

	/**
	 * This method is called in iteration for calculating minimum or maximum
	 * cell dimensions.
	 * 
	 * @param currentSize
	 *            most current calculated cell size
	 * @param compSize
	 *            current iteration cell dimension
	 * @param greater
	 *            indicates whether the new size should be greater than old one
	 */
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

	/**
	 * Computes a layout size for a given size of a cell.
	 * 
	 * @param cell
	 *            cell size
	 * @param container
	 *            layout container
	 * @return layout size
	 */
	private Dimension computeLayoutSizeFor(Dimension cell, Container container) {
		Dimension layout = new Dimension();
		Insets containerInsets = container.getInsets();
		layout.height += (ROWS - 1) * spacing + ROWS * cell.height
				+ containerInsets.left + containerInsets.right;
		layout.width += (COLUMNS - 1) * spacing + COLUMNS * cell.width
				+ containerInsets.top + containerInsets.bottom;

		return layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer(Container parent) {
		Dimension parentSize = parent.getSize();
		Insets parentsInsets = parent.getInsets();
		parentSize.height -= (parentsInsets.bottom + parentsInsets.top);
		parentSize.width -= (parentsInsets.left + parentsInsets.right);

		Dimension cellSize = new Dimension(parentSize);
		cellSize.height -= (ROWS - 1) * spacing;
		cellSize.width -= (COLUMNS - 1) * spacing;
		cellSize.height /= ROWS;
		cellSize.width /= COLUMNS;

		for (Entry<RCPosition, Component> entry : components.entrySet()) {
			RCPosition rcPosition = entry.getKey();
			int row = rcPosition.getRow();
			int col = rcPosition.getColumn();
			Component component = entry.getValue();

			component.setLocation(parentsInsets.left + (col - 1)
					* (spacing + cellSize.width), (row - 1)
					* (spacing + cellSize.height) + parentsInsets.top);
			if (row == 1 && col == 1) {
				// element on index 1,1 spans across 5 cells
				component.setSize(cellSize.width * 5 + 4 * spacing,
						cellSize.height);
			} else {
				component.setSize(cellSize);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	 * java.awt.Component)
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component,
	 * java.lang.Object)
	 */
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

	/**
	 * @param constraints
	 * @return
	 */
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

	/**
	 * @param s
	 * @return
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub

	}

}
