package uk.ac.soton.ecs.experiments.util.checker;

import java.io.IOException;
import java.util.List;

import uk.ac.soton.ecs.experiments.util.setup.SSHCommandExecutor;

public class PBSQueue {

	private String hostname;
	private String username;

	public PBSQueue(String hostname, String username) {
		this.hostname = hostname;
		this.username = username;
	}

	public List<QStatJob> getQueue() throws IOException {
		SSHCommandExecutor executor = new SSHCommandExecutor();
		executor.setCommand("qstat -a");
		executor.setUserName(username);
		executor.setHostName(hostname);

		String result = executor.execute();

		PBSQstatParser qstatParser = new PBSQstatParser(result);
		return qstatParser.getAlljobs();
	}

	public void delete(long id) throws IOException {
		SSHCommandExecutor executor = new SSHCommandExecutor();
		executor.setCommand("qdel " + id);
		executor.setUserName(username);
		executor.setHostName(hostname);

		executor.execute();
	}

	public void deleteAll() throws IOException {
		List<QStatJob> queue = getQueue();
		for (QStatJob job : queue) {
			delete(job);
		}
	}

	public void delete(QStatJob job) throws IOException {
		delete(job.getShortJobID());

	}

	public static void main(String[] args) throws IOException {
		PBSQueue queue = new PBSQueue("iridis2.soton.ac.uk", "rs1f06");

		System.out.println("get queue");
		System.out.println(queue.getQueue());

		// queue.delete("1443856.blue1");

		System.out.println("now deleting all");
		queue.deleteAll();
		//
		// System.out.println(queue.getQueue());

	}

}
