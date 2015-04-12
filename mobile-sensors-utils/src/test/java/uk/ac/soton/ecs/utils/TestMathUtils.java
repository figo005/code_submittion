package uk.ac.soton.ecs.utils;

import junit.framework.TestCase;

public class TestMathUtils extends TestCase {

	public void testMvnPDF() throws Exception {
		double[] x = { 4, 3 };
		double[] mu = { 5, 5 };
		double[][] sigma = { { 2, 0 }, { 0, 2 } };

		assertEquals(0.022799327319919, MathUtils.mvnPDF(x, mu, sigma), 1e-7);

		sigma = new double[][] { { 2, 1 }, { 1, 2 } };

		assertEquals(0.033803760991573, MathUtils.mvnPDF(x, mu, sigma), 1e-7);
	}
}
