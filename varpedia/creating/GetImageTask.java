package varpedia.creating;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.concurrent.Task;

public class GetImageTask extends Task<Void>{
	//retrieve with wget
	
private String _wordToSearch;
	
	public GetImageTask(String wordToSearch) {
		_wordToSearch = wordToSearch;
	}
	
	//Get either the api key or shared secret from the text file
	private String getAPIKey(String key) throws IOException{

		String apiKeyFile = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 

		File file = new File(apiKeyFile); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		
		return null;
	}

	@Override
	protected Void call() throws Exception{
		//Create file for images to be stored
		File f = new File("images");
		f.mkdir();
		
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");
		
			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = _wordToSearch;
			int resultsPerPage = 10;
			int page = 0;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        
	        for (Photo photo: results) {
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.LARGE);
	        		
		        	String fileName = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
		        	
		        	File outputFile = new File("images",fileName);
		        	outputFile.createNewFile();
		        	
		        	ImageIO.write(image, "jpg", outputFile);
	        	} catch (FlickrException fe) {
	        		//Error handling here, not sure what kind yet
				}
	        }
		
		}
		catch (IOException e){
			//Error handling here, not sure what kind yet
		}

		
		return null;
	}

}
