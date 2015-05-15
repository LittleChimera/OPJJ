package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

public class NumberButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String number;
	
	public NumberButton(String number) {
		super(number);
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}
}
