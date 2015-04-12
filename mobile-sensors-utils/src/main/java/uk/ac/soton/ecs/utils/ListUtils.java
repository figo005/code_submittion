package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

	public static <T extends Object> List<T> singletonList(T object) {
		List<T> result = new ArrayList<T>();
		result.add(object);
		return result;
	}
}
