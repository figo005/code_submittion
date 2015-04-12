package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * expected directory structure: - multiple directories, for every simulation
 * set one - inside: runs/run* - inside these, simulations.xml, sensor.xml
 * 
 * @author rs06r
 * 
 */
public class ResultsDBImporter {

	private Connection connection;
	private String rootDirectory;

	public ResultsDBImporter(Connection connection, String rootDirectory) {
		this.rootDirectory = rootDirectory;
		this.connection = connection;
	}

	public static void main(String[] args) throws Exception {
		String mainDirectory = "/media/data/nonmyopicresults/experiment1a";
		// mainDirectory = "/tmp/bla";
		boolean truncate = true;

		Connection openConnection = DBUtils.openConnection();

		ResultsDBImporter importer = new ResultsDBImporter(openConnection,
				mainDirectory);

		if (truncate)
			importer.truncate();

		importer.traverseDirectory();
	}

	public void traverseDirectory() {
		// within the root directory, expect individual experiments

		ExperimentGroup experimentResultSet = new ExperimentGroup(
				rootDirectory, connection);

		experimentResultSet.process();

		System.out.println("Result Set Committed as ID "
				+ experimentResultSet.getID());
	}

	public void truncate() throws SQLException {
		ExperimentGroup.truncate(connection);
	}
}
