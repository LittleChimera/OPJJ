package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class StudentRecordTest {

	@Test
	public void equalsTrue() {
		StudentRecord sr1 = new StudentRecord("0036456789", "Ante", "Matić", 5);
		StudentRecord sr2 = new StudentRecord("0036456789", "Ante", "Matić", 5);
		assertTrue(sr1.equals(sr2));
	}
	
	@Test
	public void equalsDifferentJMBAGs() {
		StudentRecord sr1 = new StudentRecord("0036456889", "Ante", "Matić", 5);
		StudentRecord sr2 = new StudentRecord("0036456789", "Ante", "Matić", 5);
		assertFalse(sr1.equals(sr2));
	}
	
	@Test
	public void equalsCompareWithNull() {
		StudentRecord sr1 = new StudentRecord("0036456889", "Ante", "Matić", 5);
		StudentRecord sr2 = null;
		assertFalse(sr1.equals(sr2));
	}
	
	@Test
	public void compareWithDifferentObject() {
		StudentRecord sr1 = new StudentRecord("0036456889", "Ante", "Matić", 5);
		String sr2 = "0036456889";
		assertFalse(sr1.equals(sr2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void JMBAGshort() {
		new StudentRecord("003645889", "Ante", "Matić", 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void JMBAGlong() {
		new StudentRecord("00364586689", "Ante", "Matić", 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void JMBAGnotDigits() {
		new StudentRecord("0036458g89", "Ante", "Matić", 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void firstNameInvalid() {
		new StudentRecord("0036458089", "Ante.", "Matić", 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void lastNameInvalid() {
		new StudentRecord("0036458089", "Ante", "Matić.", 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void gradeInvalid() {
		new StudentRecord("0036458089", "Ante", "Matić.", 3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidNameStartingWithDash() {
		new StudentRecord("0036458089", "Ante", "-Matić", 3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidNameEndingWithDash() {
		new StudentRecord("0036458g89", "Ante-", "Matić", 3);
	}
	
	@Test
	public void validStudentRecord() {
		StudentRecord sr1 = new StudentRecord("0036458289", "Ana-Marija", "Matić", 3);
		assertEquals(sr1.getJMBAG(), "0036458289");
		assertEquals(sr1.getLastName(), "Ana-Marija");
		assertEquals(sr1.getFirstName(), "Matić");
		assertEquals(sr1.getFinalGrade(), 3);
	}
	
	@Test
	public void testHashValue() {
		StudentRecord sr1 = new StudentRecord("0036456789", "Ante", "Matić", 5);
		assertEquals(sr1.hashCode(), "0036456789".hashCode());
	}
	
	@Test
	public void asString() {
		StudentRecord sr1 = new StudentRecord("0036456789", "Ante", "Matić", 5);
		assertEquals(sr1.toString(), "0036456789 Ante Matić 5");
	}

}
