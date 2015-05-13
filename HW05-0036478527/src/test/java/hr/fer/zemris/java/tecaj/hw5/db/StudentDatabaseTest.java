package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class StudentDatabaseTest {

	@Test
	public void readValidDatabase() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		assertEquals(db1.filter((record) -> true).size(), 63);

	}

	@Test(expected = IllegalArgumentException.class)
	public void readInvalidGradeDatabase() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./dbfailGrade.txt"),
				StandardCharsets.UTF_8);
		new StudentDatabase(lines);

	}

	@Test(expected = IllegalArgumentException.class)
	public void readInvalidLineDatabase() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./dbfailLine.txt"),
				StandardCharsets.UTF_8);
		new StudentDatabase(lines);

	}
	
	@Test
	public void filterRejectSome() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		assertEquals(db1.filter(new IFilter() {
			
			@Override
			public boolean accepts(StudentRecord record) {
				if (record.getFirstName().matches("M.*")) {
					return true;
				}
				return false;
			}
		}).size(), 8);
		
	}
	
	@Test
	public void getIndexedJMBAG()  {
		List<String> records = new LinkedList<String>();
		records.add("0000000001	asdf	fda	2");
		records.add("0000000002	lalla	Boss	3");
		records.add("0000000045	tatat	Fijko	5");
		records.add("0000000007	Ullal	misko	3");
		records.add("0000000031	hihi	Mirko	2");
		StudentDatabase db1 = new StudentDatabase(records);
		StudentRecord getRecord = db1.forJMBAG("0000000031");
		assertEquals("0000000031", getRecord.getJMBAG());
		assertEquals(2, getRecord.getFinalGrade());
		assertEquals("Mirko", getRecord.getFirstName());
		assertEquals("hihi", getRecord.getLastName());
		
	}

}
