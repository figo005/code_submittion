package uk.ac.soton.ecs.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtils {
	public static double[][] readFileToMatrix(File file) throws IOException {
		List<String> lines = org.apache.commons.io.FileUtils.readLines(file);

		removeCommentLines(lines);

		if (lines.isEmpty())
			return new double[0][0];

		int cols = new StringTokenizer(lines.get(0), " ").countTokens();
		int rows = lines.size();

		double[][] result = new double[rows][cols];

		for (int i = 0; i < rows; i++) {
			StringTokenizer st = new StringTokenizer(lines.get(i), " ");

			for (int j = 0; j < cols; j++) {
				result[i][j] = Double.parseDouble(st.nextToken());
			}
		}

		return result;
	}

	private static void removeCommentLines(List<String> lines) {
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			if (line.startsWith("%"))
				iterator.remove();
		}
	}
}
