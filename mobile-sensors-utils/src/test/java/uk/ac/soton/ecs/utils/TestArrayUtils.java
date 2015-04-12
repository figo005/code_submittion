package uk.ac.soton.ecs.utils;

import java.util.Arrays;

import uk.ac.soton.ecs.utils.test.ExtendedTestCase;

public class TestArrayUtils extends ExtendedTestCase {
	public void testCombine() throws Exception {
		double[] addition = { 10, 11, 12 };

		double[][] combinations = null;
		double[][] expectedResult = { { 10 }, { 11 }, { 12 } };

		double[][] result = ArrayUtils.combine(combinations, addition);

		assertEquals(expectedResult, result);

		combinations = new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

		expectedResult = new double[][] { { 1, 2, 3, 10 }, { 1, 2, 3, 11 },
				{ 1, 2, 3, 12 }, { 4, 5, 6, 10 }, { 4, 5, 6, 11 },
				{ 4, 5, 6, 12 }, { 7, 8, 9, 10 }, { 7, 8, 9, 11 },
				{ 7, 8, 9, 12 } };

		result = ArrayUtils.combine(combinations, addition);

		assertEquals(expectedResult, result);

		combinations = new double[][] { { 1 }, { 2 }, { 3 } };
		addition = new double[] { 4, 5 };

		expectedResult = new double[][] { { 1, 4 }, { 1, 5 }, { 2, 4 },
				{ 2, 5 }, { 3, 4 }, { 3, 5 } };

		result = ArrayUtils.combine(combinations, addition);

		assertEquals(expectedResult, result);
	}

	public void testAllCombinations() throws Exception {
		double[][] values = { { 1, 2, 3 }, { 4, 5 }, { 3 } };
		double[][] expectedResult = { { 1, 4, 3 }, { 1, 5, 3 }, { 2, 4, 3 },
				{ 2, 5, 3 }, { 3, 4, 3 }, { 3, 5, 3 } };

		double[][] result = ArrayUtils.allCombinations(values);

		assertEquals(expectedResult, result);
	}

	public void testAllCombinationsString() throws Exception {
		String[][] values = { { "1", "2", "3" }, { "4", "5" }, { "3" } };
		String[][] expectedResult = { { "1", "4", "3" }, { "1", "5", "3" },
				{ "2", "4", "3" }, { "2", "5", "3" }, { "3", "4", "3" },
				{ "3", "5", "3" } };

		String[][] result = ArrayUtils.allCombinations(values, String.class);

		assertTrue(Arrays.deepEquals(expectedResult, result));
	}
}
