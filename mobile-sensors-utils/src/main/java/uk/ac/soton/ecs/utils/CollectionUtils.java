package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

public class CollectionUtils {
	public static <T, E> Map<T, Collection<E>> sort(Collection<E> collection,
			Transformer<E, T> sortBy) {
		Map<T, Collection<E>> result = new HashMap<T, Collection<E>>();

		for (E e : collection) {
			T sortKey = sortBy.transform(e);
			if (!result.containsKey(sortKey)) {
				result.put(sortKey, new ArrayList<E>());
			}

			result.get(sortKey).add(e);
		}

		return result;
	}
}
