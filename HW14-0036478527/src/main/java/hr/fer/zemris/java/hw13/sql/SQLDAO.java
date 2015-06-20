package hr.fer.zemris.java.hw13.sql;

import hr.fer.zemris.java.hw13.dao.DAO;
import hr.fer.zemris.java.hw13.dao.DAOException;
import hr.fer.zemris.java.hw13.model.PollDefinition;
import hr.fer.zemris.java.hw13.model.PollEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link DAO} subsystem using SQL. This implementation
 * expects that connection is available through {@link SQLConnectionProvider}
 * class.
 * 
 * @author Luka Skugor
 *
 */
public class SQLDAO implements DAO {

	/**
	 * Name of the polls table.
	 */
	public static final String POLLS_TABLE = "Polls";
	/**
	 * Name of the poll options table.
	 */
	public static final String POLL_OPTIONS_TABLE = "PollOptions";

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw13.dao.DAO#getPollEntries(long)
	 */
	@Override
	public List<PollEntry> getPollEntries(long pollID) throws DAOException {
		List<PollEntry> entryList = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();

		try {
			try (PreparedStatement pst = con
					.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from "
							+ POLL_OPTIONS_TABLE
							+ " WHERE pollid = ? order by id");) {
				pst.setLong(1, pollID);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs != null && rs.next()) {
						PollEntry entry = new PollEntry();
						entry.setId(rs.getLong(1));
						entry.setOptionTitle(rs.getString(2));
						entry.setOptionLink(rs.getString(3));
						entry.setPollId(rs.getLong(4));
						entry.setVotesCount(rs.getLong(5));

						entryList.add(entry);
					}
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error in getting poll options list", ex);
		}
		return entryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw13.dao.DAO#getPollEntry(long)
	 */
	@Override
	public PollEntry getPollEntry(long id) throws DAOException {
		PollEntry entry = null;
		Connection con = SQLConnectionProvider.getConnection();
		try {
			try (PreparedStatement pst = con
					.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from "
							+ POLL_OPTIONS_TABLE + " where id=?");) {
				pst.setLong(1, Long.valueOf(id));
				try (ResultSet rs = pst.executeQuery()) {
					if (rs != null && rs.next()) {
						entry = new PollEntry();
						entry.setId(rs.getLong(1));
						entry.setOptionTitle(rs.getString(2));
						entry.setOptionLink(rs.getString(3));
						entry.setPollId(rs.getLong(4));
						entry.setVotesCount(rs.getLong(5));

						return entry;
					}
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured in getting entry", ex);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw13.dao.DAO#getPolls()
	 */
	@Override
	public List<PollDefinition> getPolls() throws DAOException {
		List<PollDefinition> polls = new ArrayList<PollDefinition>();

		Connection con = SQLConnectionProvider.getConnection();
		try {
			try (PreparedStatement pst = con
					.prepareStatement("select id, title, message from "
							+ POLLS_TABLE)) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs != null && rs.next()) {
						PollDefinition entry = null;
						entry = new PollDefinition();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setMessage(rs.getString(3));

						polls.add(entry);
					}
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured in getting poll definition",
					ex);
		}
		return polls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw13.dao.DAO#giveVoteTo(long)
	 */
	@Override
	public void giveVoteTo(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		try {
			try (PreparedStatement pst = con.prepareStatement("update "
					+ POLL_OPTIONS_TABLE
					+ " SET votesCount = votesCount + 1 where id=?");) {
				pst.setLong(1, Long.valueOf(id));
				pst.executeUpdate();
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured in giving vote to entry", ex);
		}
	}

	@Override
	public PollDefinition getPoll(Long pollId) {
		PollDefinition entry = null;
		Connection con = SQLConnectionProvider.getConnection();
		try {
			try (PreparedStatement pst = con
					.prepareStatement("select id, title, message from "
							+ POLLS_TABLE + " where id=?")) {
				pst.setLong(1, pollId);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs != null && rs.next()) {
						entry = new PollDefinition();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setMessage(rs.getString(3));

						return entry;
					}
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured in getting poll definition",
					ex);
		}
		return null;
	}

}
