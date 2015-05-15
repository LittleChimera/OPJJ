package hr.fer.zemris.java.gui.calc.buttons;

/**
 * InvertibleButton supports a functionality for two states of a button: normal
 * and inverted.
 * 
 * @author Luka Skugor
 *
 */
public interface InvertibleButton {

	/**
	 * Sets button state.
	 * 
	 * @param invert
	 *            if invert is true button will be in inverted state, otherwise
	 *            button will be in normal state
	 */
	public void invert(boolean invert);

}
