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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class BeliefFrame {

	private beliefModel beliefTableModel;
	

	private Map<Integer, double[]> riskBeliefs = new HashMap<Integer, double[]>();
	private Map<Integer, double[]> informationBeliefs = new HashMap<Integer, double[]>();
	
	private Map<Integer, Double> valuesFromBelief = new HashMap<Integer, Double>();
	
	private List<Integer> currentLocationIDs;
	

	
	public void setCurrentLocationID(List<Integer> currentLocationIDs) {
		this.currentLocationIDs = currentLocationIDs;
	}
	
	public void setRiskBeliefs(Map<Integer, double[]> riskBeliefs) {
		this.riskBeliefs = riskBeliefs;
	}
	
	public void setInformationBeliefs(Map<Integer, double[]> informationBeliefs) {
		this.informationBeliefs = informationBeliefs;
	}

	public void setValuesFromBelief(Map<Integer, Double> valuesFromBelief) {
		this.valuesFromBelief = valuesFromBelief;
	}

	public BeliefFrame(Map<Integer, double[]> riskBeliefs,Map<Integer, double[]> informationBeliefs, 
			 Map<Integer, Double> valuesFromBelief, List<Integer> currentLocationIDs) {
		JFrame frame = new JFrame();
		this.riskBeliefs=riskBeliefs;
		this.informationBeliefs=informationBeliefs;

		this.valuesFromBelief = valuesFromBelief;
		this.currentLocationIDs = currentLocationIDs;	
		
		frame.getContentPane().setLayout(new BorderLayout());

		beliefTableModel = new beliefModel();
		JTable table = new JTable(beliefTableModel);
		JScrollPane scrollpane = new JScrollPane();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.add(scrollpane,BorderLayout.CENTER);
//		scrollpane.getViewport().add( );
//		scrollpane.
//		createScrollPaneForTable(JTable aTable)
//		table.getColumnModel().getColumn(0).setPreferredWidth(50);
//		table.getColumnModel().getColumn(1).setPreferredWidth(50);
//		table.
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		frame.getContentPane().add(table.getTableHeader(),
				BorderLayout.PAGE_START);
		frame.getContentPane().add(table, BorderLayout.CENTER);
		frame.getContentPane().setSize(50,50);
//		frame.getContentPane().add(scrollpane, BorderLayout.CENTER);

//		setState(currentState);
		beliefTableModel.fireTableDataChanged();
		
		frame.setTitle("Belief_NextTimeStep");
		frame.pack();
		frame.setVisible(true);

	}



	private class beliefModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private final String[] columnNames = {"Index","0-safe","1-danger","2-very danger","I0","I1","I2","I3","I4","EValue"};

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
			if (columnIndex == 3) {
				return df.format(riskBeliefs.get(rowIndex)[2]);
//				return 0;
			}

			if (columnIndex == 4)
			{
				return df.format(informationBeliefs.get(rowIndex)[0]);
			}
			if (columnIndex == 5)
			{
				return df.format(informationBeliefs.get(rowIndex)[1]);
			}
			if (columnIndex == 6)
			{
				return df.format(informationBeliefs.get(rowIndex)[2]);
			}
			if (columnIndex == 7)
			{
				return df.format(informationBeliefs.get(rowIndex)[3]);
			}
			if (columnIndex == 8)
			{
				return df.format(informationBeliefs.get(rowIndex)[4]);
			}
			
			if (columnIndex == 9)
			{
				return valuesFromBelief.get(rowIndex).doubleValue();
//				return df.format(temp);
//				return df.format(0.021);
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
			return 10;
		}
	}
	
	public void updateMessage(Map<Integer, double[]> riskBeliefs, Map<Integer, double[]> informationBeliefs, 
			 Map<Integer, Double> valuesFromBelief, List<Integer> currentLocationID) {

		setRiskBeliefs(riskBeliefs);
		setInformationBeliefs(informationBeliefs);
		
		setValuesFromBelief(valuesFromBelief);
		setCurrentLocationID(currentLocationID);
		beliefTableModel.fireTableDataChanged();
		
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
			for(int ID:currentLocationIDs)
			{
				if (row == ID) {
					cell.setBackground(Color.green);
					return cell;
				}
				
			}
//			if (row == currentLocationID.) {
//				cell.setBackground(Color.green);
//			}
//			else			
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
