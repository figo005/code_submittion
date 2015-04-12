package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.Arrays;
import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public class TableParameter {

	private List<ColumnName> columnNames;
	private String title;

	public TableParameter(String title, ColumnName... columnNames) {
		this.columnNames = Arrays.asList(columnNames);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public List<ColumnName> getColumnNames() {
		return columnNames;
	}

}
