package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.utils.CompressionUtils;

public class ExperimentSet {

	private static final String RESULTS_DIR = "runs";
	private static final String TABLE_NAME = "experiment_set";
	private ExperimentGroup experimentGroup;
	private Connection connection;
	private File directory;
	private String name;
	private int id;

	public ExperimentSet(ExperimentGroup experimentGroup,
			Connection connection, File file) {
		this.experimentGroup = experimentGroup;
		this.connection = connection;
		this.directory = file;
	}

	public void process(final boolean commitIntoDB) {
		File nameFile = new File(directory, "name");

		// Validate.isTrue(nameFile.exists(), "'name' file does not exist in "
		// + directory);
		File resultsDir = new File(directory, RESULTS_DIR);

		if (!resultsDir.exists())
			return;

		Validate.isTrue(resultsDir.exists(), "Directory "
				+ resultsDir.getAbsolutePath() + " does not exist");

		try {
			if (nameFile.exists()) {
				name = (String) FileUtils.readLines(nameFile).get(0);
			} else {
				name = directory.getName();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File[] zipFiles = resultsDir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".zip");
			}
		});

		for (File file : zipFiles) {
			CompressionUtils.unzip(file, resultsDir);
		}

		if (commitIntoDB)
			commitIntoDB();

		File[] files = resultsDir.listFiles();

		ExecutorService threadPool = Executors.newFixedThreadPool(10);

		for (final File file : files) {
			if (file.isDirectory()) {
				if (new File(file, "endtime.txt").exists()) {
					threadPool.execute(new Runnable() {
						public void run() {
							processSingleExperimentResult(file, commitIntoDB);
						}
					});
				} else {
					if (new File(file, "error.txt").exists()) {
						System.err.println("[FAILED] experiment in directory "
								+ file + " has failed");
					} else {
						System.out
								.println("[UNFINISHED] experiment in directory "
										+ file + " has not finished (yet)");
					}
				}
			}
		}

		try {
			threadPool.shutdown();
			System.out.println(threadPool
					.awaitTermination(10, TimeUnit.MINUTES));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void processSingleExperimentResult(final File experimentDirectory,
			final boolean commitIntoDB) {
		Runnable runnable = new Runnable() {

			public void run() {
				SingleExperimentResult experimentResult = new SingleExperimentResult(
						ExperimentSet.this, connection, experimentDirectory);
				experimentResult.process(commitIntoDB);
			}
		};

		runnable.run();
		// new Thread(runnable).start();
	}

	private void commitIntoDB() {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO " + TABLE_NAME + " VALUES(0,'"
					+ name + "','" + experimentGroup.getID() + "');",
					Statement.RETURN_GENERATED_KEYS);

			ResultSet resultSet = statement.getGeneratedKeys();

			if (resultSet.next())
				id = resultSet.getInt(1);

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ExperimentGroup getExperimentGroup() {
		return experimentGroup;
	}

	public int getId() {
		return id;
	}

	public static void truncate(Connection connection) throws SQLException {
		SingleExperimentResult.truncate(connection);
		DBUtils.truncate(connection, TABLE_NAME);
	}

}
