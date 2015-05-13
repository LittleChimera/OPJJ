package hr.fer.zemris.java.tecaj.hw5.db.fieldGetters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Root of implementation of all getter which get a single data from a StudentRecord.
 * @author Luka Škugor
 *
 */
public interface IFieldValueGetter {
	/**
	 * Gets the data from the student record.
	 * @param record record from which data is pulled
	 * @return get data
	 */
	public String get(StudentRecord record);

}
