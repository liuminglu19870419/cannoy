package cannoy.draw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Draw {
	public static void drawDouble(String filename, double[][] image) throws IOException {
		BufferedImage bufferedImage = new BufferedImage(image[0].length,
				image.length, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int rgb = (int) image[i][j];
				Color color = new Color(rgb, rgb, rgb);
				bufferedImage.setRGB(j, i, color.getRGB());
			}
		}
		File file = new File(filename);
		ImageIO.write(bufferedImage, "jpg", file);
	}
}
