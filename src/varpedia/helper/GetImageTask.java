package varpedia.helper;

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

/**
 * The concurrency background task to get the images from Flickr
 *
 */
public class GetImageTask extends Task<Void>{
	
	private String wordToSearch;
	
	public GetImageTask(String wordToSearch) {
		this.wordToSearch = wordToSearch;
		
		//If user goes back and then forward to images screen, make sure no images roll over
		File images = new File(".temp/images");
		images.delete();
		images.mkdir();
		File selected = new File(".temp/selectedImages");
		selected.delete();
		selected.mkdir();
	}
	
	private String getAPIKey(String key) throws IOException{

		String apiKeyFile = System.getProperty("user.dir") 

				+ System.getProperty("file.separator")+ "resources" + System.getProperty("file.separator") + "flickr-api-keys.txt"; 

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
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");
		
			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = wordToSearch;
			int resultsPerPage = 10;
			int page = 0;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);

	        int i = 0;
	        for (Photo photo: results) {
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.LARGE);
	        		
		        	String fileName = (++i)+".jpg";
		        	
		        	File outputFile = new File(".temp/images",fileName);
		        	outputFile.createNewFile();
		        	
		        	ImageIO.write(image, "jpg", outputFile);
	        	} catch (FlickrException fe) {
				}
	        }
		
		}
		catch (IOException e){
		}

		
		return null;
	}

}
