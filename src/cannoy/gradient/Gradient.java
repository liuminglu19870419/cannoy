package cannoy.gradient;


public class Gradient {
	static public double[][] P;
	static public double[][] Q;
	static public double[][] M;
	static public double[][] Theta;
	static public double[][] result;

	public static void calcGradient(double[][] image) {
		P = new double[image.length][image[0].length];
		Q = new double[image.length][image[0].length];
		M = new double[image.length][image[0].length];
		Theta = new double[image.length][image[0].length];
		result = new double[image.length][image[0].length];
		for (int i = 0; i < image.length - 1; i++) {
			for (int j = 0; j < image[0].length - 1; j++) {
				P[i][j] = (image[i][j + 1] - image[i][j] + image[i + 1][j + 1] - image[i + 1][j]) / 2;
				Q[i][j] = (image[i][j] - image[i + 1][j] + image[i][j + 1] - image[i + 1][j + 1]) / 2;
			}
		}

		double scale = 180 / Math.PI;

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				M[i][j] = Math.sqrt(P[i][j] * P[i][j] + Q[i][j] * Q[i][j]);
				Theta[i][j] = Math.atan2(Q[i][j], P[i][j]);
				Theta[i][j] += 360;
			}
		}

		double min = 0.0001;
		double g1, g2, g3, g4;
		double dweight;
		double tmp1 = 0, tmp2 = 0;
		for (int i = 1; i < image.length - 1; i++) {
			for (int j = 1; j < image[0].length - 1; j++) {
				if (M[i][j] < min) {
					result[i][j] = 0;
				} else {
					if ((Theta[i][j] >= 90 && Theta[i][j] < 135)
							|| (Theta[i][j] >= 270 && Theta[i][j] < 315)) {
						g1 = M[i - 1][j - 1];
						g2 = M[i - 1][j];
						g3 = M[i + 1][j];
						g4 = M[i + 1][j + 1];
						dweight = Math.abs(P[i][j] / Q[i][j]);
						tmp1 = g1 * dweight + g2 * (1 - dweight);
						tmp2 = g4 * dweight + g3 * (1 - dweight);
					}
					if ((Theta[i][j] >= 135 && Theta[i][j] < 180)
							|| (Theta[i][j] >= 315 && Theta[i][j] < 360)) {
						g1 = M[i - 1][j - 1];
						g2 = M[i][j - 1];
						g3 = M[i][j + 1];
						g4 = M[i + 1][j + 1];
						dweight = Math.abs(Q[i][j] / P[i][j]);
						tmp1 = g2 * dweight + g1 * (1 - dweight);
						tmp2 = g4 * dweight + g3 * (1 - dweight);
					}
					if ((Theta[i][j] >= 45 && Theta[i][j] < 90)
							|| (Theta[i][j] >= 225 && Theta[i][j] < 270)) {
						g1 = M[i - 1][j];
						g2 = M[i - 1][j + 1];
						g3 = M[i + 1][j];
						g4 = M[i + 1][j - 1];
						dweight = Math.abs(P[i][j] / Q[i][j]);
						tmp1 = g2 * dweight + g1 * (1 - dweight);
						tmp2 = g3 * dweight + g4 * (1 - dweight);
					}
					if ((Theta[i][j] >= 0 && Theta[i][j] < 45)
							|| (Theta[i][j] >= 180 && Theta[i][j] < 225)) {
						g1 = M[i - 1][j + 1];
						g2 = M[i][j + 1];
						g3 = M[i + 1][j - 1];
						g4 = M[i][j - 1];
						dweight = Math.abs(Q[i][j] / P[i][j]);
						tmp1 = g1 * dweight + g2 * (1 - dweight);
						tmp2 = g3 * dweight + g4 * (1 - dweight);
					}
					if (M[i][j] >= tmp1 && M[i][j] >= tmp2) {
						result[i][j] = 128;
					} else {
						result[i][j] = 0;
					}
				}
			}
		} // for (int i = 1; i < image.length - 1; i++) {

		int nHlist[] = new int[512];

		for (int i = 0; i < nHlist.length; i++) {
			nHlist[i] = 0;
		}

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				if (result[i][j] == 128) {
					nHlist[(int) M[i][j]]++;
				}
			}
		}

		int nEdgeNumber = nHlist[0];
		int nMaxMag = 0;

		for (int i = 1; i < nHlist.length; i++) {
			if (nHlist[i] != 0) {
				nMaxMag = i;
			}
			nEdgeNumber += nHlist[i];
		}

		double dRatHigh = 0.65;
		double dRatLow = 0.5;
		double dThrHigh = 0;
		double dThrLow = 0;

		int nHighCount = (int) (dRatHigh * nEdgeNumber + 0.5);
		int count = 1;
		nEdgeNumber = nHlist[1];
		while ((count < nMaxMag) && (nEdgeNumber < nHighCount)) {
			count++;
			nEdgeNumber += nHlist[count];
		}

		dThrHigh = count;
		dThrLow = (dThrHigh * dRatLow) + 0.5;

		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (result[i][j] == 128 && M[i][j] > dThrHigh) {
					result[i][j] = 255;
					traceEdge(i, j, dThrLow);
				}
			}
		}

		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (result[i][j] != 255) {
					result[i][j] = 0;
				}
			}
		}
	}

	private static void traceEdge(int i, int j, double dThrLow) {
		if (i < 0 || j < 0 || i > M.length - 1 || j > M[0].length - 1) {
			return;
		}

		for (int x = j - 1 < 0 ? 0 : j - 1; x <= (j + 1 > M[0].length - 1 ? M[0].length - 1
				: j + 1); x++) {
			for (int y = i - 1 < 0 ? 0 : i - 1; y <= (i + 1 > M[0].length - 1 ? M[0].length
					: i + 1); y++) {
				if (x == j && y == i) {
					continue;
				}
				if (result[y][x] == 128 && M[y][x] > dThrLow) {
					result[y][x] = 255;
					traceEdge(y, x, dThrLow);
				}
			}

		}

	}
}
