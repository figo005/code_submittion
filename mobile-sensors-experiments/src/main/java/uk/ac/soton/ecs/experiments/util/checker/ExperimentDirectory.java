package uk.ac.soton.ecs.experiments.util.checker;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ExperimentDirectory {

	private static final long LONG_TIME = 1000 * 3600 * 24;
	private File directory;
	private Long startTime;
	private Long endTime;

	public ExperimentDirectory(File directory) {
		this.directory = directory;
	}

	public ExperimentState getState() {
		if (new File(directory, "error.txt").exists()) {
			return ExperimentState.ERROR;
		}
		if (new File(directory, "endtime.txt").exists()) {
			return ExperimentState.FINISHED;
		}

		long diff = new Date().getTime() - getStartTime();

		if (diff > LONG_TIME) {
			return ExperimentState.STARTED_LONG_AGO;
		}

		return ExperimentState.STARTED;
	}

	public static boolean isExperimentDirectory(File directory) {
		return new File(directory, "starttime.txt").exists();
	}

	public File getDirectory() {
		return directory;
	}

	public Long getExecutionTime() {
		if (getState() != ExperimentState.FINISHED)
			return 0l;

		return getEndTime() - getStartTime();
	}

	public Long getEndTime() {
		if (endTime == null) {
			endTime = readNumber("endtime.txt");
		}

		return endTime;
	}

	public Long getStartTime() {
		if (startTime == null) {
			startTime = readNumber("starttime.txt");
		}

		return startTime;
	}

	private Long readNumber(String fileName) {
		try {
			List<String> readLines = FileUtils.readLines(new File(directory,
					fileName));
			return Long.parseLong(readLines.get(0));
		} catch (IOException e) {
		}

		return -1l;
	}

}
