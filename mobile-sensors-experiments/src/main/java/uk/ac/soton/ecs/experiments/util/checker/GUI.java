package uk.ac.soton.ecs.experiments.util.checker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import ch.fhnw.filecopier.CopyJob;
import ch.fhnw.filecopier.FileCopier;

public class GUI extends JFrame implements ActionListener {

	private static final String REFRESH = "refresh";
	private static final String COPY = "copy";
	private static final String SELECT_FINISHED = "select_finished";
	private static final String AUTO_REFRESH = "auto_refresh";
	private static final String DELETE = "delete";
	private ExperimentsRootDirectory root;
	private JTable table;
	private FileCopierDialog fileCopierDialog;
	private Timer timer = new Timer();

	public GUI(ExperimentsRootDirectory root) {
		setTitle("Experiment Status Checker");
		getContentPane().setLayout(new BorderLayout());

		this.root = root;

		JToolBar toolBar = new JToolBar("Still draggable");
		JButton button = makeNavigationButton(null, REFRESH,
				"Refresh the Table", "Refresh");
		JButton copy = makeNavigationButton(null, COPY,
				"Copy selected experiments to temporary director", "Copy");
		JButton delete = makeNavigationButton(null, DELETE,
				"Delete selected experiments", "Delete");

		JButton selectFinished = makeNavigationButton(null, SELECT_FINISHED,
				"Select finished experiments", "Select Finished");

		JToggleButton autoRefresh = new JToggleButton();
		autoRefresh.setActionCommand(AUTO_REFRESH);
		autoRefresh.setToolTipText("Auto Refresh every 5 sec.");
		autoRefresh.addActionListener(this);
		autoRefresh.setText("Auto Refresh");

		toolBar.add(button);
		toolBar.add(copy);
		toolBar.add(delete);
		toolBar.add(selectFinished);
		toolBar.add(autoRefresh);

		getContentPane().add(toolBar, BorderLayout.PAGE_START);

		table = new JTable();
		table.setAutoCreateRowSorter(true);
		table.setDefaultRenderer(ExperimentState.class,
				new ExperimentTableRenderer());

		JScrollPane pane = new JScrollPane(table);

		this.getContentPane().add(pane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 800));

		this.pack();

		initialise();
		refresh();

	}

	public void refresh() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				ProgressDialog progressDialog = new ProgressDialog(GUI.this,
						"Refreshing");

				System.out.println("scanning");
				Set<ExperimentDirectory> experimentDirectories = root
						.getExperimentDirectories();
				table.setModel(new ExperimentTableModel(experimentDirectories));

				System.out.println("scanning done");

				progressDialog.setVisible(false);
				return null;
			}
		};

		worker.execute();

	}

	protected JButton makeNavigationButton(String imageName,
			String actionCommand, String toolTipText, String altText) {
		// Look for the image.
		String imgLocation = "images/" + imageName + ".gif";
		URL imageURL = GUI.class.getResource(imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found
			button.setText(altText);
			System.err.println("Resource not found: " + imgLocation);
		}

		return button;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(REFRESH)) {
			refresh();
		}
		if (e.getActionCommand().equals(COPY)) {
			copy();
		}
		if (e.getActionCommand().equals(DELETE)) {
			delete();
		}
		if (e.getActionCommand().equals(SELECT_FINISHED)) {
			selectFinished();
		}
		if (e.getActionCommand().equals(AUTO_REFRESH)) {
			boolean autoRefresh = ((JToggleButton) e.getSource()).isSelected();
			setAutoRefresh(autoRefresh);
		}
	}

	private void delete() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				ProgressDialog progressDialog = new ProgressDialog(GUI.this,
						"Deleting");

				List<File> selectedExperimentDirectories = getSelectedExperimentDirectories();

				for (File file : selectedExperimentDirectories) {
					try {
						FileUtils.deleteDirectory(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				progressDialog.setVisible(false);

				return null;
			}
		};

		worker.execute();

		refresh();

	}

	private void setAutoRefresh(boolean autoRefresh) {
		if (autoRefresh) {
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					refresh();
				}
			}, 0, 5000);
		} else {
			timer.cancel();
		}

	}

	private void selectFinished() {
		ExperimentTableModel model = (ExperimentTableModel) table.getModel();

		table.clearSelection();

		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getExperiment(i).getState() == ExperimentState.FINISHED) {
				table.addRowSelectionInterval(i, i);
			}
		}

	}

	public void initialise() {
		fileCopierDialog = new FileCopierDialog(this);
	}

	private void copy() {

		File tempDirectory;
		try {
			tempDirectory = File.createTempFile("experiments", "");
		} catch (IOException e) {
			return;
		}
		tempDirectory.delete();
		tempDirectory.mkdir();

		final List<CopyJob> copyJobs = new ArrayList<CopyJob>();

		for (File selectedFile : getSelectedExperimentDirectories()) {
			ExperimentFileCopier experimentCopier = new ExperimentFileCopier(
					selectedFile, tempDirectory);

			copyJobs.addAll(experimentCopier.getCopyJobs());
		}

		final FileCopier fileCopier = new FileCopier();
		fileCopierDialog.setFileCopier(fileCopier);

		SwingWorker<Void, Void> copier = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() {
				try {
					fileCopierDialog.setVisible(true);
					fileCopier.copy(copyJobs.toArray(new CopyJob[] {}));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void done() {
				fileCopierDialog.setVisible(false);
			}
		};

		copier.execute();
	}

	private List<File> getSelectedExperimentDirectories() {
		int[] selectedRows = table.getSelectedRows();

		List<File> selected = new ArrayList<File>();

		for (int i = 0; i < selectedRows.length; i++) {
			File selectedFile = (File) table.getValueAt(selectedRows[i], 0);
			selected.add(selectedFile);
		}

		return selected;

	}
}
