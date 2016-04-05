package cannoy.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cannoy.draw.Draw;
import cannoy.gaussian.Gaussian;
import cannoy.gradient.Gradient;
import cannoy.grey.Grey;

public class Main {
	public static void main(String[] args) throws IOException {
		File file = new File("4ran.png");
		BufferedImage bufferedImage = ImageIO.read(file);
		double[][] greyImage = Grey.toGrey(bufferedImage);
//		Draw.drawDouble("grey.jpg", greyImage);
		double[][] gaussianImage = Gaussian.gaussianFilter(greyImage);
//		Draw.drawDouble("gaussian.jpg", gaussianImage);
		Gradient.calcGradient(gaussianImage);
		Draw.drawDouble("result.jpg", Gradient.result);
	}
}
