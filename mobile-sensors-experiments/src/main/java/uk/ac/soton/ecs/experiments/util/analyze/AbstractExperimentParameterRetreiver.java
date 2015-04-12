package uk.ac.soton.ecs.experiments.util.analyze;

import java.sql.Connection;

public abstract class AbstractExperimentParameterRetreiver implements
		ExperimentParameterRetreiver {

	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public AbstractExperimentParameterRetreiver() {
	}

	public String getParameterValue(ExperimentalResults results) {
		return getParameter(connection, results);
	}

	protected abstract String getParameter(Connection connection,
			ExperimentalResults results);
}
