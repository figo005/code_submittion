package uk.ac.soton.ecs.experiments.util.analyze;

import java.sql.Connection;

public interface ExperimentParameterRetreiver {

	String getParameterValue(ExperimentalResults results);

	String getParameterName();

	void setConnection(Connection connection);

}
