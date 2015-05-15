package hr.fer.zemris.java.gui.calc.buttons;

/**
 * SimetricFunctionButton is an instance of a {@link FunctionButton} which has
 * equivalent normal and reverse function.
 * 
 * @author Luka Skugor
 *
 */
public class SimetricFunctionButton extends FunctionButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new SimetricFunctionButton with given name and function.
	 * @param name display name
	 * @param function button's function
	 */
	public SimetricFunctionButton(String name, Function function) {
		super(name, name, function, function);
	}

}
