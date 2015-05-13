package hr.fer.zemris.java.gui.layouts;

public class RCPosition {

	private int row;
	private int column;

	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

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

	@Override
	public int hashCode() {
		return Integer.valueOf(row).hashCode()
				+ Integer.valueOf(column).hashCode();
	}
	
	@Override
	public String toString() {
		return row + "," + column;
	}

}
