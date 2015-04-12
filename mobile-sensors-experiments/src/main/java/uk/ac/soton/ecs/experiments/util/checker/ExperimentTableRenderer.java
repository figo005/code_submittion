package uk.ac.soton.ecs.experiments.util.checker;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ExperimentTableRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (column == 1) {
			ExperimentState state = (ExperimentState) value;

			c.setBackground(getColor(state));
		}

		return c;
	}

	private Color getColor(ExperimentState state) {
		switch (state) {
		case ERROR:
			return Color.RED;
		case FINISHED:
			return Color.GREEN;
		case STARTED:
			return Color.WHITE;
		case STARTED_LONG_AGO:
			return Color.ORANGE;
		}

		throw new IllegalArgumentException();
	}

}
