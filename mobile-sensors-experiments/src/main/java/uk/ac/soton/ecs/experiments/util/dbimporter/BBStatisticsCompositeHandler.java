package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Creates multiple handlers to read the bb_statistics.txt file with Branch and
 * Bound statistics.
 * 
 * @author rs06r
 * 
 */
public class BBStatisticsCompositeHandler implements
		CompositeConfigurationValueHandler {

	private ColumnName[] columnNames = { ColumnName.EXPANDED_PARTIAL_NODES,
			ColumnName.TOTAL_PARTIAL_NODES, ColumnName.FUNCTION_CALLS,
			ColumnName.CACHE_MISSES };

	private ConfigurationValueHandler createHandler(final int columnIndex) {
		return new ConfigurationValueHandler() {

			public String handle(File directory) {
				try {
					List<String> lines = FileUtils.readLines(new File(
							directory, "bb_statistics.txt"));

					return lines.get(1).split(" ")[columnIndex];
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			public ColumnName getColumnName() {
				return columnNames[columnIndex];
			}
		};
	}

	public Collection<ConfigurationValueHandler> getHandlers() {
		Collection<ConfigurationValueHandler> result = new ArrayList<ConfigurationValueHandler>();

		for (int i = 0; i < columnNames.length; i++) {
			result.add(createHandler(i));
		}

		return result;
	}

}
