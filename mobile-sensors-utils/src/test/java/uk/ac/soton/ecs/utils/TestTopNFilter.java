package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

public class TestTopNFilter extends TestCase {

	private Random random = new Random();

	public void testSubListInvariant() {
		TopNFilter<String, Integer> top50Filter = new TopNFilter<String, Integer>(
				50);

		TopNFilter<String, Integer> top10Filter = new TopNFilter<String, Integer>(
				10);

		for (int i = 0; i < 100; i++) {
			int value = random.nextInt(50);
			int uniqueValue = random.nextInt(1000);

			top50Filter.add("" + value + uniqueValue, value);
			top10Filter.add("" + value + uniqueValue, value);
		}

		assertEquals(top10Filter.getKeys(), top50Filter.getKeys()
				.subList(0, 10));
	}

	public void testAddN() {
		List<String> keys = new ArrayList<String>();
		for (int i = 9; i >= 0; i--) {
			keys.add(String.valueOf(i));
		}

		TopNFilter<String, Integer> top10Filter = new TopNFilter<String, Integer>(
				10);

		for (String string : keys) {
			top10Filter.add(string, Integer.parseInt(string));
		}

		assertEquals(keys, top10Filter.getKeys());
	}
}
