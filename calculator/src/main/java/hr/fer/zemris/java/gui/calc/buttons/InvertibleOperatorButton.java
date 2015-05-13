package hr.fer.zemris.java.gui.calc.buttons;


public class InvertibleOperatorButton extends OperatorButton implements InvertibleButton {

	private String nameInverted;
	private BinaryOperator invertedOperator;
	
	boolean inverted;

	public InvertibleOperatorButton(String name, String nameInverted,
			BinaryOperator normalOperator, BinaryOperator invertedOperator) {
		super(name, normalOperator);
		this.nameInverted = nameInverted;
		this.invertedOperator = invertedOperator;
	}
	
	@Override
	public BinaryOperator getOperator() {
		return (inverted)?invertedOperator:operator;
	}
	
	public void invert(boolean inverted) {
		this.inverted = inverted;
		setText((inverted)?nameInverted:name);
	}
	
}
