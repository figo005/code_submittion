package uk.ac.soton.ecs.experiments.util.analyze;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class HTMLResultsPageOutputter {

	private String outputDirectory;

	public HTMLResultsPageOutputter(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void output(ExperimentResultSet resultSet) throws Exception {
		Collection<ExperimentalResults> experimentGroups = ExperimentResultCollector
				.getExperimentGroups(resultSet);

		List<GraphParameter> graphParameters = resultSet.getGraphParameters();

		File destinationDirectory = new File(outputDirectory);
		FileUtils.forceMkdir(destinationDirectory);

		new ExperimentalResultTableOutput().output(experimentGroups);

		Collection<File> images = new ArrayList<File>();

		for (GraphParameter parameter : graphParameters) {
			GraphCreator graphCreator = new GraphCreator(parameter.getTitle(),
					experimentGroups, parameter.getParamNames(), parameter
							.getYAxisName(), parameter.isYAxisLogarithmic());

			File pngFile = new File(destinationDirectory, parameter
					.getGetShortDescription());
			images.add(graphCreator.saveAsPNG(pngFile.getAbsolutePath(), 800,
					600));

			// graphCreator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		File htmlFile = new File(destinationDirectory, "index.html");

		PrintWriter writer = new PrintWriter(htmlFile);
		writer.write("<html>\n");
		writer.write("<head>\n");
		writer.write("<title>\n");
		writer.write("</title>\n");
		writer.write("</head>\n");
		writer.write("<body>\n");

		for (File image : images) {
			writer.write("<img src=\"" + image.getName() + "\" />\n");
			writer.write("<p />");
		}
		writer.write("</body>\n");
		writer.write("</html>\n");
		writer.flush();
		writer.close();
	}
}
