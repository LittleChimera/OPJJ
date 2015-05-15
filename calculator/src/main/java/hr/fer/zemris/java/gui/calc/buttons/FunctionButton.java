package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

/**
 * Function button has a {@link Function} which can be applied on a number.
 * Button has two states based on which function it's performing: normal or
 * inverse. When button is inverted it has different name.
 * 
 * @author Luka Skugor
 *
 */
public class FunctionButton extends JButton implements InvertibleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Button display name.
	 */
	private String name;
	/**
	 * Button display name if button is inverted.
	 */
	private String nameInverted;
	/**
	 * Flag which indicates if button is inverted.
	 */
	private boolean isInverted;
	/**
	 * Normal function.
	 */
	private Function normal;
	/**
	 * Inverted function.
	 */
	private Function inverted;

	/**
	 * Creates a new FunctionButton with given names and function for both
	 * normal and inverted state.
	 * 
	 * @param name normal state display name
	 * @param nameInverted inverted state display name
	 * @param normal normal function
	 * @param inverted inverted function
	 */
	public FunctionButton(String name, String nameInverted, Function normal,
			Function inverted) {
		super(name);
		this.name = name;
		this.nameInverted = nameInverted;
		this.normal = normal;
		this.inverted = inverted;
	}

	/**
	 * Applies a function based on the current state on a number.
	 * @param number number on which function will be applied
	 * @return result of the function
	 */
	public Double apply(double number) {
		return ((isInverted) ? inverted : normal).apply(number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.gui.calc.buttons.InvertibleButton#invert(boolean)
	 */
	public void invert(boolean inverted) {
		this.isInverted = inverted;
		setText((inverted) ? nameInverted : name);
	}

	/**
	 * Function has only one functionality: applying a function on a number.
	 * @author Luka Skugor
	 *
	 */
	public static interface Function {
		
		/**
		 * Applies the function on a number.
		 * @param number number on which function will be applied
		 * @return result of the function
		 */
		Double apply(double number);
	}

}
