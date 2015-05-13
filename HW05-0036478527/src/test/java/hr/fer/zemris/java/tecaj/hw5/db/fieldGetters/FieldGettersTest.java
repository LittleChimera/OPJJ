package hr.fer.zemris.java.tecaj.hw5.db.fieldGetters;

import static org.junit.Assert.*;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

import org.junit.Test;

public class FieldGettersTest {
	
	StudentRecord sr = new StudentRecord("1234567890", "Murdock", "Mirko", 3);
	
	@Test
	public void getJMBAG() {
		assertEquals((new JMBAGFieldGetter()).get(sr), "1234567890");
	}
	
	@Test
	public void getLastName() {
		assertEquals((new LastNameFieldGetter()).get(sr), "Murdock");
	}
	
	@Test
	public void getFirstName() {
		assertEquals((new FirstNameFieldGetter()).get(sr), "Mirko");
	}

}
