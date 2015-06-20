package hr.fer.zemris.java.hw13.dao;

import hr.fer.zemris.java.hw13.model.PollDefinition;
import hr.fer.zemris.java.hw13.model.PollEntry;

import java.util.List;


/**
 * Interface of subsystem for keeping data persistency.
 * 
 * @author Luka Skugor
 *
 */
public interface DAO {
	
	/**
	 * Gives a single vote to an option with given ID.
	 * @param id ID of the option
	 * @throws DAOException if DAOException occurs
	 */
	public void giveVoteTo(long id) throws DAOException;

	/**
	 * Gets all poll options for a single poll.
	 * @param pollID id of the poll
	 * @return poll options
	 * @throws DAOException if DAOException occurs
	 */
	public List<PollEntry> getPollEntries(long pollID) throws DAOException;

	/**
	 * Gets a single poll option with given id.
	 * @param id id of the option
	 * @return requested poll option
	 * @throws DAOException if DAOException occurs
	 */
	public PollEntry getPollEntry(long id) throws DAOException;

	/**
	 * Gets all polls.
	 * @return all polls
	 * @throws DAOException if DAOException occurs
	 */
	public List<PollDefinition> getPolls() throws DAOException;

	/**
	 * Gets poll definition.
	 * @param pollId poll's id
	 * @return poll definition
	 */
	public PollDefinition getPoll(Long pollId);
	
}
