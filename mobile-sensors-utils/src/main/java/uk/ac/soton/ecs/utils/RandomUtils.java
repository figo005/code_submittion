package uk.ac.soton.ecs.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;

public class RandomUtils {
	public static <E> E getRandomElement(Collection<E> elements) {
		int index = new Random().nextInt(elements.size());
		int currentElement = 0;

		for (E e : elements) {
			if (index == currentElement)
				return e;
			currentElement++;
		}

		throw new IllegalArgumentException();
	}

	public static <E> E getRandomElement(List<E> elements) {
		int index = new Random().nextInt(elements.size());

		return elements.get(index);
	}

	/**
	 * Selects a random index proportional to the (unnormalised) probabilities
	 * of the input vector
	 * 
	 * @param probabilities
	 * @return
	 */
	public static int selectUniformRandom(double[] probabilities) {
		probabilities = ArrayUtils.normalize(probabilities);

		double[] cumulative = ArrayUtils.cumulative(probabilities);
		Validate
				.isTrue(Math.abs(cumulative[probabilities.length - 1]) - 1 < 1e-8);

		int index = Arrays.binarySearch(cumulative, Math.random());

		index = index < 0 ? -index - 1 : index;

		Validate.isTrue(index >= 0);
		Validate.isTrue(index < probabilities.length);

		return index;
	}
}
