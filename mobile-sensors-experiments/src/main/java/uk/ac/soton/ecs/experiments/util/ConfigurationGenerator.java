package uk.ac.soton.ecs.experiments.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Reads a template file from disk, parameterised with '$' style parameters.
 * Reads property file from disk, and replaces all parameters with the values
 * from the property file. Configuration files are written to the specified
 * output directory.
 * 
 * See src/test/resources for an example template and test file
 * 
 * @author rs06r
 * 
 */
public class ConfigurationGenerator {

	private static Map<String, List<String>> parameterValues;

	public static void main(String[] args) throws IOException,
			ConfigurationException {
		if (args.length != 3) {
			System.out.println("Usage: template outputDirectory config");
			System.exit(1);
		}

		File templateFile = new File(args[0]);
		File outputDirectory = new File(args[1]);
		File configFile = new File(args[2]);

		run(templateFile, outputDirectory, configFile);

	}

	public static void run(File templateFile, File outputDirectory,
			File configFile) throws ConfigurationException, IOException {
		run(templateFile, outputDirectory, configFile, "");
	}

	public static void run(File templateFile, File outputDirectory,
			File configFile, String extension) throws IOException,
			ConfigurationException {
		// read the configuration file with the replacements
		PropertiesConfiguration configuration = new PropertiesConfiguration(
				configFile);

		run(templateFile, outputDirectory, configuration, extension);
	}

	public static void run(File templateFile, File outputDirectory,
			Configuration configuration, String extension) throws IOException,
			ConfigurationException {
		parameterValues = new HashMap<String, List<String>>();

		// read the template file
		String template = FileUtils.readFileToString(templateFile);

		FileUtils.forceMkdir(outputDirectory);

		List<String> result = new ArrayList<String>();
		result.add(template);

		for (String parameter : getParameters(template)) {
			List<String> workingList = new ArrayList<String>();

			for (String currentTemplate : result) {
				workingList.addAll(replace(currentTemplate, parameter,
						configuration));
			}

			result = workingList;
		}

		int current = 0;
		for (String content : result) {
			FileUtils.writeStringToFile(new File(outputDirectory, "result"
					+ StringUtils.leftPad("" + current, 4, '0') + "."
					+ extension), content);
			current++;
		}

		System.out.println("Wrote " + result.size() + " configuration files");

		writeMetaData(outputDirectory);
	}

	private static void writeMetaData(File outputDirectory) throws IOException {
		String content = "";

		for (String parameter : parameterValues.keySet()) {
			content += parameter + " = "
					+ StringUtils.join(parameterValues.get(parameter), ", ")
					+ "\n";
		}

		FileUtils.writeStringToFile(new File(outputDirectory, "metadata"),
				content);

	}

	public static Set<String> getParameters(String template) {
		Pattern pattern = Pattern.compile("\\$[A-Za-z_][0-9A-Za-z_]*",
				Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(template);
		Set<String> result = new HashSet<String>();

		while (matcher.find()) {
			result.add(matcher.group());
		}

		return result;
	}

	/**
	 * Returns the parameter names without the dollar sign
	 * 
	 * @param template
	 * @return
	 */
	public static Set<String> getParameterNames(String template) {
		Set<String> parameters = getParameters(template);

		Set<String> result = new HashSet<String>();

		for (String parameter : parameters) {
			result.add(parameter.substring(1));
		}

		return result;
	}

	public static List<String> replace(String template, String parameter,
			Configuration configuration) {
		String parameterName = parameter.replaceFirst("\\$", "");

		String value = configuration.getString(parameterName);

		if (value == null)
			throw new IllegalArgumentException("Parameter " + parameterName
					+ " not found in replacement file");

		List<String> replacements = parseParameterValue(value);

		parameterValues.put(parameterName, replacements);

		List<String> result = new ArrayList<String>();

		// System.out.println(parameter + " = " + value + ": "
		// + replacements.size());

		for (String replacement : replacements) {
			result.add(replace(template, parameter, replacement));
		}
		return result;
	}

	public static List<String> parseParameterValue(String value) {
		List<String> replacements;

		if (value.startsWith("[ls]"))
			replacements = listDirectory(value);
		else if (value.startsWith("[range]"))
			replacements = range(value);
		else if (value.startsWith("[randInt]"))
			replacements = randomIntegers(value);
		else
			replacements = Arrays.asList(value.split(" "));

		return replacements;
	}

	private static List<String> randomIntegers(String value) {
		String[] components = value.split(" ");
		String countString = components[1];
		int count = Integer.parseInt(countString);

		List<String> result = new ArrayList<String>();

		Random random = new Random();
		for (int i = 0; i < count; i++) {
			result.add(String.valueOf(random.nextInt()));
		}

		return result;
	}

	public static String replace(String template, String parameter, String value) {
		return template.replaceAll(Matcher.quoteReplacement(parameter), value);
	}

	private static List<String> range(String value) {
		String[] components = value.split(" ");
		String fromString = components[1];
		String toString = components[2];
		String countString = components[3];

		double from = Double.parseDouble(fromString);
		double to = Double.parseDouble(toString);
		int count = Integer.parseInt(countString);

		List<String> result = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			double current = (from + i * (to - from) / (count - 1));
			result.add("" + current);
			// current += increment;
		}

		return result;
	}

	private static List<String> listDirectory(String value) {
		String[] components = value.split(" ");
		String directory = components[1];
		final String extension = components[2];

		File datasetDirectory = new File(directory);

		File[] datasets = datasetDirectory
				.listFiles(new ExtensionFileNameFilter(extension));

		List<String> filenames = new ArrayList<String>();
		for (File dataset : datasets) {
			filenames.add(dataset.toString());
		}
		return filenames;
	}
}
