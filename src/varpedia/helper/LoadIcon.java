package varpedia.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

public class LoadIcon {

	public static ImageView loadIcon(String iconName, int width, int height) {
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/"+iconName+".png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(height);
			imageView.fitWidthProperty().set(width);
			return imageView;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
