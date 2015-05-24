package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token representing double value.
 * 
 * @author Luka Skugor
 *
 */
public class TokenConstantDouble extends Token {

	private double value;

	/**
	 * Creates a token with given value.
	 * 
	 * @param value
	 *            double
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Gets token's double value.
	 * @return token's double value
	 */
	public double getValue() {
		return value;
	}
}
