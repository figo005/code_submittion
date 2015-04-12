package uk.ac.soton.ecs.experiments.util.analyze;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public class MatlabTableOutputter {

	private String outputDirectory;

	public MatlabTableOutputter(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public final String averagePostfix = "_avg";
	public final String stdErrOfMeanPostfix = "_std_err_of_mean";

	public void output(Collection<ExperimentalResults> results,
			TableParameter tableParameter) throws IOException {
		List<String> avgColumnNames = new ArrayList<String>();
		List<String> seomColumnNames = new ArrayList<String>();

		for (ColumnName columnName : tableParameter.getColumnNames()) {
			avgColumnNames.add(columnName + averagePostfix);
			seomColumnNames.add(columnName + stdErrOfMeanPostfix);
		}

		// printLegend(results, tableParameter.getTitle());

		ExperimentalResultTableOutput tableOutputter = new ExperimentalResultTableOutput();

		PrintStream printStream = new PrintStream(new File(outputDirectory,
				tableParameter.getTitle() + averagePostfix));
		tableOutputter.output(results, avgColumnNames, printStream);
		printStream.flush();
		printStream.close();

		System.out.println(new File(outputDirectory, tableParameter.getTitle()
				+ stdErrOfMeanPostfix));

		printStream = new PrintStream(new File(outputDirectory, tableParameter
				.getTitle()
				+ stdErrOfMeanPostfix));
		tableOutputter.output(results, seomColumnNames, printStream);
		printStream.flush();
		printStream.close();
	}

	private void printLegend(Collection<ExperimentalResults> results,
			String title) throws IOException {
		StringBuffer buffer = new StringBuffer();

		buffer.append("legend(");

		for (ExperimentalResults result : results) {
			buffer.append("'" + result.getName() + "',");
		}

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(");\n");

		FileUtils.writeStringToFile(new File(outputDirectory + '/' + title),
				buffer.toString());
	}

	public void output(ExperimentResultSet resultSet) throws Exception {
		Collection<ExperimentalResults> experimentGroups = ExperimentResultCollector
				.getExperimentGroups(resultSet);

		List<TableParameter> tableParameters = resultSet.getTableParameters();

		for (TableParameter tableParameter : tableParameters) {
			output(experimentGroups, tableParameter);
		}
	}

}
