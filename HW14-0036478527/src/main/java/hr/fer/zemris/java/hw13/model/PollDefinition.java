package hr.fer.zemris.java.hw13.model;

/**
 * Represents a poll definition.
 * @author Luka Skugor
 *
 */
public class PollDefinition {
	
	/**
	 * poll id.
	 */
	private Long id;
	/**
	 * poll title.
	 */
	private String title;
	/**
	 * Poll message.
	 */
	private String message;
	
	/**
	 * Gets poll id.
	 * @return poll id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Sets poll id.
	 * @param id poll id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets poll title.
	 * @return poll title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets poll title
	 * @param title poll title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Gets poll message
	 * @return poll message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Sets poll message.
	 * @param message poll message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
