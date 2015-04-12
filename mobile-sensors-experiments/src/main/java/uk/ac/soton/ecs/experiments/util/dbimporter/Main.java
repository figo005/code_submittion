package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.sql.Connection;

public class Main {
	public static void main(String[] args) throws Exception {
		String mainDirectory = "/home/rs06r/workspace/nonmyopicresults/coordinate/";

		Connection openConnection = DBUtils.openConnection();

		ResultsDBImporter importer = new ResultsDBImporter(openConnection,
				mainDirectory);

		// importer.truncate();
		importer.traverseDirectory();

	}
}
