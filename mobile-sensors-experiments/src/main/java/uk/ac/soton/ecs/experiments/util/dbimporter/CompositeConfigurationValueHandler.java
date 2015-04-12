package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.util.Collection;

public interface CompositeConfigurationValueHandler {

	Collection<ConfigurationValueHandler> getHandlers();

}
