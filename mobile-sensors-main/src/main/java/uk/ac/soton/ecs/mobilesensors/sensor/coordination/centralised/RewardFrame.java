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

import uk.ac.soton.ecs.mobilesensors.sensor.coordination.HealthyBelief;

public class RewardFrame {

	private RewardTableModel tableModel;
	private double observationValue;
	private int realHealth;
	private HealthyBelief healthyBelief;
	
	public double getObservationValue() {
		return observationValue;
	}

	public void setObservationValue(double observationValue) {
		this.observationValue = observationValue;
	}

	public HealthyBelief getHealthyLevel() {
		return healthyBelief;
	}

	public void setHealthyBelief(HealthyBelief healthyBelief) {
		this.healthyBelief = healthyBelief;
	}



	public RewardFrame(double observationValue, int realHealth) {
		JFrame frame = new JFrame();
		this.observationValue=observationValue;
//		this.healthyBelief=healthyBelief;
		this.realHealth=realHealth;


		frame.getContentPane().setLayout(new BorderLayout());

		tableModel = new RewardTableModel();
		JTable table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
//		table.getColumnModel().getColumn(2).setPreferredWidth(150);
//		table.getColumnModel().getColumn(3).setPreferredWidth(150);
//
//		table.getColumnModel().getColumn(1).setCellRenderer(
//				new DoubleRenderer());
//		table.getColumnModel().getColumn(2).setCellRenderer(
//				new DoubleRenderer());
//		table.getColumnModel().getColumn(3).setCellRenderer(
//				new DoubleRenderer());

		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		frame.getContentPane().add(table.getTableHeader(),
				BorderLayout.PAGE_START);
		frame.getContentPane().add(table, BorderLayout.CENTER);

//		setState(currentState);
		tableModel.fireTableDataChanged();
		
		frame.setTitle("ObservationValueTotoal and HealthyBelief");
		frame.pack();
		frame.setVisible(true);

	}

//	public void setState(S state) {
//		this.state = state;
//		this.actions = new ArrayList<MultiSensorAction>(transitionFunction
//				.getActions(state));
//
//		tableModel.fireTableDataChanged();
//	}

	private class RewardTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private final String[] columnNames = { "item", "Value"
				 };

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			DecimalFormat df = new DecimalFormat("0.00");
			
			if (rowIndex == 0) {
				if(columnIndex==0)				
					return "observationValueTotoal";
				if(columnIndex==1)
					return observationValue;
			}
			
			if (rowIndex == 1) {
				if(columnIndex==0)				
					return "realHealth";
				if(columnIndex==1)
					return 0.1*realHealth;
			}

//			if (rowIndex == 2) {
//				if(columnIndex==0)				
//					return "P(Healthy=1.0)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(10);
//			}
//			
//			if (rowIndex == 3) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.9)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(9);
//			}
//			if (rowIndex == 4) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.8)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(8);
//			}
//			if (rowIndex == 5) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.7)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(7);
//			}
//			if (rowIndex == 6) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.6)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(6);
//			}
//			if (rowIndex == 7) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.5)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(5);
//			}
//			if (rowIndex == 8) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.4)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(4);
//			}
//			if (rowIndex == 9) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.3)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(3);
//			}
//			if (rowIndex == 10) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.2)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(2);
//			}
//			if (rowIndex == 11) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.1)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(1);
//			}
//			if (rowIndex == 12) {
//				if(columnIndex==0)				
//					return "P(Healthy=0.0)";
//				if(columnIndex==1)
//					return healthyBelief.getProbability(0);
//			}
//
//			if (rowIndex == 13) {
//				if(columnIndex==0)				
//					return "ExpectHealthy";
//				if(columnIndex==1)
//					return 0.1*healthyBelief.getExpectHealthy();
//			}
			


			return null;
		}

		public int getRowCount() {
			return 2;
		}

		public int getColumnCount() {
			return 2;
		}
	}
	
	public void updateMessage(double observationValue, int realHealth, HealthyBelief healthyBelief) {

		
		setObservationValue(observationValue);
		setHealthyBelief(healthyBelief);
		this.realHealth=realHealth;
		tableModel.fireTableDataChanged();
		
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
			if (row == 0) {
				cell.setBackground(Color.white);
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
