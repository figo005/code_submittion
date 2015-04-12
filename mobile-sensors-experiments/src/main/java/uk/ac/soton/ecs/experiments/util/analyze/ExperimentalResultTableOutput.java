package uk.ac.soton.ecs.experiments.util.analyze;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class ExperimentalResultTableOutput {

	public void output(Collection<ExperimentalResults> results,
			List<String> columnNames, PrintStream stream) {

		printHeader(columnNames, stream);

		for (ExperimentalResults experimentalResults : results) {
			print(experimentalResults, columnNames, stream);
		}
	}

	public void output(Collection<ExperimentalResults> results) {
		output(results, System.out);
	}

	public void output(Collection<ExperimentalResults> results,
			PrintStream stream) {

		ExperimentalResults first = results.iterator().next();

		printHeader(first.getColumnNames(), stream);

		for (ExperimentalResults experimentalResults : results) {
			print(experimentalResults, first.getColumnNames(), stream);
		}

	}

	private void print(ExperimentalResults experimentalResults,
			List<String> columnNames, PrintStream stream) {
		for (String string : columnNames) {
			stream.print(experimentalResults.getParameter(string) + " \t");
		}

		stream.println();
	}

	private void printHeader(List<String> columnNames) {
		printHeader(columnNames, System.out);
	}

	private void printHeader(List<String> columnNames, PrintStream stream) {
		stream.print("% ");

		for (String string : columnNames) {
			stream.print(string + " \t");
		}
		stream.println();
	}
}
