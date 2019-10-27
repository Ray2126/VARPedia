package varpedia.helper;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Creation {
	private String name;
    private Integer number;
    private String search;
    private ImageView image;

    public Creation(){
        this("",0,"");
    }

    public Creation(String name, int number, String search){
        this.name = name;
        this.number = number;
        this.search = search;
        this.image = getImageView(name);
    }
    
	private ImageView getImageView(String creation) {
		// load the image
	    File file = new File("./creations/"+creation+"/thumb.jpg");
	    Image image = new Image(file.toURI().toString());

        // simple displays ImageView the image as is
        ImageView iv = new ImageView();
        iv.setImage(image);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
	}
    
	public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setSearch(String search){
        this.search = search;
    }
    
    public void setImage(String name){
    	this.image = getImageView(name);
    }

    public ImageView getImage(){
        return image;
    }

    public String getSearch(){
        return search;
    }
    
    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }
    
    @Override
    public String toString() {
    	return name;
    }
}
