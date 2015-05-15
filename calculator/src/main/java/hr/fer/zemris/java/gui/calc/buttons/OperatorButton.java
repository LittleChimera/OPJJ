package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

public class OperatorButton extends JButton {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected BinaryOperator operator;
	protected String name;

	public OperatorButton(String name, BinaryOperator operator) {
		super(name);
		this.name = name;
		this.operator = operator;
	}

	public BinaryOperator getOperator() {
		return operator;
	}
	
	public interface BinaryOperator {
		public Double operate(double d1, double d2);
	}
	
}
