package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of database storing StudentRecords. Primary key is JMBAG.
 * 
 * @author Luka Škugor
 *
 */
public class StudentDatabase {

	/**
	 * Storage of student records.
	 */
	Map<String, StudentRecord> records;

	/**
	 * Creates a new student database with given list of records.
	 * 
	 * @param records
	 *            list of student records
	 */
	public StudentDatabase(List<String> records) {
		this.records = new LinkedHashMap<>();

		for (String stringRecord : records) {
			String[] recordArray = stringRecord.split("\\t");
			if (recordArray.length != 4) {
				throw new IllegalArgumentException(stringRecord
						+ ": Invalid student record format");
			}
			try {
				this.records.put(
						recordArray[0],
						new StudentRecord(recordArray[0], recordArray[1],
								recordArray[2], Integer
										.parseInt(recordArray[3])));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(recordArray[3] + ": "
						+ StudentRecord.GRADE_ERROR_MSG);
			}

		}
	}

	/**
	 * Gets indexed StudentRecord for given JMBAG.
	 * 
	 * @param jmbag
	 *            student's JMBAG
	 * @return student's record
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return records.get(jmbag);
	}

	/**
	 * Applies filtering on all student records and returns a list of students
	 * records which match the filtering.
	 * 
	 * @param filter
	 *            filtering method with which records are filtered
	 * @return list of filtered student records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> result = new LinkedList<>();

		for (StudentRecord record : records.values()) {
			if (filter.accepts(record)) {
				result.add(record);
			}
		}
		return result;
	}

}
