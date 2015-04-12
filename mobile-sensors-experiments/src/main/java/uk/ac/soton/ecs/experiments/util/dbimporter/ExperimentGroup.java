package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.Validate;

public class ExperimentGroup {

	public static final String NAME_FILE = "name";
	public static final String TABLE = "experiment_group";

	private Connection connection;
	private File mainDirectory;
	private String name;
	private int id;

	public ExperimentGroup(String mainDirectory, Connection connection) {
		this.connection = connection;
		this.mainDirectory = new File(mainDirectory);
	}

	public void process() {
		process(true);
	}

	public void process(final boolean commitIntoDB) {
		Validate.isTrue(mainDirectory.exists(), "Directory ", mainDirectory
				+ " does not exist");
		File nameFile = new File(mainDirectory, NAME_FILE);

		try {
			if (nameFile.exists()) {
				name = (String) FileUtils.readLines(nameFile).get(0);
			} else {
				name = "undefined";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (commitIntoDB) {
			try {
				commitIntoDB();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		File[] files = mainDirectory
				.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		for (final File file : files) {
			// only import directories that contain experiments
			if (new File(file, "experiment_name").exists()) {

				ExperimentSet experimentSet = new ExperimentSet(
						ExperimentGroup.this, connection, file);
				experimentSet.process(commitIntoDB);
			}
		}

		System.out.println("Done");
	}

	private void commitIntoDB() throws Exception {
		try {
			connection = DBUtils.openConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO " + TABLE + " VALUES(0,'"
					+ name + "');", Statement.RETURN_GENERATED_KEYS);

			ResultSet resultSet = statement.getGeneratedKeys();

			if (resultSet.next())
				id = resultSet.getInt(1);

			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public int getID() {
		return id;
	}

	public static void truncate(Connection connection) throws SQLException {
		ExperimentSet.truncate(connection);
		DBUtils.truncate(connection, TABLE);

	}
}
