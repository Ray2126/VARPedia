package varpedia.creating;

import java.io.*;
import java.nio.file.Files;


public class Scripts {
	//Have made all Bash command chunks to do with a certain topic create their own script file
	//Which is then executed and deleted.
	
	//This allows all the code for running a script to be in own spot and user specifies the action to complete with a string
	public Process getScript(String request, String[] params) {
        File tempScript = null;
		try {
			switch(request) {
				case "clearSelImg":
					tempScript = clearSelectedImg();
					break;
				case "copyImg":
					tempScript = copySelectedImg(params[0]);
					break;
				case "name":
					tempScript = renamePreview(params[0]);
					break;
				case "preview":
					tempScript = createPreviewScript(params[0]);
					break;
				case "cleanup":
					tempScript = cleanupScript();
					break;
				case "mergeAudio":
					tempScript = mergeAudioScript();
					break;
				case "deleteAudio":
					tempScript = deleteAudioScript(params[0]);
					break;
				case "playAudio":
					tempScript = playAudioScript(params[0]);
					break;
				case "selectSave":
					tempScript = saveSelected(params[0], params[1], params[2], params[3]);
					break;
				case "listAudio":
					tempScript = audioListFileScript();
					break;
				case "selectPlay":
					tempScript = playSelected(params[0], params[1], params[2]);
					break;
				case "search":
					tempScript = searchScript(params[0]);
					break;
				case "new":
					tempScript = createScript(params[0], params[1], params[2]);
					break;
				case "listCreations":
					tempScript = createListFileScript();
					break;
				case "delete":
					tempScript = deleteScript(params[0]);
					break;
				case "nameValid":
					tempScript = nameValidScript(params[0]);
					break;
			}
	        try {
	            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
	            pb.inheritIO();
	            Process process = pb.start();
	            process.waitFor();
	            return process;
	        } finally {
	            tempScript.delete();
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public File mergeAudioScript() {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");

			//get all .wav files in ./audio
			File[] files = new File("./audio").listFiles( new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".wav");
				}
			});
			
			//Resample the bitrate of all the chunks
			printWriter.println("rm -f -r ./resampledAudio");
			printWriter.println("mkdir resampledAudio");
			for(int i = 0; i < files.length; i++) {
				String name = files[i].getName();
			    printWriter.println("if [ -f ./audio/"+name+"  ]; then");
				printWriter.println("ffmpeg -y -i ./audio/"+name+" -ar 25600 ./resampledAudio/"+name+" &> /dev/null");
                printWriter.println("fi");
			}
      
			printWriter.println("sox ./resampledAudio/*.wav output.wav");
			//printWriter.println("ffmpeg -q -f concat -safe 0 -i <( for f in ./audio/*.wav; do echo \"file '$(pwd)/$f'\";done ) output.wav &> /dev/null");
//			  File dir = new File("audio");
//			  File[] directoryListing = dir.listFiles();
//			  if (directoryListing != null) {
//			    for (File child : directoryListing) {
//			      // Do something with child
//			    	System.out.println(child.getName());
//			    	if(child.getName().contains(".wav")) {
//			    		printWriter.println("ffmpeg -y -i ./audio/"+child.getName()+" -ar 41000 ./audio/"+child.getName()+" &> /dev/null");
//			    	}
//			    }
//			  } else {
//			    // Handle the case where dir is not really a directory.
//			    // Checking dir.isDirectory() above would not be sufficient
//			    // to avoid race conditions with another process that deletes
//			    // directories.
//			  }
//			printWriter.println("ffmpeg -y -i ./audio/*.wav -ar 48000 &> /dev/null");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File clearSelectedImg() {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("rm -r selectedImages");
			printWriter.println("mkdir selectedImages");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File copySelectedImg(String name) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("NAME="+name);
			printWriter.println("cp ./images/${NAME} ./selectedImages/${NAME}");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File renamePreview(String name) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("mv ./creations/preview.mp4 ./creations/"+name+".mp4");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File cleanupScript() {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("rm -f pre.mp4");
			printWriter.println("rm -f ./creations/preview.mp4");
			printWriter.println("rm -f *.text");
			printWriter.println("rm -f ./images/*");
			printWriter.println("rm -f ./audio/*");
			printWriter.println("rm -f ./selectedImages/*");
			printWriter.println("rm -f ./resampledAudio/*");
			printWriter.println("rm -f output.wav");
			printWriter.println("rm -f selected.wav");
			printWriter.println("rm -f selected");
			printWriter.println("rm -f audios");
			printWriter.println("rm -f listing");


			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}
	
    public File searchScript(String searchTerm) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("TERM="+searchTerm);
	        printWriter.println("rm -f *.text");
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
    		e.printStackTrace();
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
	        printWriter.println("touch listing");
	        printWriter.println("ls ./creations/*.mp4 >listing 2> /dev/null");
	        printWriter.println("cat listing | sed 's/\\..*$//'");
	        
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }

	public File audioListFileScript() {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("#!/bin/bash");
			printWriter.println("ls ./audio/*.txt >audios 2> /dev/null");

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

	public File deleteAudioScript(String name) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("#!/bin/bash");
			printWriter.println("SELECTED="+name);
			printWriter.println("rm -f ${SELECTED}.txt");
            printWriter.println("rm -f ${SELECTED}.wav");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File playAudioScript(String name) {
		System.out.println("playing: "+name);
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

//			printWriter.println("#!/bin/bash");
//			printWriter.println("SELECTED="+name);
//			printWriter.println("ffplay -loglevel quiet -autoexit ${SELECTED}.wav");
			String cmd = "ffplay -loglevel quiet -autoexit "+name+".wav";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			pb.start();

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
	        printWriter.println("if [[ -f \"./creations/$NAME.mp4\" ]]; then");
	        printWriter.println("exit 1");
	        printWriter.println("fi");
	        printWriter.println("exit 0");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }

    public File playSelected(String selected, String synth, String voice){
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("#!/bin/bash");
			printWriter.println("TEXT='"+selected+"'");
			printWriter.println("echo $TEXT > selected");
			if(synth.equals("espeak")) {
				printWriter.println("espeak -f selected -w selected.wav");
			}
			else {
			    if(voice.equals("nz")){
                    printWriter.println("cat selected | text2wave -o selected.wav -eval nz.scm &>/dev/null");
                }else if(voice.equals("cg")){
                    printWriter.println("cat selected | text2wave -o selected.wav -eval cg.scm &>/dev/null");
                }else{
                    printWriter.println("cat selected | text2wave -o selected.wav &>/dev/null");
                }
			}
			printWriter.println("length=$(wc -w < selected.wav)");
			printWriter.println("if [ $length -eq 0 ]");
			printWriter.println("then");
			printWriter.println("exit 1");
			printWriter.println("fi");
			printWriter.println("ffplay -loglevel quiet -autoexit selected.wav");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File saveSelected(String selected, String name, String synth, String voice){
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("#!/bin/bash");
			printWriter.println("NAME="+name);
			printWriter.println("TEXT='"+selected+"'");
			printWriter.println("echo $TEXT > ./audio/$NAME.txt");
			if(synth.equals("espeak")) {
				printWriter.println("espeak -f ./audio/$NAME.txt -w ./audio/${NAME}.wav");
			}
			else {
				if(voice.equals("nz")){
					printWriter.println("cat ./audio/$NAME.txt | text2wave -o ./audio/${NAME}.wav -eval nz.scm &>/dev/null");
				}else if(voice.equals("cg")) {
					printWriter.println("cat ./audio/$NAME.txt | text2wave -o ./audio/${NAME}.wav -eval cg.scm &>/dev/null");
				}else {
				printWriter.println("cat ./audio/$NAME.txt | text2wave -o ./audio/${NAME}.wav &>/dev/null");
				}
			}
			printWriter.println("length=$(wc -w < ./audio/${NAME}.wav)");
			printWriter.println("if [ $length -eq 0 ]");
			printWriter.println("then");
			printWriter.println("rm -f ./audio/$NAME.txt");
			printWriter.println("rm -f ./audio/${NAME}.wav");
			printWriter.println("exit 1");
			printWriter.println("fi");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File createPreviewScript(String framerate) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			
			File[] files = new File(".").listFiles( new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".text");
				}
			});
			
			String name = files[0].getName().substring(0, files[0].getName().lastIndexOf("."));
			printWriter.println("length=$(soxi -D output.wav)");
			printWriter.println("framerate="+framerate);
			printWriter.println("cat ./selectedImages/*.jpg | ffmpeg -y -f image2pipe -framerate $framerate -i - -i output.wav -c:v libx264 -pix_fmt yuv420p -vf \"scale=1400:800\" -r 25 -max_muxing_queue_size 1024 -y ./creations/output.mp4  &> /dev/null" );
			printWriter.println("ffmpeg -y -i ./creations/output.mp4 -vf drawtext=\"fontfile=OpenSans-bold.ttf: text='"+name+"': fontcolor=white: fontsize=50: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy ./creations/preview.mp4 &> /dev/null");
			printWriter.println("rm -f ./creations/output.mp4");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File makeImageFiles(String amount) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("amount="+amount);
			printWriter.println("cat ./selectedImages/*.jpg | ffmpeg -y -f image2pipe -framerate $framerate -i - -i output.wav -c:v libx264 -pix_fmt yuv420p -vf \"scale=1400:800\" -r 25 -max_muxing_queue_size 1024 -y ./creations/preview.mp4 &> /dev/null" );

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
