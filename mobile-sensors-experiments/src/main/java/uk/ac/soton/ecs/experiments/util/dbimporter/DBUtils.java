package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {

	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/";
	private static final String DATABASE = "experiments";

	public static Connection openConnection() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("user", USER);
		properties.setProperty("password", PASSWORD);
		Class.forName(DRIVER).newInstance();

		return DriverManager.getConnection(URL + DATABASE, properties);
	}

	public static void truncate(Connection connection, String tableName)
			throws SQLException {
		Statement statement = connection.createStatement();

		statement.executeUpdate("TRUNCATE " + tableName + ";");

		statement.close();
	}
}
