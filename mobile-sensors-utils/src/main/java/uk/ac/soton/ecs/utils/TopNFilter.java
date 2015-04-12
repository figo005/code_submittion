package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.comparators.ComparableComparator;
import org.apache.commons.collections15.comparators.ReverseComparator;

public class TopNFilter<S1, S2 extends Comparable<S2>> implements Iterable<S1> {

	private Comparator<S2> comparator = new ReverseComparator<S2>(
			new ComparableComparator<S2>());
	private int size;
	private List<S1> keys;
	private List<S2> values;

	public TopNFilter(int size) {
		keys = new ArrayList<S1>(size + 1);
		values = new ArrayList<S2>(size + 1);

		this.size = size;
	}

	public void add(S1 key, S2 value) {
		int index = Collections.binarySearch(values, value, comparator);

		int insertionPoint;

		if (index < 0) {
			insertionPoint = -(index + 1);
		} else {
			// value exists, insert new value after the last index with the same
			// value to preserve sublist invariant
			insertionPoint = index;

			while (insertionPoint < values.size()
					&& values.get(insertionPoint).equals(value))
				insertionPoint++;
		}

		if (insertionPoint < size) {
			keys.add(insertionPoint, key);
			values.add(insertionPoint, value);

			if (keys.size() > size) {
				keys.remove(size);
				values.remove(size);
			}
		}
	}

	public Iterator<S1> iterator() {
		return keys.iterator();
	}

	public List<S1> getKeys() {
		return keys;
	}

	public static void main(String[] args) {
		TopNFilter<String, Integer> filter = new TopNFilter<String, Integer>(
				100);

		filter.add("4four", 4);
		filter.add("2", 2);
		filter.add("4quattro", 4);
		filter.add("8acht", 8);
		filter.add("4vier", 4);
		filter.add("6", 6);
		filter.add("1", 1);

		for (String string : filter) {
			System.out.println(string);
		}
	}
}
