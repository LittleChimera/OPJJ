package hr.fer.zemris.java.tecaj.hw5.db;

import hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw5.db.fieldGetters.IFieldValueGetter;

/**
 * ConditionalExpression represents a conditional expression in a query which
 * has the following format:
 * 
 * fieldName(comparison operator)"literal"
 * 
 * Note: Spaces between these three components are allowed.
 * 
 * Field name is represented with IFieldValueGetter which gets the value of the
 * field name. Comparison operator is represented with IComparisonOperator which
 * is used to compare a field value with the literal.
 * 
 * @author Luka Škugor
 *
 */
public class ConditionalExpression {

	/**
	 * Getter of the field value.
	 */
	private IFieldValueGetter getter;
	/**
	 * Literal with which get value will be compared.
	 */
	private String literal;
	/**
	 * Operator which is used for comparing requested values.
	 */
	private IComparisonOperator operator;

	/**
	 * Creates a new conditional expression from given value getter, comparison
	 * operator and a literal.
	 * 
	 * @param getter
	 *            field getter
	 * @param literal
	 *            literal with which values are compared
	 * @param operator
	 *            comparison operator with which value and literal are compared
	 */
	public ConditionalExpression(IFieldValueGetter getter, String literal,
			IComparisonOperator operator) {
		this.getter = getter;
		this.literal = literal;
		this.operator = operator;
	}

	/**
	 * Gets field value getter.
	 * 
	 * @return field value getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return getter;
	}

	/**
	 * Gets the literal.
	 * 
	 * @return literal
	 */
	public String getStringLiteral() {
		return literal;
	}

	/**
	 * Gets comparison operator.
	 * 
	 * @return comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return operator;
	}
}
