package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.soton.ecs.experiments.util.setup.BuildMobileSensorJarFileCommand;

public class LastBuiltPanel extends JPanel implements TextListener {

	private JLabel lastBuiltDateLabel;

	public LastBuiltPanel(final TextFieldInput mobileSensorsProjectDir) {
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Last built"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));

		setLayout(new FlowLayout());
		lastBuiltDateLabel = new JLabel("");
		add(lastBuiltDateLabel);

		mobileSensorsProjectDir.addTextListener(this);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				updateLastBuiltLabel(mobileSensorsProjectDir.getText());
			}
		}, 0, 1000);

	}

	public void textValueChanged(TextEvent e) {
		String projectDir = ((TextField) e.getSource()).getText();
		updateLastBuiltLabel(projectDir);
	}

	public void updateLastBuiltLabel(String projectDir) {
		File jarFile = BuildMobileSensorJarFileCommand.getJarFile(new File(
				projectDir),
				BuildMobileSensorJarFileCommand.DEFAULT_SOURCE_JAR_NAME);

		if (jarFile.exists()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

			Date lastModified = new Date(jarFile.lastModified());

			Date now = new Date();
			long[] diff = timeDiff(lastModified, now);

			String text = String.format(
					"%s    %d day(s), %d hour(s), %d minute(s) %s second(s) ago\n",
					simpleDateFormat.format(lastModified), diff[0], diff[1],
					diff[2], diff[3]);

			lastBuiltDateLabel.setText(text);
		} else {
			lastBuiltDateLabel.setText("Jar file does not exist");
		}
	}

	public static long[] timeDiff(Date d1, Date d2) {
		long[] result = new long[5];
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.setTime(d1);
		long t1 = cal.getTimeInMillis();
		cal.setTime(d2);
		long diff = Math.abs(cal.getTimeInMillis() - t1);
		final int ONE_DAY = 1000 * 60 * 60 * 24;
		final int ONE_HOUR = ONE_DAY / 24;
		final int ONE_MINUTE = ONE_HOUR / 60;
		final int ONE_SECOND = ONE_MINUTE / 60;

		long d = diff / ONE_DAY;
		diff %= ONE_DAY;
		long h = diff / ONE_HOUR;
		diff %= ONE_HOUR;
		long m = diff / ONE_MINUTE;
		diff %= ONE_MINUTE;
		long s = diff / ONE_SECOND;
		long ms = diff % ONE_SECOND;
		result[0] = d;
		result[1] = h;
		result[2] = m;
		result[3] = s;
		result[4] = ms;

		return result;
	}
}
