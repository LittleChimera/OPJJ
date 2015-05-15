package hr.fer.zemris.java.gui.layouts;

/**
 * RCPosition is a pair of integers (row, column) which indicate a cell in a
 * matrix.
 * 
 * @author Luka Skugor
 *
 */
public class RCPosition {

	/**
	 * Row index of the position.
	 */
	private int row;
	/**
	 * Column index of the position.
	 */
	private int column;

	/**
	 * Creates a new RCPosition with given row and column indexes.
	 * 
	 * @param row
	 *            row index of the position
	 * @param column
	 *            column index of the position
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets column index.
	 * 
	 * @return column index
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Gets row index.
	 * 
	 * @return row index
	 */
	public int getRow() {
		return row;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RCPosition)) {
			return false;
		}
		RCPosition rcpObj = (RCPosition) obj;
		if (rcpObj.row == this.row && rcpObj.column == this.column) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Integer.valueOf(row).hashCode()
				+ Integer.valueOf(column).hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return row + "," + column;
	}

}
