package hr.fer.zemris.java.hw13.dbconnection;

import hr.fer.zemris.java.hw13.dao.DAOException;
import hr.fer.zemris.java.hw13.model.PollDefinition;
import hr.fer.zemris.java.hw13.model.PollEntry;
import hr.fer.zemris.java.hw13.sql.SQLConnectionProvider;
import hr.fer.zemris.java.hw13.sql.SQLDAO;
import hr.fer.zemris.java.hw13.voting.DatabaseLoaderUtility;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Initializes connection between app and database and creates needed databases
 * if they do not exist.
 * 
 * @author Luka Skugor
 *
 */
@WebListener
public class ConnectionPoolInitializer implements ServletContextListener {

	/**
	 * Path to definition of the polls.
	 */
	private static final String POLLS_DEFINITION_PATH = "/WEB-INF/polls-definition.txt";
	/**
	 * Path to all poll options.
	 */
	private static final String POLLS_OPTIONS_PATH = "/WEB-INF/polls-options.txt";
	/**
	 * Path to database connection settings.
	 */
	private static final String dbSettings = "/WEB-INF/dbsettings.properties";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		Properties dbProperties = new Properties();
		Path dbSettingsPath = Paths.get(sce.getServletContext().getRealPath(
				dbSettings));
		try {
			dbProperties.load(Files.newInputStream(dbSettingsPath));
		} catch (IOException noConfig) {
			throw new RuntimeException("Missing databse configuration path: "
					+ dbSettingsPath);
		}

		String connectionURL = "jdbc:derby://"
				+ dbProperties.getProperty("host") + "/"
				+ dbProperties.getProperty("name") + ";user="
				+ dbProperties.getProperty("user") + ";password="
				+ dbProperties.getProperty("password");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error occured while getting driver.",
					e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		if (createTables(cpds)) {
			try {
				fillTables(sce.getServletContext(), cpds);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Fills table with content from {@link #POLLS_DEFINITION_PATH} and
	 * {@link #POLLS_OPTIONS_PATH}.
	 * 
	 * @param servletContext
	 *            servlet context
	 * @param dataSource
	 *            source providing connection to database
	 * @throws DAOException
	 *             if database error occurs
	 */
	private void fillTables(ServletContext servletContext, DataSource dataSource)
			throws DAOException {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e1) {
			throw new RuntimeException(
					"Error occured when connecting to database", e1);
		}

		Map<Long, Long> idTranslate = new HashMap<Long, Long>();
		try {
			List<PollDefinition> polls = DatabaseLoaderUtility
					.loadPolls(servletContext
							.getRealPath(POLLS_DEFINITION_PATH));
			for (PollDefinition poll : polls) {
				try {
					PreparedStatement pst = con.prepareStatement("INSERT INTO "
							+ SQLDAO.POLLS_TABLE
							+ " (title, message) VALUES (?, ?)",
							Statement.RETURN_GENERATED_KEYS);
					pst.setString(1, poll.getTitle());
					pst.setString(2, poll.getMessage());
					pst.executeUpdate();

					ResultSet resultSet = pst.getGeneratedKeys();
					resultSet.next();
					Long generatedId = resultSet.getLong(1);
					idTranslate.put(poll.getId(), generatedId);
					resultSet.close();
					pst.close();
				} catch (Exception e) {
					throw new DAOException("Failed to insert values");
				}
			}

			List<PollEntry> pollOptions = DatabaseLoaderUtility
					.loadPollEntries(servletContext
							.getRealPath(POLLS_OPTIONS_PATH));
			for (PollEntry option : pollOptions) {
				try (PreparedStatement pst = con
						.prepareStatement("INSERT INTO "
								+ SQLDAO.POLL_OPTIONS_TABLE
								+ " (optionTitle, optionLink, pollId, votesCount) VALUES (?, ?, ?, ?)")) {
					pst.setString(1, option.getOptionTitle());
					pst.setString(2, option.getOptionLink());
					pst.setLong(3, idTranslate.get(option.getPollId()));
					pst.setLong(4, 0);
					pst.executeUpdate();
				} catch (Exception e) {
					throw new DAOException("Failed to insert values");
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured in filling tables.", ex);
		}
	}

	/**
	 * Creates required tables if they do not exist.
	 * 
	 * @param dataSource
	 *            database connection provider
	 * @return true if all tables were created, otherwise false
	 */
	private boolean createTables(DataSource dataSource) {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e1) {
			throw new RuntimeException(
					"Error occured when connecting to database", e1);
		}

		try {
			try (PreparedStatement pst = con.prepareStatement("CREATE TABLE "
					+ SQLDAO.POLLS_TABLE
					+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					+ "title VARCHAR(150) NOT NULL,"
					+ "message CLOB(2048) NOT NULL" + ")")) {
				pst.execute();
			} catch (Exception e) {
				if (tableExists(SQLDAO.POLLS_TABLE, con)) {
					return false;
				}
				throw e;
			}
			try (PreparedStatement pst = con.prepareStatement("CREATE TABLE "
					+ SQLDAO.POLL_OPTIONS_TABLE
					+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					+ "optionTitle VARCHAR(100) NOT NULL,"
					+ "optionLink VARCHAR(150) NOT NULL,"
					+ "pollID BIGINT,votesCount BIGINT,"
					+ "FOREIGN KEY (pollID) REFERENCES Polls(id)" + ")")) {
				pst.execute();
			} catch (SQLException e) {
				if (tableExists(SQLDAO.POLL_OPTIONS_TABLE, con)) {
					return false;
				}
				throw e;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error when creating tables.", ex);
		}
		return true;
	}

	/**
	 * Checks if table exists.
	 * 
	 * @param tableName
	 *            name of the table
	 * @param con
	 *            connection to the database
	 * @return true if table exists, otherwise false
	 * @throws SQLException
	 *             if database error occurs
	 */
	private boolean tableExists(String tableName, Connection con)
			throws SQLException {
		if (con != null) {
			DatabaseMetaData dbmd = con.getMetaData();
			try (ResultSet rs = dbmd.getTables(null, null,
					tableName.toUpperCase(), null)) {
				return rs.next();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}