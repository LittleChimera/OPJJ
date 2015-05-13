package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class QueryCommandTest {

	@Test
	public void sampleQuery() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		QueryCommand qc = new QueryCommand("Query lastName = \"M*\"", db1);
		String result = qc.formatQueryResult();
		int countOfNewLines = result.length() - result.replace("\n", "").length();
		assertEquals(10, countOfNewLines);
	}
	
	@Test
	public void noResults() {
		StudentDatabase db1 = new StudentDatabase(new LinkedList<String>());
		QueryCommand qc = new QueryCommand("Query lastName = \"M*\"", db1);
		String result = qc.formatQueryResult();
		assertEquals(0, result.length());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidQueryStart(){
		new QueryCommand("qury lastName < A and JMBAG>0000000001", null);
	}
	
	@Test
	public void queryForStudentJmbagAccepts() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		QueryCommand qc = new QueryCommand("Query jmbag = \"0000000033\" and lastName = \"M*\"", db1);
		String result = qc.formatQueryResult();
		int countOfNewLines = result.length() - result.replace("\n", "").length();
		assertEquals(3, countOfNewLines);
	}
	
	@Test
	public void queryForStudentJmbagRejects() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		QueryCommand qc = new QueryCommand("Query jmbag = \"0000000033\" and lastName = \"K*\"", db1);
		String result = qc.formatQueryResult();
		int countOfNewLines = result.length() - result.replace("\n", "").length();
		assertEquals(0, countOfNewLines);
	}
	
	@Test
	public void twoDistinctJMBAGsGiveEmptyResult() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),
				StandardCharsets.UTF_8);
		StudentDatabase db1 = new StudentDatabase(lines);
		QueryCommand qc = new QueryCommand("Query jmbag = \"0000000033\" and jmbag = \"0000000023\"", db1);
		String result = qc.formatQueryResult();
		assertEquals("", result);
	}

}
