package fun.morefun.sudoku;

import java.util.LinkedList;
import java.util.List;

public class SudokuNumber {
	private int value;
	LinkedList<Integer> validNumbers;
	private int x;
	private int y;
	
	public SudokuNumber(int x, int y) {
		this.x = x;
		this.y = y;
		value = -1;
		validNumbers = new LinkedList<Integer>();
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getWeight() {
		if (value == -1) {
			return validNumbers.size();
		}
		return -1;
	}
	
	public void removeValid(Integer number) {
		validNumbers.remove(number);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int tryFirst() {
		return validNumbers.pop();
	}
}
