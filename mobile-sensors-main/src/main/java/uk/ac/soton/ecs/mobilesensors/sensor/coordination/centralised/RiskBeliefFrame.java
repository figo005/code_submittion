package uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class RiskBeliefFrame {

	private riskBeliefModel riskBeliefTableModel;
	

	private Map<Integer, double[]> riskBeliefs = new HashMap<Integer, double[]>();
	
	private int currentLocationID;
	

	
	public void setCurrentLocationID(int currentLocationID) {
		this.currentLocationID = currentLocationID;
	}
	
	public void setRiskBeliefs(Map<Integer, double[]> riskBeliefs) {
		this.riskBeliefs = riskBeliefs;
	}


	public RiskBeliefFrame(Map<Integer, double[]> riskBeliefs,int currentLocationID) {
		JFrame frame = new JFrame();
		this.riskBeliefs=riskBeliefs;
		this.currentLocationID = currentLocationID;

		frame.getContentPane().setLayout(new BorderLayout());

		riskBeliefTableModel = new riskBeliefModel();
		JTable table = new JTable(riskBeliefTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);


		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		frame.getContentPane().add(table.getTableHeader(),
				BorderLayout.PAGE_START);
		frame.getContentPane().add(table, BorderLayout.CENTER);

//		setState(currentState);
		riskBeliefTableModel.fireTableDataChanged();
		
		frame.setTitle("riskBeliefTable");
		frame.pack();
		frame.setVisible(true);

	}



	private class riskBeliefModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private final String[] columnNames = {"Index","0-safte","1-danger"};

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
				return df.format(riskBeliefs.get(rowIndex)[0]);
			}
			if (columnIndex == 2) {
				return df.format(riskBeliefs.get(rowIndex)[1]);
			}
//			if (columnIndex == 3) {
//				return stateValue
//						* Math.pow(planner.getGamma(), action.getDuration())
//						+ reward;
//			}

			return null;
		}

		public int getRowCount() {
			return riskBeliefs.size();
		}

		public int getColumnCount() {
			return 2;
		}
	}
	
	public void updateMessage(Map<Integer, double[]> riskBeliefs, int currentLocationID) {

		setRiskBeliefs(riskBeliefs);

		riskBeliefTableModel.fireTableDataChanged();
		
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
