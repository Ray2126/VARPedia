package gemmas.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class Scripts {
	//Have made all Bash command chunks to do with a certain topic create their own script file
	//Which is then executed and deleted.
	
	//This allows all the code for running a script to be in own spot and user specifies the action to complete with a string
	public Process getScript(String request, String[] params) {
        File tempScript = null;
		try {
			switch(request) {
			case "search":
				tempScript = searchScript(params[0]);
				break;
			case "new":
				tempScript = createScript(params[0], params[1], params[2]);
				break;
			case "listCreations":
				tempScript = createListFileScript();
				break;
			case "play":
				tempScript = playScript(params[0]);
				break;
			case "delete":
				tempScript = deleteScript(params[0]);
				break;
			case "nameValid":
				tempScript = nameValidScript(params[0]);
				break;
			case "lineValid":
				tempScript = lineValidScript(params[0], params[1]);
				break;
			}
	        try {
	        	if(tempScript != null) {
					ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
					pb.inheritIO();
					Process process = pb.start();
					process.waitFor();
					return process;
				}
	        } finally {
	        	if(tempScript != null) {
					tempScript.delete();
				}
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public File searchScript(String searchTerm) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("TERM="+searchTerm);
	        printWriter.println("filename=${TERM}.text");
	        printWriter.println("wikit $TERM >${TERM}.text");
	        printWriter.println("if grep -q \"not found :^(\" \"$filename\"; then");
	        printWriter.println("exit 1");
	        printWriter.println("fi");
	        printWriter.println("sed -i 's/'[.]'/.\\n/g' $filename");
	        printWriter.println("sed -i '/^$/d' $filename");
	        printWriter.println("lineCount=$(wc -l $filename)");
	        printWriter.println("lineCount=`echo $lineCount | grep -P -o \"[[:digit:]]+\" | head -1`");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }
    
    public File createListFileScript() {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("ls *.avi >creations 2> /dev/null");
	        printWriter.println("sed -i 's/\\..*$//' creations");
	        
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }
    
    public File deleteScript(String name) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("SELECTED="+name);
	        printWriter.println("rm -f ${SELECTED}.avi");
	        
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }
    
    public File playScript(String name) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("SELECTED="+name);
	        printWriter.println("ffplay -loglevel quiet -autoexit ${SELECTED}.avi");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }
    
    public File nameValidScript(String name) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("NAME="+name);
	        printWriter.println("if [[ -f \"$NAME.avi\" ]]; then");
	        printWriter.println("exit 1");
	        printWriter.println("fi");
	        printWriter.println("exit 0");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }
    
    public File lineValidScript(String term ,String lines) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("TERM="+term);
	        printWriter.println("filename=${TERM}.text");
	        printWriter.println("lineCount=$(wc -l $filename)");
	        printWriter.println("lineCount=`echo $lineCount | grep -P -o \"[[:digit:]]+\" | head -1`");
	        printWriter.println("AMOUNT="+lines);
	        printWriter.println("if [[ ! \"$AMOUNT\" =~ ^[0-9]+$ ]] || [[ $AMOUNT -gt $lineCount ]] || [[ $AMOUNT -lt 1 ]]; then\n");
	        printWriter.println("exit 1");
	        printWriter.println("fi");
	        printWriter.println("exit 0");
	        
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }


    public File createScript(String term, String amount, String name) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
            printWriter.println("rm -f *.wav");
            printWriter.println("rm -f *.mp4");
	        printWriter.println("TERM="+term);
	        printWriter.println("filename=${TERM}.text");
	        
	        printWriter.println("lineCount=$(wc -l $filename)");
	        printWriter.println("lineCount=`echo $lineCount | grep -P -o \"[[:digit:]]+\" | head -1`");

	        printWriter.println("AMOUNT="+amount);
	        printWriter.println("let \"DELETE=$lineCount-$AMOUNT\"");
	        printWriter.println("for (( i=1; i<=$DELETE; i=i+1 )) ; do");
	        printWriter.println("sed -i '$d' $filename");
	        printWriter.println("done");
    		printWriter.println("cat $filename | text2wave -o ${TERM}.wav &>/dev/null");
    		printWriter.println("if [ $? == 1 ]");
    		printWriter.println("then ");
    		printWriter.println("echo \"error encountered\" >&2");
            printWriter.println("exit 1");
            printWriter.println("fi");
            printWriter.println("NAME="+name);
            printWriter.println("length=$(soxi -D ${TERM}.wav)");
            printWriter.println("ffmpeg -loglevel quiet -f lavfi -i color=c=blue:s=320x240:d=$length -vf \"drawtext=fontfile=./VideoPhreak.ttf:fontsize=30:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='$TERM'\" video.mp4");
            printWriter.println("ffmpeg -loglevel quiet -i video.mp4 -i ${TERM}.wav -codec copy -shortest ${NAME}.avi");
            printWriter.println("if [ $? == 1 ]");
            printWriter.println("then ");
            printWriter.println("echo \"error encountered\" >&2");
            printWriter.println("exit 1");
            printWriter.println("fi");
            printWriter.println("rm -f ${TERM}.wav");
            printWriter.println("rm -f $filename");
            printWriter.println("rm -f video.mp4");
            printWriter.println("rm -f *.text");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }

}
