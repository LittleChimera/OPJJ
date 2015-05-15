package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

/**
 * Button which hold a value of a number component (decimal number, decimal
 * point, etc.).
 * 
 * @author Luka Skugor
 *
 */
public class NumberButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Number component as string.
	 */
	private String number;

	/**
	 * Creates a new NumberButton with given number component. Constructor does
	 * not check if given component is valid.
	 * 
	 * @param number number component
	 */
	public NumberButton(String number) {
		super(number);
		this.number = number;
	}

	/**
	 * Gets number component.
	 * @return number component
	 */
	public String getNumber() {
		return number;
	}
}
