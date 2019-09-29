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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
		_title = new Text("Choose the images you would like in your creation: ");
		_title.setTextAlignment(TextAlignment.CENTER);
		_topToBottomText = new Text("Order of images in creation: ");
		_topToBottomText.setTextAlignment(TextAlignment.CENTER);
		_listOfImages = new ArrayList<ImageDisplayBox>();
		
		_observableListOfSelectedImages = FXCollections.observableArrayList();
		_listOfSelectedImages = new ListView<String>(_observableListOfSelectedImages);
		_listOfSelectedImages.setMaxWidth(250);
		_listOfSelectedImages.setMaxHeight(245);
		_listOfSelectedImages.setMinHeight(100);
		_listOfSelectedImages.setStyle("-fx-alignment: CENTER-RIGHT;");
		_listOfSelectedImages.setPlaceholder(new Text("Please select some images"));
		
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
		row1.setSpacing(20);
		row1.setAlignment(Pos.CENTER);
		row1.setPadding(new Insets(0,20,20,20));
		HBox row2 = new HBox(10);
		row2.setSpacing(20);
		row2.setAlignment(Pos.CENTER);
		row2.setPadding(new Insets(20,20,20,20));
		//Add images to the rows panes
		for(int i = 0; i < 5; i++) {
			row1.getChildren().addAll(_listOfImages.get(i));
		}
		
		for(int i = 5; i < 10; i++) {
			row2.getChildren().addAll(_listOfImages.get(i));
		}
		
		VBox rowsPane = new VBox();
		rowsPane.setSpacing(20);
		rowsPane.setAlignment(Pos.CENTER);
		rowsPane.setPadding(new Insets(10,20,10,20));
		

		HBox title = new HBox();
		title.setSpacing(20);
		title.setAlignment(Pos.CENTER);
		title.setPadding(new Insets(0,20,30,20));
		title.getChildren().addAll(_title);
		_title.setFont(Font.font(Font.getDefault().getName(),20));
		_topToBottomText.setFont(Font.font(Font.getDefault().getName(),15));
		rowsPane.getChildren().addAll(title, row1, row2);
		//this.setTop(title);
		this.setCenter(rowsPane);
		BorderPane.setAlignment(_listOfSelectedImages,Pos.BOTTOM_CENTER);
		//this.setBottom(_listOfSelectedImages);
	}

	public int getAmountSelected(){
		return(amount);
	};
	
	//Get the images that have been selected by the user(images in the table)
	public void saveSelectedImages() {
		List<BufferedImage> outputList = new ArrayList<BufferedImage>();
		selected.clear();
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
			System.out.println(name);
			scripts.getScript("copyImg", new String[]{name});
		}

		amount = outputList.size();
	}

	public boolean isSelected() {
		if(_listOfSelectedImages.getItems().isEmpty()){
			Text place = new Text("You must have at least one image selected to continue");
			place.setFont(Font.font(Font.getDefault().getName(),15));
			place.setFill(Color.RED);
			_listOfSelectedImages.setPlaceholder(place);
			return false;
		}else{
			return true;
		}
	}

	//Inner class for each image display box (10 of these)
	private class ImageDisplayBox extends VBox{
		
		Label _imageNumber;
		BufferedImage _image;
		CheckBox _checkBox;
		
		private ImageDisplayBox(BufferedImage image, int index) {
			_image = image;
			_imageNumber = new Label("Image #" + index);
			_imageNumber.setFont(Font.font(Font.getDefault().getName(),15));
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
			this.setSpacing(10);
			this.setAlignment(Pos.CENTER);
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
