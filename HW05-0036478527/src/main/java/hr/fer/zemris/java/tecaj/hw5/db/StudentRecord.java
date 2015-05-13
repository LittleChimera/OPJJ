package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.regex.Pattern;

/**
 * StudentRecord is a database record representing basic information of a
 * student. It has 4 parameters: student's JMBAG, first name of the student,
 * last name of the student and studen'ts final grade.
 * 
 * @author Luka Škugor
 *
 */
public class StudentRecord {

	/**
	 * Message to print when the JMBAG has a wrong format.
	 */
	public static final String JMBAG_ERROR_MSG = "Invalid JMBAG. JMBAG should have 10 digits.";
	/**
	 * Message to print when a name has a wrong format.
	 */
	public static final String NAME_ERROR_MSG = "Invalid name format";
	/**
	 * Message to print when a grade has a wrong format.
	 */
	public static final String GRADE_ERROR_MSG = "Invalid grade format. Grade be a number between 1 and 5 both including.";

	/**
	 * Pattern of a JMBAG.
	 */
	private static final Pattern JMBAG_PATTERN = Pattern.compile("^\\d{10}$");
	/**
	 * Pattern of a name.
	 */
	private static final Pattern NAME_PATTERN = Pattern.compile(
			"^([\\w ]+|\\b\\-\\b)+$", Pattern.UNICODE_CHARACTER_CLASS);
	/**
	 * Pattern of a grade.
	 */
	private static final Pattern GRADE_PATTERN = Pattern.compile("^[1-5]$");

	/**
	 * Student's JMBAG.
	 */
	private String jmbag;
	/**
	 * Student's first name.
	 */
	private String firstName;
	/**
	 * Student's last name.
	 */
	private String lastName;
	/**
	 * Student's final grade.
	 */
	private int finalGrade;

	/**
	 * Creates a new student record with given information. Null values aren't
	 * allowed.
	 * 
	 * @param jmbag
	 *            student's JMBAG
	 * @param lastName
	 *            student's last name
	 * @param firstName
	 *            student's first name
	 * @param finalGrade
	 *            student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName,
			int finalGrade) {
		checkPattern(JMBAG_PATTERN, jmbag.trim(), JMBAG_ERROR_MSG);
		checkPattern(NAME_PATTERN, firstName.trim(), NAME_ERROR_MSG);
		checkPattern(NAME_PATTERN, lastName.trim(), NAME_ERROR_MSG);
		checkPattern(GRADE_PATTERN, Integer.toString(finalGrade),
				GRADE_ERROR_MSG);

		this.jmbag = jmbag;
		this.firstName = firstName;
		this.lastName = lastName;
		this.finalGrade = finalGrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof StudentRecord)) {
			return false;
		}
		return jmbag.equals(((StudentRecord) obj).jmbag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return jmbag.hashCode();
	}

	/**
	 * Checks an input for a pattern and if it doesn't match throws an exception
	 * with given message.
	 * 
	 * @param pattern
	 *            pattern with which input will be checked against
	 * @param input
	 *            input to check
	 * @param errorMsg
	 *            error message to print if input doesn't match
	 */
	private void checkPattern(Pattern pattern, String input, String errorMsg) {
		if (!pattern.matcher(input).matches()) {
			throw new IllegalArgumentException(input + ": " + errorMsg);
		}
	}

	/**
	 * Gets student's final grade.
	 * 
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Gets student's first name.
	 * 
	 * @return student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets student's JMBAG.
	 * 
	 * @return student's JMBAG
	 */
	public String getJMBAG() {
		return jmbag;
	}

	/**
	 * Gets student's last name.
	 * 
	 * @return student's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return jmbag + " " + lastName + " " + firstName + " " + finalGrade;
	}
}
