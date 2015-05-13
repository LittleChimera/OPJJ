package hr.fer.zemris.java.tecaj.hw5.db;

import hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.*;
import hr.fer.zemris.java.tecaj.hw5.db.fieldGetters.*;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of IFilter which filters StudentRecord for multiple
 * conditional expressions. Query conditions can be only separated with the key word 'and'
 * which is case insensitive. 
 * 
 * @author Luka Škugor
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * JMBAG of the conditional expression which can evaluate true only for single JMBAG.
	 */
	private String jmbag;
	/**
	 * Pattern of a conditional expression.
	 */
	private static final Pattern CONDITIONAL_EXPRESSION_PATTERN = Pattern
			.compile("^([^<>=!]+)\\s*(=|!=|<|<=|>|>=)\\s*\"([^\"]+)\"$");
	/**
	 * List of query's conditional expressions.
	 */
	private List<ConditionalExpression> conditions;

	/**
	 * Creates a new query for a given query string.
	 * 
	 * @param query query string
	 * @throws IllegalArgumentException if query string is invalid
	 */
	public QueryFilter(String query) {
		conditions = new LinkedList<ConditionalExpression>();
		if (query.trim().toUpperCase().endsWith("AND")) {
			throw new IllegalArgumentException("Invalid query!");
		}
		String[] stringQueries = query.split("(?i)\\s*and\\s*");
		for (String stringQuery : stringQueries) {
			Matcher queryMatcher = CONDITIONAL_EXPRESSION_PATTERN.matcher(stringQuery.trim());
			if (!queryMatcher.matches()) {
				throw new IllegalArgumentException("Invalid query pattern: "
						+ stringQuery);
			}
			String literal = queryMatcher.group(3);
			IComparisonOperator comparisonOperator = parseComparisonOperator(queryMatcher
					.group(2));
			IFieldValueGetter fieldValueGetter = parseFieldValueGetter(queryMatcher
					.group(1));
			if (comparisonOperator instanceof WildCardEqualsCondition
					&& fieldValueGetter instanceof JMBAGFieldGetter
					&& literal.indexOf(WildCardEqualsCondition.WILD_CARD) == -1) {
				if (jmbag == null || jmbag.equals(literal)) {
					jmbag = literal;
				} else {
					throw new NoSuchElementException(
							"Query can't match for two different JMBAGs: "
									+ jmbag + " and " + literal + ".");
				}
			} else {
				ConditionalExpression expr = new ConditionalExpression(
						fieldValueGetter, literal, comparisonOperator);
				conditions.add(expr);
			}
		}
	}

	/**
	 * Gets Optional of JMBAG which will be present if there is a conditional
	 * expression in the query which evaluates true for single JMBAG.
	 * 
	 * @return optional of jMBAG
	 */
	public Optional<String> getJMBAG() {
		return Optional.ofNullable(jmbag);
	}

	/**
	 * Gets corresponding field value getter for given field name. Field name is
	 * case insensitive.
	 * 
	 * @param getterName
	 *            field name
	 * @return corresponding field value getter
	 */
	private IFieldValueGetter parseFieldValueGetter(String getterName) {
		IFieldValueGetter fieldValueGetter;
		switch (getterName.toUpperCase().trim()) {
		case "FIRSTNAME":
			fieldValueGetter = new FirstNameFieldGetter();
			break;
		case "LASTNAME":
			fieldValueGetter = new LastNameFieldGetter();
			break;
		case "JMBAG":
			fieldValueGetter = new JMBAGFieldGetter();
			break;
		default:
			throw new IllegalArgumentException("No such field");
		}
		return fieldValueGetter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.tecaj.hw5.db.IFilter#accepts(hr.fer.zemris.java.tecaj
	 * .hw5.db.StudentRecord)
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : conditions) {
			boolean recordSatisfies = expr.getComparisonOperator().satisfied(
					expr.getFieldGetter().get(record), expr.getStringLiteral());
			if (!recordSatisfies) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets corresponding comparison operator for given string.
	 * 
	 * @param operator
	 *            string representation of the operator
	 * @return corresponding comparison operator
	 */
	private IComparisonOperator parseComparisonOperator(String operator) {
		IComparisonOperator comparisonOperator = null;

		switch (operator) {
		case "=":
			comparisonOperator = new WildCardEqualsCondition();
			break;
		case "<=":
			comparisonOperator = new LessOrEqualsComparator();
			break;
		case ">=":
			comparisonOperator = new GreaterOrEqualsComparator();
			break;
		case ">":
			comparisonOperator = new GreaterThanComparator();
			break;
		case "<":
			comparisonOperator = new LessThanComparator();
			break;
		case "!=":
			comparisonOperator = new DistinctOperator();
			break;
		}
		return comparisonOperator;
	}

	/**
	 * Get a copy of conditional expressions of the query.
	 * 
	 * @return list of conditional expressions
	 */
	public List<ConditionalExpression> copyOfConditionalExpressions() {
		List<ConditionalExpression> copy = new LinkedList<>(conditions);
		return copy;
	}

}
