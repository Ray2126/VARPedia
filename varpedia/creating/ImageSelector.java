package varpedia.creating;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ImageSelector extends BorderPane{
	// User selects how many images to create
	// getImages() method; wgets amount of images
	
	private Text _title;
	private Text _topToBottomText;
	private List<ImageDisplayBox> _listOfImages;
	private ListView<String> _listOfSelectedImages;
	private ObservableList<String> _observableListOfSelectedImages;
	private int amount;
	ArrayList<String> names;
	ArrayList<String> selected;
	Scripts scripts;

	
	public ImageSelector() {
		_title = new Text("Choose the images you would like in the creation: ");
		_topToBottomText = new Text("The images in the final creation will be displayed in order from top to bottom of this list: ");
	
		_listOfImages = new ArrayList<ImageDisplayBox>();
		
		_observableListOfSelectedImages = FXCollections.observableArrayList();
		_listOfSelectedImages = new ListView<String>(_observableListOfSelectedImages);
		
		//Read the images from the directory
		File imagesDir = new File("images");
		File[] images = imagesDir.listFiles();
		BufferedImage image;
		names = new ArrayList<>();
		selected = new ArrayList<>();
		scripts = new Scripts();
		int index = 0;
		for(File i: images) {
			try {
				names.add(i.getName());
				image = ImageIO.read(i);
				_listOfImages.add(new ImageDisplayBox(image, ++index));
			} catch (IOException e) {
				//If image cannot be read
			}
		}
		
		HBox row1 = new HBox(10);
		HBox row2 = new HBox(10);
		
		//Add images to the rows panes
		for(int i = 0; i < 5; i++) {
			row1.getChildren().addAll(_listOfImages.get(i));
		}
		
		for(int i = 5; i < 10; i++) {
			row2.getChildren().addAll(_listOfImages.get(i));
		}
		
		VBox rowsPane = new VBox();
		rowsPane.getChildren().addAll(row1, row2, _topToBottomText);
		
		this.setTop(_title);
		this.setCenter(rowsPane);
		this.setBottom(_listOfSelectedImages);
	}

	public int getAmountSelected(){
		return(amount);
	};
	
	//Get the images that have been selected by the user(images in the table)
	public void saveSelectedImages() {
		List<BufferedImage> outputList = new ArrayList<BufferedImage>();
		for(int i = 0; i < _observableListOfSelectedImages.size(); i++) {
			for(int j = 0; j < _listOfImages.size(); j++) {
				if(_observableListOfSelectedImages.get(i).equals(_listOfImages.get(j).getName())) {
					outputList.add(_listOfImages.get(j).getImage());
					selected.add(names.get(j));
				}
			}
		}

		Iterator<String> iter= selected.iterator();
		scripts.getScript("clearSelImg", new String[]{});
		while(iter.hasNext()){
			String name = iter.next();
			//cp script
			scripts.getScript("copyImg", new String[]{name});
		}

		amount = outputList.size();

//		File newFolder = new File("selectedImages");
//		newFolder.mkdir();
//
//		File file;
//		for(int i = 1 ; i <= outputList.size(); i++) {
//			file = new File("selectedImages/" + i );
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				ImageIO.write(outputList.get(i-1), "jpg", file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	//Inner class for each image display box (10 of these)
	private class ImageDisplayBox extends VBox{
		
		Label _imageNumber;
		BufferedImage _image;
		CheckBox _checkBox;
		
		private ImageDisplayBox(BufferedImage image, int index) {
			_image = image;
			_imageNumber = new Label("Image #" + index);
			_checkBox = new CheckBox();
			_checkBox.setIndeterminate(false);
			
			_image = resize(_image, 200, 200);
			
			ImageView imageBox = new ImageView();
			imageBox.setImage(SwingFXUtils.toFXImage(_image, null));
			
			//When user checks the box it will add to the list and when they uncheck it will remove
			_checkBox.setOnAction(new EventHandler<ActionEvent>() {	
				@Override
				public void handle(ActionEvent arg0) {
					if(_checkBox.isSelected()) {
						_observableListOfSelectedImages.add(_imageNumber.getText());
					}
					else if((!_checkBox.isSelected())) {
						_observableListOfSelectedImages.remove(_imageNumber.getText());
					}
				}
			});
			
			
			this.getChildren().addAll(_imageNumber, imageBox, _checkBox);
		}
		
		private BufferedImage getImage() {
			return _image;
		}
		
		private String getName() {
			return _imageNumber.getText();
		}
		
		//Helper method to resize the image
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
