package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;

public interface ConfigurationValueHandler {

	String handle(File directory);

	ColumnName getColumnName();

}
