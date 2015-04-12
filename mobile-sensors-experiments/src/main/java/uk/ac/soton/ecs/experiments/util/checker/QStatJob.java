package uk.ac.soton.ecs.experiments.util.checker;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class QStatJob {

	public static final String JOB_ID = "JobID";
	public static final String USERNAME = "Username";
	public static final String QUEUE = "Queue";
	public static final String JOB_NAME = "Jobname";
	public static final String SESSION_ID = "SessID";
	public static final String NODES = "NDS";
	public static final String REQD_CPUS = "TSK";
	public static final String REQD_MEMORY = "ReqdMemory";
	public static final String REQD_TIME = "ReqdTime";
	public static final String STATE = "S";
	public static final String ELAPSED_TIME = "ElapTime";
	public static final String NEWLINE = "\n";
	public static final String NOT_AVAILABLE = "--";
	public static final String[] allKeys = { JOB_ID, USERNAME, QUEUE, JOB_NAME,
			SESSION_ID, NODES, REQD_CPUS, REQD_MEMORY, REQD_TIME, STATE,
			ELAPSED_TIME };
	private String jobID;
	private String username;
	private String queue;
	private String jobName;
	private String sessionID;
	private String nodes;
	private String requiredCPUS;
	private String requiredMemory;
	private String requiredTime;
	private String state;
	private String elapsedTime;

	public QStatJob(String[] split) {
		this.jobID = split[0].substring(0, split[0].length() - 2);
		this.username = split[1];
		this.queue = split[2];
		this.jobName = split[3];
		this.sessionID = split[4];
		this.nodes = split[5];
		this.requiredCPUS = split[6];
		this.requiredMemory = split[7];
		this.requiredTime = split[8];
		this.state = split[9];
		this.elapsedTime = split[10];
	}

	public static String getJOB_ID() {
		return JOB_ID;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static String getQUEUE() {
		return QUEUE;
	}

	public static String getJOB_NAME() {
		return JOB_NAME;
	}

	public static String getSESSION_ID() {
		return SESSION_ID;
	}

	public static String getNODES() {
		return NODES;
	}

	public static String getREQD_CPUS() {
		return REQD_CPUS;
	}

	public static String getREQD_MEMORY() {
		return REQD_MEMORY;
	}

	public static String getREQD_TIME() {
		return REQD_TIME;
	}

	public static String getSTATE() {
		return STATE;
	}

	public static String getELAPSED_TIME() {
		return ELAPSED_TIME;
	}

	public static String getNEWLINE() {
		return NEWLINE;
	}

	public static String getNOT_AVAILABLE() {
		return NOT_AVAILABLE;
	}

	public static String[] getAllKeys() {
		return allKeys;
	}

	public String getJobID() {
		return jobID;
	}

	public String getUsername() {
		return username;
	}

	public String getQueue() {
		return queue;
	}

	public String getJobName() {
		return jobName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getNodes() {
		return nodes;
	}

	public String getRequiredCPUS() {
		return requiredCPUS;
	}

	public String getRequiredMemory() {
		return requiredMemory;
	}

	public String getRequiredTime() {
		return requiredTime;
	}

	public String getState() {
		return state;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	public long getShortJobID() {
		String id = getJobID().substring(0, getJobID().indexOf('.'));

		return Long.parseLong(id);
	}

}
