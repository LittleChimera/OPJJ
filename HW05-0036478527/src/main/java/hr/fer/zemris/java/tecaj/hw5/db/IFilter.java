package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Interface on which every filtering implementation of StudentRecord is built.
 * @author Luka Škugor
 *
 */
public interface IFilter {
	/**
	 * Checks if StudentRecord matches filtering.
	 * @param record StudentRecord which will be checked
	 * @return true if record matches filtering, else false
	 */
	public boolean accepts(StudentRecord record);
}
