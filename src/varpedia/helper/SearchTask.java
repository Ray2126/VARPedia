package varpedia.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class SearchTask extends Task<TextArea>{

	private String searchTerm;
	private Scripts scripts;
	
	public SearchTask(String searchTerm) {
		this.searchTerm = searchTerm;
		scripts = new Scripts();
	}
	
	@Override
	protected TextArea call() throws Exception {
		TextArea output = new TextArea();
		Process process = scripts.getScript("search", new String[] {searchTerm});
		if(process.exitValue() == 1) {
			return null;
		}
		try {
			FileInputStream fstream = new FileInputStream(searchTerm+".text");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				//append content of text file to TextArea output
				output.appendText(strLine.trim()+ "\n");
			}
			
			fstream.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return output;
	}

}
