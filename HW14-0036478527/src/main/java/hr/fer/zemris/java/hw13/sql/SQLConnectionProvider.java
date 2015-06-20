package hr.fer.zemris.java.hw13.sql;

import java.sql.Connection;


/**
 * Provider of SQL connections.
 * 
 * @author Luka Skugor
 *
 */
public class SQLConnectionProvider {

	/**
	 * Connections mapped to their threads.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	
	/**
	 * Sets SQL connection.
	 * @param con sql connection
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets SQL connection.
	 * @return SQL connection
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}
