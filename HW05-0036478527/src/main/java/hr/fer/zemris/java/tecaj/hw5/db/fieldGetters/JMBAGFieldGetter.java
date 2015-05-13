package hr.fer.zemris.java.tecaj.hw5.db.fieldGetters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Implementation of the IFieldValueGetter for getting the JMBAG from a student record.
 * @author Luka Škugor
 *
 */
public class JMBAGFieldGetter implements IFieldValueGetter {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.tecaj.hw5.db.fieldGetters.IFieldValueGetter#get(hr.fer.zemris.java.tecaj.hw5.db.StudentRecord)
	 */
	public String get(StudentRecord record) {
		return record.getJMBAG();
	}
	
}
