package uk.ac.soton.ecs.experiments.util.checker;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class QStatJobTableModel implements TableModel {

	private String[] columnNames = { "ID", "Name", "State", "Required Time",
			"Elapsed Time" };
	private List<QStatJob> jobs;

	public QStatJobTableModel(List<QStatJob> jobs) {
		this.jobs = jobs;
	}

	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	public Class<?> getColumnClass(int arg0) {
		if (getRowCount() == 0)
			return String.class;

		return getValueAt(0, arg0).getClass();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int arg0) {
		return columnNames[arg0];
	}

	public int getRowCount() {
		return jobs.size();
	}

	public Object getValueAt(int arg0, int arg1) {
		QStatJob statJob = jobs.get(arg0);

		switch (arg1) {
		case 0:
			return statJob.getJobID();
		case 1:
			return statJob.getJobName();
		case 2:
			return statJob.getState();
		case 3:
			return statJob.getRequiredTime();
		case 4:
			return statJob.getElapsedTime();
		}

		throw new IllegalArgumentException();

	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public void removeTableModelListener(TableModelListener arg0) {
	}

	public void setValueAt(Object arg0, int arg1, int arg2) {
	}
}
