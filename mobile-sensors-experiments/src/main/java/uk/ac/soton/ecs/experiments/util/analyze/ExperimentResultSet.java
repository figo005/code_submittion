package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.Collection;
import java.util.List;

public interface ExperimentResultSet {

	Collection<ExperimentParameterRetreiver> getParameterRetreivers();

	List<GraphParameter> getGraphParameters();

	List<TableParameter> getTableParameters();

}
