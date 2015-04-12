package uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class InformationValueFrame {

	private InformationValueModel informationValueTableModel;
	private double informationValue[];
	private int currentLocationID;
	
	public void setInformationValue(double informationValue[]) {
		this.informationValue = informationValue;
	}
	
	public void setCurrentLocationID(int currentLocationID) {
		this.currentLocationID = currentLocationID;
	}


	public InformationValueFrame(double informationValue[],int currentLocationID) {
		JFrame frame = new JFrame();
		this.informationValue=informationValue;
		this.currentLocationID = currentLocationID;

		frame.getContentPane().setLayout(new BorderLayout());

		informationValueTableModel = new InformationValueModel();
		JTable table = new JTable(informationValueTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);


		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		frame.getContentPane().add(table.getTableHeader(),
				BorderLayout.PAGE_START);
		frame.getContentPane().add(table, BorderLayout.CENTER);

//		setState(currentState);
		informationValueTableModel.fireTableDataChanged();
		
		frame.setTitle("informationValueTable");
		frame.pack();
		frame.setVisible(true);

	}



	private class InformationValueModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private final String[] columnNames = {"Index","Value" };

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			DecimalFormat df = new DecimalFormat("0.00");
			
			if (columnIndex == 0) {
				return "Point"+rowIndex;
			}
			if (columnIndex == 1) {
				return df.format(informationValue[rowIndex]);
			}
//			if (columnIndex == 2) {
//				return stateValue;
//			}
//			if (columnIndex == 3) {
//				return stateValue
//						* Math.pow(planner.getGamma(), action.getDuration())
//						+ reward;
//			}

			return null;
		}

		public int getRowCount() {
			return informationValue.length;
		}

		public int getColumnCount() {
			return 2;
		}
	}
	
	public void updateMessage(double[] informationValue, int currentLocationID) {

		setInformationValue(informationValue);
		setCurrentLocationID(currentLocationID);
		informationValueTableModel.fireTableDataChanged();
		
	}

	public class CustomTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cell = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

//			MultiSensorAction action = actions.get(row);
//			MultiSensorAction nextAction = planner.nextAction(state);

//			if (action.equals(nextAction)) {
//				cell.setBackground(Color.green.brighter());
//			} else {
//				cell.setBackground(Color.white);
//			}
			if (row == currentLocationID) {
				cell.setBackground(Color.green);
			}
			else			
				cell.setBackground(Color.white);

			return cell;
		}
	}

	static class DoubleRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		NumberFormat formatter;

		public DoubleRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = new DecimalFormat("0.00");
			}

			setText((value == null) ? "" : formatter.format(value));
		}
	}

}
