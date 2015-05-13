package fun.morefun.sudoku;

import java.util.NoSuchElementException;


public class SudokuMap {

	private SudokuNumber[][] table;
	private SudokuNumber[][] squares;
	
	public SudokuMap(String[] map) {
		// TODO Auto-generated constructor stub
		
		
	}
	
	
	
	public boolean solve() {
		SudokuNumber add = findMinimumWeightNumber();
		try {
			
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	private void fillNumber(SudokuNumber pos, Integer number) {
		int x = pos.getX();
		int y = pos.getY();
		
		//remove from rows and columns
		for (int i = 0; i < 9; i++) {
			table[x][i].removeValid(number);
			table[y][i].removeValid(number);
		}
		
		int squareX = (x/3)*3;
		int squareY = (y/3)*3;
		
		//remove from square
		for (int i = squareX; i < squareX+3; i++) {
			for (int j = squareY; j < squareY+3; j++) {
				table[i][j].removeValid(number);
			}
		}
		
		
	}
	
	private SudokuNumber findMinimumWeightNumber() {
		SudokuNumber minimumWeight = null;
		for (SudokuNumber[] sudokuRow : table) {
			for (SudokuNumber sudokuNumber : sudokuRow) {
				if (minimumWeight == null || minimumWeight.getWeight() > sudokuNumber.getWeight()) {
					minimumWeight = sudokuNumber;
				}
			}
		}
		return minimumWeight;
	}

}
