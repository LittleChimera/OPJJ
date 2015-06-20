package hr.fer.zemris.java.hw13.model;

/**
 * Represents a poll option.
 * @author Luka Skugor
 *
 */
public class PollEntry {
	
	/**
	 * option id.
	 */
	private Long id;
	/**
	 * Option title.
	 */
	private String optionTitle;
	/**
	 * Option link.
	 */
	private String optionLink;
	/**
	 * Id of the poll this option belongs to.
	 */
	private Long pollId;
	/**
	 * Option's vote count.
	 */
	private Long votesCount;
	
	/**
	 * Gets option id.
	 * @return option id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Sets option id.
	 * @param id option id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets option title.
	 * @return option title
	 */
	public String getOptionTitle() {
		return optionTitle;
	}
	/**
	 * Sets option title.
	 * @param optionTitle option title
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	/**
	 * Gets option link.
	 * @return option link.
	 */
	public String getOptionLink() {
		return optionLink;
	}
	/**
	 * Sets option link.
	 * @param optionLink option link
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}
	/**
	 * Gets id of the poll this option belongs to.
	 * @return poll id
	 */
	public Long getPollId() {
		return pollId;
	}
	/**
	 * Sets id of the poll this option belongs to.
	 * @param pollId poll id
	 */
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}
	/**
	 * Gets votes count.
	 * @return votes count
	 */
	public Long getVotesCount() {
		return votesCount;
	}
	/**
	 * Sets votes count.
	 * @param votesCount votes count
	 */
	public void setVotesCount(Long votesCount) {
		this.votesCount = votesCount;
	}

}
