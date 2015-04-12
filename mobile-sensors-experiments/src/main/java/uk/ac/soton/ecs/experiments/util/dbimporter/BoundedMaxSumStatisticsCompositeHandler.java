package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class BoundedMaxSumStatisticsCompositeHandler implements
		CompositeConfigurationValueHandler {

	private static final ColumnName IGNORE = null;
	private ColumnName[] columnNames = { IGNORE,
			ColumnName.BOUNDED_MS_TREE_VALUE,
			ColumnName.BOUNDED_MS_FACTOR_GRAPH_VALUE,
			ColumnName.BOUNDED_MS_OPTIMAL_VALUE,
			ColumnName.BOUNDED_MS_UPPER_BOUND, ColumnName.MESSAGE_SIZE,
			ColumnName.BOUNDED_MS_APPROX_RATIO };

	private ConfigurationValueHandler createHandler(final int columnIndex) {
		return new ConfigurationValueHandler() {

			public String handle(File directory) {
				try {
					List<String> lines = FileUtils.readLines(new File(
							directory, "bounded_max_sum_metrics.txt"));

					lines = lines.subList(1, lines.size());

					return getAverage(lines, columnIndex);

				} catch (IOException e) {
					return null;
				}
			}

			private String getAverage(List<String> lines, int columnIndex) {
				if (lines.size() == 0) {
					return "0.0";
				}

				double sum = 0.0;
				for (String line : lines) {
					double value = Double
							.parseDouble(line.split(" ")[columnIndex]);

					sum += value;
				}

				return "" + sum / lines.size();
			}

			public ColumnName getColumnName() {
				return columnNames[columnIndex];
			}
		};
	}

	public Collection<ConfigurationValueHandler> getHandlers() {
		Collection<ConfigurationValueHandler> result = new ArrayList<ConfigurationValueHandler>();

		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i] != IGNORE)
				result.add(createHandler(i));
		}

		return result;
	}
}
