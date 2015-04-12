package uk.ac.soton.ecs.experiments.util.analyze;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GraphCreator extends ApplicationFrame {

	private JFreeChart chart;

	private CategoryDataset createDataset(
			Collection<ExperimentalResults> results, List<String> parameters) {

		// column keys
		List<String> experimentNames = new ArrayList<String>();

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (String parameter : parameters) {

			for (ExperimentalResults result : results) {
				String experimentName = result.getName();
				experimentNames.add(experimentName);

				double value = Double.parseDouble(result
						.getParameter(parameter));

				if (Double.isNaN(value))
					continue;

				experimentName = experimentName.replaceAll("_", " ");

				dataset.addValue(value, parameter, experimentName);
			}
		}
		return dataset;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * @param title
	 * @param logarithmic
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(final CategoryDataset dataset,
			String yAxisTitle, String title, boolean logarithmic) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(title, // chart
				// title
				"Simulation Name", // domain axis label
				yAxisTitle, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// // disable bar outlines...
		// final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		// renderer.setDrawBarOutline(false);

		if (logarithmic) {
			LogarithmicAxis axis = new LogarithmicAxis(yAxisTitle);
			axis.setAllowNegativesFlag(true);

			plot.setRangeAxis(axis);
		}

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setMaximumCategoryLabelLines(4);

		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	/**
	 * Creates a new demo instance.
	 * 
	 * @param title
	 *            the frame title.
	 * @param logarithmic
	 */
	public GraphCreator(final String title,
			Collection<ExperimentalResults> results, List<String> paramNames,
			String yAxisName, boolean logarithmic) {
		super(title);

		final CategoryDataset dataset = createDataset(results, paramNames);
		chart = createChart(dataset, yAxisName, title, logarithmic);
	}

	public void showOnScreen() {
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(700, 400));
		setContentPane(chartPanel);

		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	public File saveAsPNG(String filename) throws IOException {
		return saveAsPNG(filename, getWidth(), getHeight());
	}

	public File saveAsPNG(String filename, int width, int height)
			throws IOException {
		File file = new File(filename + ".png");
		ChartUtilities.saveChartAsPNG(file, chart, width, height);
		System.out.println("Saved as " + file.getAbsolutePath());
		return file;
	}

}
