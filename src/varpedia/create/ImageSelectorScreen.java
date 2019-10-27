package varpedia.create;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

/**
 * The screen where the user selects the images they would like in their creation
 *
 */
public class ImageSelectorScreen{
	
	private BorderPane screen;
	/**
	 * The title of this screen - "Select images"
	 */
	private Label title;
	
	/**
	 * Pane containing the rows of images. 
	 */
	private VBox rowsPane;
	
	/**
	 * Error text when user selects no images
	 */
	private Label errorText;
	
	/**
	 * The list of boxes of images on the screen
	 */
	private List<ImageDisplayBox> listOfImages;
	
	/**
	 * The boxes of images the user has selected
	 */
	private List<ImageDisplayBox> listOfSelectedImages;
	
	/**
	 * The number of images the user has selected
	 */
	private int amountSelected;
	
	/**
	 * Linux scripts
	 */
	private Scripts scripts;

	/**
	 * Constructor.
	 */
	public ImageSelectorScreen() {
		screen = new BorderPane();
		listOfImages = new ArrayList<ImageDisplayBox>();
		listOfSelectedImages = new ArrayList<ImageDisplayBox>();
		scripts = new Scripts();
		rowsPane = new VBox();
		title = new Label("Select Images");
		
		Styling.yellowBG(screen);
	}
	
	public BorderPane getScreen() {
		return screen;
	}
	
	/**
	 * Images have completed downloading so set up this screen
	 */
	public void setUp() {
		//Clear lists for when they press back then go next to this screen
		listOfImages.clear();
		listOfSelectedImages.clear();
		setUpPane();
		setUpTitle();
		getImageFiles();
		setUpImages();
		setUpError();
		
		screen.setCenter(rowsPane);
	}
	
	/**
	 * Set up the pane for the rows of images
	 */
	private void setUpPane() {
		rowsPane.setSpacing(20);
		rowsPane.setAlignment(Pos.CENTER);
		rowsPane.setPadding(new Insets(10,20,10,20));
	}
	
	/**
	 * Set up the title on the top of screen
	 */
	private void setUpTitle() {
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font(Font.getDefault().getName(),25));
		BorderPane.setAlignment(title, Pos.TOP_CENTER);
		Styling.textColor(title);
		title.setPadding(new Insets(30,20,10,20));
		screen.setTop(title);
	}
	
	/**
	 * Get the images downloading to the images folder and instantiate inner class for
	 * each image and add them to list
	 */
	private void getImageFiles() {
		File imagesDir = new File("images");
		File[] images = imagesDir.listFiles();
		int index = 0;
		for(File i: images) {
			try {
				BufferedImage image = ImageIO.read(i);
				listOfImages.add(new ImageDisplayBox(image, ++index));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add the images to the screen in two rows
	 */
	private void setUpImages() {
		HBox row1 = new HBox(10);
		HBox row2 = new HBox(10);
		
		row1.setSpacing(20);
		row2.setSpacing(20);
		
		row1.setAlignment(Pos.CENTER);
		row2.setAlignment(Pos.CENTER);
		
		row1.setPadding(new Insets(0,20,20,20));
		row2.setPadding(new Insets(20,20,20,20));
		
		for(int i = 0; i < 5; i++) {
			row1.getChildren().addAll(listOfImages.get(i));
		}
		
		for(int i = 5; i < 10; i++) {
			row2.getChildren().addAll(listOfImages.get(i));
		}
		
		rowsPane.getChildren().addAll(row1, row2);
	}
	
	/**
	 * Set up the error text for when they don't select at least one image
	 */
	private void setUpError() {
		errorText = new Label("");
		errorText.setFont(Font.font(Font.getDefault().getName(),20));
		errorText.setTextFill(Color.RED);
		
		BorderPane.setAlignment(errorText, Pos.BOTTOM_CENTER);
		
		errorText.setPadding(new Insets(0,0,30,0));
		
		screen.setBottom(errorText);
	}
	
	/**
	 * Check if there is at least one image selected
	 * @return	true 	at least one image is selected
	 * 			false	no images are selected
	 */
	public boolean isSelected() {
		if(listOfSelectedImages.size() == 0){
			errorText.setText("You must have at least one image selected to continue");
			errorText.setFont(Font.font ("Verdana", 16));
			return false;
		}else{
			errorText.setText("");
			return true;
		}
	}

	/**
	 * Save the selected images to a separate folder in the file system
	 */
	public void saveSelectedImages() {
		//Clear selected images folder for when they press back and then next
		scripts.getScript("clearSelImg", new String[]{});
		
		for(int i = 0; i < listOfSelectedImages.size(); i++) {
			String name = listOfSelectedImages.get(i).getNumber();
			scripts.getScript("copyImg", new String[]{name+".jpg", Integer.toString(i+1)});
		}
		
		amountSelected = listOfSelectedImages.size();
	}
	
	/**
	 * Return the number of images user has selected
	 * @return	number of images selected
	 */
	public int getAmountSelected(){
		return amountSelected;
	};


	/**
	 * Inner class for each image box seen on the screen(there will be 10 of these)
	 *
	 */
	private class ImageDisplayBox extends VBox{
		
		private ImageDisplayBox thisObject = this;
		private BufferedImage image;
		private int imageNumber;
		private CheckBox checkBox;
		
		/**
		 * @param image		the image this will display
		 * @param index		the number of this image
		 */
		private ImageDisplayBox(BufferedImage image, int index) {
			this.image = image;
			imageNumber = index;
			
			setUpImageBox();
			setUpCheckBox();
			
			this.setSpacing(10);
			this.setAlignment(Pos.CENTER);
		}
		
		/**
		 * Set up the image view of the image this will display
		 */
		private void setUpImageBox() {
			ImageView imageBox = new ImageView();
			image = resize(image, 200, 200);
			imageBox.setImage(SwingFXUtils.toFXImage(image, null));
			this.getChildren().add(imageBox);
		}
		
		/**
		 * Set up the checkbox
		 */
		private void setUpCheckBox() {
			checkBox = new CheckBox();
			checkBox.setIndeterminate(false);
			Styling.checkBox(checkBox);
			//When user checks the box it will add to the list and when they uncheck it will remove
			checkBox.setOnAction(e -> {
				if(checkBox.isSelected()) {
					listOfSelectedImages.add(thisObject);
				}
				else if((!checkBox.isSelected())) {
					listOfSelectedImages.remove(thisObject);
				}
			});
			this.getChildren().add(checkBox);
		}
		
		/**
		 * Gets the number(index) of this object
		 * @return	index as a string
		 */
		private String getNumber() {
			return String.valueOf(imageNumber);
		}
		
		/**
		 * Helper method to resize the images to fix in each box
		 * @param img		image to resize
		 * @param height	height to resize to
		 * @param width		width to resize to
		 * @return			resized image
		 */
		private BufferedImage resize(BufferedImage img, int height, int width) {
			Image tmp = img.getScaledInstance(width,  height,  Image.SCALE_SMOOTH);
			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = resized.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			return resized;
		}
	}
}
