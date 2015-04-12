package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class SingleExperimentResult {

	private static final String SIMULATION_FILE = "simulation.xml";
	private static final String SENSOR_FILE = "sensor.xml";
	private static final String TABLE_NAME = "experiment_result";
	private static final String EXPERIMENT_GROUP_ID = "experiment_set_id";
	private ExperimentSet experimentSet;
	private Connection connection;
	private File directory;
	private File resultDirectory;
	private Map<String, String> recordEntries = new HashMap<String, String>();
	private static Collection<ConfigurationValueHandler> experimentConfigurationValueHandler = new ArrayList<ConfigurationValueHandler>();

	static {
		experimentConfigurationValueHandler
				.add(new CommunicationProbabilityValueHandler());
		experimentConfigurationValueHandler
				.add(new CommunicationRangeValueHandler());
		experimentConfigurationValueHandler.add(new SensorCountValueHandler());
		experimentConfigurationValueHandler.add(new AverageRMSValueHandler());
		experimentConfigurationValueHandler.add(new MessageCountValueHandler());
		experimentConfigurationValueHandler
				.add(new AverageSensorPairsInRangeValueHandler());
		experimentConfigurationValueHandler.add(new StartTimeValueHandler());
		experimentConfigurationValueHandler.add(new EndTimeValueHandler());
		experimentConfigurationValueHandler.add(new GammaHandler());
		experimentConfigurationValueHandler.add(new TauHandler());
		experimentConfigurationValueHandler.add(new LengthScaleValueHandler());
		experimentConfigurationValueHandler.add(new TimeScaleValueHandler());
		experimentConfigurationValueHandler
				.addAll(new BBStatisticsCompositeHandler().getHandlers());
		experimentConfigurationValueHandler
				.addAll(new BoundedMaxSumStatisticsCompositeHandler()
						.getHandlers());
		experimentConfigurationValueHandler.add(new CaptureTimeHandler());
		experimentConfigurationValueHandler.add(new PatrollingLossHandler());
		experimentConfigurationValueHandler
				.add(new AverageSpatialFieldValueHandler());
		experimentConfigurationValueHandler
				.add(new MaxSpatialFieldValueHandler());
		experimentConfigurationValueHandler
				.add(new AverageEntropyValueHandler());
		experimentConfigurationValueHandler.add(new RandomSeedValueHandler());
		experimentConfigurationValueHandler.add(new StateCountBeforeHandler());
		experimentConfigurationValueHandler.add(new StateCountAfterHandler());
		experimentConfigurationValueHandler
				.add(new ActualObservationValueHandler());
		experimentConfigurationValueHandler.add(new ExpectedRewardHandler());
		experimentConfigurationValueHandler.add(new ActualRewardHandler());
		experimentConfigurationValueHandler
				.add(new UncertaintyIncrementValueHandler());
	}

	public SingleExperimentResult(ExperimentSet experimentSet,
			Connection connection, File file) {
		this.experimentSet = experimentSet;
		this.connection = connection;
		this.directory = file;
		this.resultDirectory = directory;
	}

	public void process(boolean commitIntoDB) {
		System.out.println("Processing " + directory);

		File sensorFile = new File(resultDirectory, SENSOR_FILE);
		File simulationFile = new File(resultDirectory, SIMULATION_FILE);
		Validate.isTrue(sensorFile.exists(), sensorFile + " does not exist");
		Validate.isTrue(simulationFile.exists());
		recordEntries.put(EXPERIMENT_GROUP_ID, experimentSet.getId() + "");

		for (ConfigurationValueHandler handler : experimentConfigurationValueHandler) {
			String columnName = handler.getColumnName().toString();
			String value = handler.handle(resultDirectory);
			recordEntries.put(columnName, value);
		}

		if (commitIntoDB)
			commitIntoDB();
	}

	private void commitIntoDB() {
		String query = null;

		try {
			String columnNames = "";
			String values = "";

			for (String columnName : recordEntries.keySet()) {
				columnNames += columnName + ",";
				String value = recordEntries.get(columnName);
				if (value == null)
					value = "NULL";
				else
					value = "'" + value + "'";

				values += "" + value + ",";
			}

			columnNames = columnNames.substring(0, columnNames.length() - 1);
			values = values.substring(0, values.length() - 1);

			query = "INSERT INTO " + TABLE_NAME + " (" + columnNames
					+ ") VALUES " + " (" + values + ");";

			Statement statement = connection.createStatement();

			statement.execute(query);
			statement.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(query);

			e.printStackTrace();
		}
	}

	public static void truncate(Connection connection) throws SQLException {
		DBUtils.truncate(connection, TABLE_NAME);
	}
}
