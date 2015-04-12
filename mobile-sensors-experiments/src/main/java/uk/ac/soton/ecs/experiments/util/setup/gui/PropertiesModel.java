package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.experiments.util.ConfigurationGenerator;

public class PropertiesModel implements TableModel {

	private List<String> parameterNames = new ArrayList<String>();
	private Configuration properties;
	private String[] columnNames = { "Property", "Value" };
	private String propertyPrefix;

	public PropertiesModel(Set<String> parameterNames,
			Configuration properties, String propertyPrefix) {
		this.parameterNames = new ArrayList<String>(parameterNames);

		Validate.notNull(properties);

		this.properties = properties;
		this.propertyPrefix = propertyPrefix;
	}

	public PropertiesModel() {
		this(new HashSet<String>(), new PropertiesConfiguration(), "");
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
		return parameterNames.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return parameterNames.get(rowIndex);
		}
		if (columnIndex == 1) {
			String value = properties
					.getString(prefixPropertyKey(parameterNames.get(rowIndex)));

			if (value == null)
				return "";
			return value;
		}

		throw new IllegalArgumentException();
	}

	private String prefixPropertyKey(String string) {
		return propertyPrefix + "." + string;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		String parameterName = (String) getValueAt(rowIndex, 0);

		List<String> parameterValues = ConfigurationGenerator
				.parseParameterValue((String) value);

		properties.setProperty(prefixPropertyKey(parameterName), StringUtils
				.join(parameterValues, " "));
	}

	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

}
