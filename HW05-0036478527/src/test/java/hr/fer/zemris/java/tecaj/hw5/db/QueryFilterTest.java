package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.*;
import hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.DistinctOperator;
import hr.fer.zemris.java.tecaj.hw5.db.comparisonOperators.LessThanComparator;
import hr.fer.zemris.java.tecaj.hw5.db.fieldGetters.IFieldValueGetter;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

public class QueryFilterTest {
	
	@Test
	public void indexJMBAG() {
		QueryFilter qf = new QueryFilter("firstName>=\"A\" and firstName<=\"C\" and lastName=\"B*ć\"");
		assertFalse(qf.getJMBAG().isPresent());
	}
	
	@Test
	public void noJMBAGQuery() {
		QueryFilter qf = new QueryFilter("jmbag=\"0000000003\"");
		assertTrue(qf.getJMBAG().isPresent());
	}
	
	@Test
	public void JMBAGWithWildCard() {
		QueryFilter qf = new QueryFilter("jmbag=\"00000*003\"");
		assertFalse(qf.getJMBAG().isPresent());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void twoDifferentJMBAGs() {
		new QueryFilter("jmbag=\"0000000003\" and jmbag=\"0000000004\"");
	}
	
	@Test
	public void twoSameJMBAGs() {
		QueryFilter qf = new QueryFilter("jmbag=\"0000000003\" and jmbag=\"0000000003\"");
		assertTrue(qf.getJMBAG().isPresent());
	}
	
	
	@Test
	public void allFieldNames() {
		QueryFilter qf = new QueryFilter("firstName>=\"A\" and firstName<=\"C\" and lastName=\"B*ć\" and jmbag>\"0000000002\"");
		List<IFieldValueGetter> fieldValueGetters = new LinkedList<IFieldValueGetter>();
		Expressions: for (ConditionalExpression expression : qf.copyOfConditionalExpressions()) {
			for(IFieldValueGetter fvg : fieldValueGetters) {
		        if (fvg.getClass() == expression.getFieldGetter().getClass()) {
		            continue Expressions;
		        }
		    }
			fieldValueGetters.add(expression.getFieldGetter());
		}
		assertEquals(3, fieldValueGetters.size());
	}
	
	@Test
	public void acceptRecord() {
		StudentRecord sr1 = new StudentRecord("0036456789", "Matić", "Ante", 5);
		QueryFilter qf = new QueryFilter("firstName>=\"A\" and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
		assertTrue(qf.accepts(sr1));
	}
	
	@Test
	public void rejectRecord() {
		StudentRecord sr1 = new StudentRecord("0036456789", "Matić", "Ante", 5);
		QueryFilter qf = new QueryFilter("firstName>=\"B\" and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
		assertFalse(qf.accepts(sr1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidSeparator() {
		new QueryFilter("firstName>=\"B\" and firstName<=\"C\" and lastName=\"M*ć\" nd jmbag=\"0036456789\"");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void literalWithNoClosingQuotes() {
		new QueryFilter("firstName>=\"B and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidFieldName() {
		new QueryFilter("firsstName>=\"B\" and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void andAtTheEnd() {
		new QueryFilter("firstName>=\"B\" and firstName<=\"C\" and lastName=\"M*ć\" and ");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void unsupportedComparisonOperator() {
		new QueryFilter("firstName<>\"B\" and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void emptyQuery() {
		new QueryFilter("firstName<>\"B\" and firstName<=\"C\" and lastName=\"M*ć\" and jmbag=\"0036456789\"");
	}
	
	@Test
	public void lessThanParsing() {
		QueryFilter qf = new QueryFilter("jmbag=\"0000000003\" and lastName<\"B\" and firstName<=\"C\"");
		boolean found = false;
		for (ConditionalExpression expr : qf.copyOfConditionalExpressions()) {
			if (expr.getComparisonOperator() instanceof LessThanComparator) {
				found = true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void distinctOperatorParsing() {
		QueryFilter qf = new QueryFilter("jmbag=\"0000000003\" and lastName!=\"B\" and firstName<=\"C\"");
		boolean found = false;
		for (ConditionalExpression expr : qf.copyOfConditionalExpressions()) {
			if (expr.getComparisonOperator() instanceof DistinctOperator) {
				found = true;
			}
		}
		assertTrue(found);
	}
	/*new QueryFilter("lastName=\"B*\"");
	new QueryFilter("firstName>\"A\" and lastName=\"B*ć\" and ");
	new QueryFilter("firstName>\"A\" and firstName<\"C\" and lastName!=\"B*ć\" and jmbag=\"0000000003\"");
	*/
	
	//TODO test simple query filter(StudenRecord);
}
