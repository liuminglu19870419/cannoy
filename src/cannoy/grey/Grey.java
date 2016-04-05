package cannoy.grey;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Grey {
	public static double[][] toGrey(BufferedImage bufferedImage) {
		double[][] greyImage = new double[bufferedImage.getHeight()][bufferedImage
				.getWidth()];
		for (int i = 0; i < greyImage.length; i++) {
			for (int j = 0; j < greyImage[0].length; j++) {
				Color color = new Color(bufferedImage.getRGB(j, i));
				greyImage[i][j] = (color.getBlue() * 0.114 + color.getRed()
						* 0.299 + color.getGreen() * 0.587);
			}
		}
		return greyImage;
	}
}
