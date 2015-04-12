package uk.ac.soton.ecs.experiments.util.checker;

import java.util.List;
import java.util.Vector;

public class PBSQstatParser {

	private String qstatoutput;
	private List<QStatJob> alljobs;

	/*
	 * Job ID- the job identifier assigned by PBS.Username- the job owner.Queue-
	 * The queue in which the job currently resides.Jobname- The job name given
	 * by the submitter.SessID- The session id (if the job is running).NDS- The
	 * number of nodes requested by the job.TSK- The number of cpus or tasks
	 * requested by the job.Req'd Memory- The amount of memory requested by the
	 * job.Req'd Time- Either the cpu time, if specified, or wall time requested
	 * by the job, (hh:mm).S- The job's current state.Elap Time- The amount of
	 * cpu time or wall time used by the job (hh:mm).
	 */

	public PBSQstatParser(String qstatoutput) {
		this.qstatoutput = qstatoutput;

		parse();
	}

	private void parse() {

		String[] outputarray = qstatoutput.split("\n");
		int beginningOfData = outputarray.length;

		// find separator line in qstat output
		for (int i = 0; i < outputarray.length; i++) {
			if (outputarray[i].startsWith("-")) {
				beginningOfData = i + 1; // index where data starts
				break;
			}
		}

		alljobs = new Vector<QStatJob>();

		for (int i = beginningOfData; i < outputarray.length; i++) {
			alljobs.add(new QStatJob(outputarray[i].split("\\s+")));
		}
	}

	public String getRawQstatOutput() {
		return qstatoutput;
	}

	public void setRawQstatOutput(String qstatoutput) {
		this.qstatoutput = qstatoutput;
	}

	public List<QStatJob> getAlljobs() {
		return alljobs;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		for (QStatJob job : alljobs) {
			buffer.append(job + "\n");
		}
		return buffer.toString();
	}
}
