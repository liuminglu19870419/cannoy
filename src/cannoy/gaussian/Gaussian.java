package cannoy.gaussian;

public class Gaussian {
	static public double[][] get2DKernalData(int n, double sigma) {
		int size = 2 * n + 1;
		double sigma22 = 2 * sigma * sigma;
		double sigma22PI = (double) Math.PI * sigma22;
		double[][] kernalData = new double[size][size];
		int row = 0;
		double sum = 0;
		for (int i = -n; i <= n; i++) {
			int column = 0;
			for (int j = -n; j <= n; j++) {
				double xDistance = i * i;
				double yDistance = j * j;
				kernalData[row][column] = (double) Math
						.exp(-(xDistance + yDistance) / sigma22) / sigma22PI;
				sum += kernalData[row][column];
				column++;
			}
			row++;
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				kernalData[i][j] = kernalData[i][j] / sum;
			}
		}
		return kernalData;
	}

	public static double[][] gaussianFilter(double[][] greyImage) {
		double[][] gaussianImage = new double[greyImage.length][greyImage[0].length];
		
		int lenght = 3;
		double [][]gaussianFilter = get2DKernalData(lenght, 0.50);
		
		for (int i = lenght; i < greyImage.length - lenght; i++) {
			for (int j = lenght; j < greyImage[0].length - lenght; j++) {
				for (int m = 0; m < gaussianFilter.length; m++) {
					for (int n = 0; n < gaussianFilter[0].length; n++) {
						gaussianImage[i][j] += gaussianFilter[m][n]
								* greyImage[i + m - lenght][j + n - lenght];
					}
				}
			}
		}
		return gaussianImage;
	}
}
