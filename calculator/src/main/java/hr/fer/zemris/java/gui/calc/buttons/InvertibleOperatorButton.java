package hr.fer.zemris.java.gui.calc.buttons;

/**
 * InvertibleOperatorButton is a {@link OperatorButton} which has two states:
 * normal and inverse. Inverse state has inverse binary operator of normal
 * state. E.g. for operation x^n inverse operation would be x^(1/n).
 * 
 * @author Luka Skugor
 *
 */
public class InvertibleOperatorButton extends OperatorButton implements
		InvertibleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Display name when button is inverted.
	 */
	private String nameInverted;
	/**
	 * Binary operator of inverted state.
	 */
	private BinaryOperator invertedOperator;

	/**
	 * Flag which indicates if button is inverted.
	 */
	boolean inverted;

	/**
	 * Creates a new InvertibleOperatorButton with given names and binary
	 * operations for both states.
	 * 
	 * @param name display name of normal state
	 * @param nameInverted display name inverted state
	 * @param normalOperator binary operation of normal state
	 * @param invertedOperator binary operation of inverted state
	 */
	public InvertibleOperatorButton(String name, String nameInverted,
			BinaryOperator normalOperator, BinaryOperator invertedOperator) {
		super(name, normalOperator);
		this.nameInverted = nameInverted;
		this.invertedOperator = invertedOperator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.gui.calc.buttons.OperatorButton#getOperator()
	 */
	@Override
	public BinaryOperator getOperator() {
		return (inverted) ? invertedOperator : operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.gui.calc.buttons.InvertibleButton#invert(boolean)
	 */
	public void invert(boolean inverted) {
		this.inverted = inverted;
		setText((inverted) ? nameInverted : name);
	}

}
