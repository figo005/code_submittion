package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperimentalResults implements Comparable<ExperimentalResults> {

	private int id;
	private List<String> columnNames = new ArrayList<String>();

	private Map<String, String> values = new HashMap<String, String>();
	private String name;

	public ExperimentalResults(int id, String name) {
		setParameter("id", "" + id);
		setParameter("name", "" + name);

		this.name = name;
		this.id = id;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	@Override
	public String toString() {
		return values.toString();
	}

	public void setParameter(String key, String value) {
		if (!columnNames.contains(key)) {
			columnNames.add(key);
		}

		values.put(key, value);
	}

	public int getId() {
		return id;
	}

	public String getParameter(String string) {
		return values.get(string);
	}

	public String getName() {
		return name;
	}

	public int compareTo(ExperimentalResults o) {
		return getName().compareTo(o.getName());
	}
}
