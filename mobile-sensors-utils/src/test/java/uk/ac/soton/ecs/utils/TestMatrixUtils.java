package uk.ac.soton.ecs.utils;

import uk.ac.soton.ecs.utils.test.ExtendedTestCase;
import Jama.Matrix;

public class TestMatrixUtils extends ExtendedTestCase {
	private final Matrix K = new Matrix(new double[] { 1.055081255982868,
			1.135048907776010, 1.130690825773057, 1.278704749548056,
			1.135048907776010, 1.860854552594188, 1.410773856415864,
			1.951381879653658, 1.130690825773057, 1.410773856415864,
			2.179001713817390, 1.706000033741727, 1.278704749548056,
			1.951381879653658, 1.706000033741727, 2.096829293496743 }, 4);

	public void testSum() throws Exception {
		double[][] vals = { { 1, 2, 4 }, { 5, 3, 1 }, { 5, 6, 3 } };

		Matrix matrix = new Matrix(vals);

		assertEquals(new double[][] { { 11, 11, 8 } }, MatrixUtils.sum(matrix)
				.getArray());
	}

	public void testSum2() throws Exception {
		double[][] vals = { { 1, -2 }, { 5, 3 }, { 5, 6 } };

		Matrix matrix = new Matrix(vals);

		assertEquals(new double[][] { { 11, 7 } }, MatrixUtils.sum(matrix)
				.getArray());
	}

	public void testKronecker() throws Exception {
		double[][] aVals = { { 1, 2 }, { 4, 5 } };
		double[][] bVals = { { 3 }, { 4 }, { 5 } };

		Matrix result = MatrixUtils.kronecker(new Matrix(aVals), new Matrix(
				bVals));

		double[][] expectedResult = { { 3, 6 }, { 4, 8 }, { 5, 10 },
				{ 12, 15 }, { 16, 20 }, { 20, 25 } };

		assertEquals(expectedResult, result.getArray());
	}

	public void testLogDet() throws Exception {
		Matrix mat = new Matrix(new double[][] {
				{ 1.436299774935793, 1.216378465879193, 1.275528163178899,
						1.205292884281812, 0.890863954914659 },
				{ 1.216378465879193, 1.168179606193329, 0.870831319047711,
						1.071860813846701, 0.675062853754179 },
				{ 1.275528163178899, 0.870831319047711, 2.242943429083151,
						1.301006651819557, 1.460462927689341 },
				{ 1.205292884281812, 1.071860813846701, 1.301006651819557,
						1.213649492321072, 1.074347483681873 },
				{ 0.890863954914659, 0.675062853754179, 1.460462927689341,
						1.074347483681873, 1.439552507435190 } });

		assertEquals(-6.573102759697524, MatrixUtils.logDet(mat), 1e-6);
	}

	public void testCholUpdate() throws Exception {
		Matrix R = K.chol().getL();

		Matrix expected = K.getMatrix(1, 3, 1, 3).chol().getL();

		Matrix S = R.getMatrix(1, 3, 0, 0);
		Matrix U = R.getMatrix(1, 3, 1, 3);

		Matrix actual = MatrixUtils.choleskyUpdate(U, S);

		assertEquals(expected, actual, 1e-6);
	}

	public void testCholDowndate() throws Exception {
		Matrix R = K.chol().getL();

		Matrix expected = K.getMatrix(1, 3, 1, 3).chol().getL();

		Matrix actual = MatrixUtils.choleskyDowndate(R);

		assertEquals(expected, actual, 1e-6);
	}

	public void testAppend() throws Exception {
		Matrix existing = new Matrix(5, 5, 5);

		Matrix addition = new Matrix(2, 4, 3);

		try {
			MatrixUtils.append(existing, addition).print(10, 4);
			fail();
		} catch (IllegalArgumentException e) {
		}

		addition = new Matrix(2, 5, 3);

		Matrix actual = MatrixUtils.append(existing, addition);

		Matrix expected = new Matrix(new double[][] { { 5, 5, 5, 5, 5 },
				{ 5, 5, 5, 5, 5 }, { 5, 5, 5, 5, 5 }, { 5, 5, 5, 5, 5 },
				{ 5, 5, 5, 5, 5 }, { 3, 3, 3, 3, 3 }, { 3, 3, 3, 3, 3 } });

		assertEquals(expected, actual);
	}
}
