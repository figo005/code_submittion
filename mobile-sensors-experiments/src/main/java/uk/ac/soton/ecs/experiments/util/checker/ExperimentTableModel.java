package uk.ac.soton.ecs.experiments.util.checker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ExperimentTableModel implements TableModel {

	private ArrayList<ExperimentDirectory> experiments;

	private String columnNames[] = { "Directory", "State", "Start", "End",
			"Execution Time (ms.)" };

	public ExperimentTableModel(Set<ExperimentDirectory> experiments) {
		this.experiments = new ArrayList<ExperimentDirectory>(experiments);
	}

	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	public Class<?> getColumnClass(int columnIndex) {
		if (getRowCount() == 0)
			return String.class;

		return getValueAt(0, columnIndex).getClass();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getRowCount() {
		return experiments.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ExperimentDirectory experimentDirectory = experiments.get(rowIndex);

		SimpleDateFormat format = new SimpleDateFormat();

		switch (columnIndex) {
		case 0:
			return experimentDirectory.getDirectory();
		case 1:
			return experimentDirectory.getState();
		case 2:
			return format.format(experimentDirectory.getStartTime());
		case 3:
			if (experimentDirectory.getState() == ExperimentState.FINISHED)
				return format.format(experimentDirectory.getEndTime());
			return "";
		case 4:
			return experimentDirectory.getExecutionTime();
		default:
			throw new IllegalArgumentException();
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {

	}

	public ExperimentDirectory getExperiment(int i) {
		return experiments.get(i);
	}

}
