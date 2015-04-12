package uk.ac.soton.ecs.experiments.util.checker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

public class QStatFrame extends JFrame implements ActionListener {

	private static final String REFRESH = "refresh";
	private static final String DELETE_ALL = "delete_all";
	private List<QStatJob> jobs = new ArrayList<QStatJob>();
	private JTable table;

	public QStatFrame() {

		this.getContentPane().setLayout(new BorderLayout());

		table = new JTable(new QStatJobTableModel(jobs));
		JScrollPane scrollPane = new JScrollPane(table);

		getContentPane().add(getToolbar(), BorderLayout.PAGE_START);

		getContentPane().add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(800, 800));
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		refreshTable();
	}

	private Component getToolbar() {
		JToolBar toolBar = new JToolBar();

		JButton button = new JButton("Refresh");
		button.setActionCommand(REFRESH);
		button.addActionListener(this);

		toolBar.add(button);

		JButton deleteAll = new JButton("Delete All");
		deleteAll.setActionCommand(DELETE_ALL);
		deleteAll.addActionListener(this);
		toolBar.add(deleteAll);

		return toolBar;
	}

	private void refreshTable() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				PBSQueue queue = getQueue();

				ProgressDialog progressDialog = new ProgressDialog(
						QStatFrame.this, "Refreshing");

				List<QStatJob> list = queue.getQueue();

				table.setModel(new QStatJobTableModel(list));

				progressDialog.setVisible(false);

				return null;
			}
		};

		worker.execute();
	}

	protected PBSQueue getQueue() {
		return new PBSQueue("iridis2.soton.ac.uk", "rs1f06");
	}

	public static void main(String[] args) {
		QStatFrame statFrame = new QStatFrame();
		statFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(REFRESH)) {
			refreshTable();
		}
		if (e.getActionCommand().equals(DELETE_ALL)) {
			try {
				getQueue().deleteAll();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			refreshTable();
		}
	}
}
