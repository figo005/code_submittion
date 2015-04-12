package uk.ac.soton.ecs.experiments.util.analyze;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public abstract class MultiValuedParameterRetreiver extends
		AbstractExperimentParameterRetreiver {

	private ColumnName columnName;

	public MultiValuedParameterRetreiver(ColumnName columnName) {
		this.columnName = columnName;
	}

	@Override
	protected String getParameter(Connection connection,
			ExperimentalResults results) {

		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT "
					+ getColumnName()
					+ " FROM experiment_result WHERE experiment_set_id="
					+ results.getId());

			List<Double> values = new ArrayList<Double>();

			while (resultSet.next()) {
				values.add(resultSet.getDouble(getColumnName().toString()));
			}

			return "" + computeValue(values);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		throw new IllegalArgumentException();
	}

	protected abstract double computeValue(List<Double> values);

	public ColumnName getColumnName() {
		return columnName;
	}

	public String getParameterName() {
		return columnName.toString() + getParameterPostFix();
	}

	protected abstract String getParameterPostFix();
}
