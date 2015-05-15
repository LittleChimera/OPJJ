package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

/**
 * OperatorButton holds a reference to an {@link BinaryOperator}.
 * 
 * @author Luka Skugor
 *
 */
public class OperatorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Binary operator which button hold a reference to.
	 */
	protected BinaryOperator operator;
	/**
	 * Display name of the button.
	 */
	protected String name;

	/**
	 * Creates a new button with given name and binary operation.
	 * @param name display name
	 * @param operator binary operation which button holds
	 */
	public OperatorButton(String name, BinaryOperator operator) {
		super(name);
		this.name = name;
		this.operator = operator;
	}

	/**
	 * Gets held binary operation.
	 * @return binary operation
	 */
	public BinaryOperator getOperator() {
		return operator;
	}

	/**
	 * BinaryOperator can perform a binary operations on two number operands.
	 * 
	 * @author Luka Skugor
	 *
	 */
	public interface BinaryOperator {
		/**
		 * @param d1 first operand
		 * @param d2 second operand
		 * @return result of the opeartion
		 */
		public Double operate(double d1, double d2);
	}

}
