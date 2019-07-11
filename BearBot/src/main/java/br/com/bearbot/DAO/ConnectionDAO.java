package br.com.bearbot.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDAO {
	private final String URL = "";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String user = "";
	private final String password = "";
	private Connection connection;

	public void connect() throws ClassNotFoundException, SQLException {

		Class.forName(driver);
		connection = DriverManager.getConnection(URL, user, password);

	}

	public Connection db() {
		return connection;
	}

	public void disconnect() throws SQLException {
		connection.close();
	}

}
