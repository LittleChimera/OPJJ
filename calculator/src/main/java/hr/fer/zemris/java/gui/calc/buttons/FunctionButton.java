package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

public class FunctionButton extends JButton implements InvertibleButton {
	
	private String name;
	private String nameInverted;
	private boolean isInverted;
	private Function normal;
	private Function inverted;
	
	public FunctionButton(String name, String nameInverted, Function normal, Function inverted) {
		super(name);
		this.name = name;
		this.nameInverted = nameInverted;
		this.normal = normal;
		this.inverted = inverted;
	}
	
	public Double apply(double number) {
		return ((isInverted)?inverted:normal).apply(number);
	}

	public void invert(boolean inverted) {
		this.isInverted = inverted;
		setText((inverted)?nameInverted:name);
	}
	
	public static interface Function {
		Double apply(double number);
	}
	
	
	
}
