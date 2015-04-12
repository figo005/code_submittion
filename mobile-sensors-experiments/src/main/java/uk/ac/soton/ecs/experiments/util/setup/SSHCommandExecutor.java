package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.connection.ChannelInputStream;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;

public class SSHCommandExecutor {
	private String hostName;
	private File privateKeyFile = new File(ConsoleUtil.getUserDirectory()
			+ "/.ssh/id_rsa");
	private String userName;
	private String command;
	private ChannelInputStream in;
	private SessionChannelClient sessionChannel;
	private SshClient client;

	public SSHCommandExecutor() {

	}

	public String execute() throws IOException {
		InputStream in = executeInteractive();

		byte buffer[] = new byte[255];
		int read;
		StringBuffer stringBuffer = new StringBuffer();

		while ((read = in.read(buffer)) > 0) {
			stringBuffer.append(new String(buffer, 0, read));
		}

		close();

		return stringBuffer.toString();
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public File getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(File privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public InputStream executeInteractive() throws IOException {
		client = new SshClient();
		client.connect(hostName, new IgnoreHostKeyVerification());

		PublicKeyAuthenticationClient keyAuthenticationClient = new PublicKeyAuthenticationClient();
		keyAuthenticationClient.setUsername(userName);
		SshPrivateKeyFile file = SshPrivateKeyFile.parse(privateKeyFile);

		keyAuthenticationClient.setKey(file.toPrivateKey(""));

		client.authenticate(keyAuthenticationClient);

		sessionChannel = client.openSessionChannel();

		sessionChannel.executeCommand(command);

		/**
		 * Reading from the session InputStream
		 */
		in = sessionChannel.getInputStream();

		return in;
	}

	public void close() throws IOException {
		sessionChannel.close();

		client.disconnect();
	}
}
