package br.com.bearbot.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerifyExistenceDAO {
	public boolean checkExistence(long guildId) {
		ConnectionDAO connection = new ConnectionDAO();

		try {
			connection.connect();
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} catch (SQLException e) {
//			e.printStackTrace();
		}

		try {
			String query = "SELECT * from guildserver where serverid = " + guildId;

			PreparedStatement sets = connection.db().prepareStatement(query);
			ResultSet results = sets.executeQuery();

			while (results.next()) {
				return true;
			}
			connection.disconnect();
		} catch (Exception e) {
		}

		return false;
	}
}
