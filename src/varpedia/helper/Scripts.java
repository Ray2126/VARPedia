package varpedia.helper;

import java.io.*;

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
					tempScript = copySelectedImg(params[0], params[1]);
					break;
				case "name":
					tempScript = renamePreview(params[0], params[1]);
					break;
				case "preview":
					tempScript = createPreviewScript(params[0]);
					break;
				case "cleanup":
					tempScript = cleanupScript();
					break;
				case "mergeAudio":
					tempScript = mergeAudioScript(params[0]);
					break;
				case "deleteAudio":
					tempScript = deleteAudioScript(params[0]);
					break;
				case "selectSave":
					tempScript = saveSelected(params[0], params[1], params[2]);
					break;
				case "listAudio":
					tempScript = audioListFileScript();
					break;
				case "search":
					tempScript = searchScript(params[0]);
					break;
				case "listCreations":
					tempScript = createListFileScript();
					break;
				case "nameValid":
					tempScript = nameValidScript(params[0]);
					break;
				case "delCreation":
					tempScript = deleteCreationScript(params[0]);
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

	private File deleteCreationScript(String creation) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("if [ ! -f ./trash ]; then");
			printWriter.println("mkdir ./trash");
			printWriter.println("fi");
			printWriter.println("mv ./creations/"+creation+"/ ./trash/");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File mergeAudioScript(String music) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");

			//get all .wav files in ./audio
			File[] files = new File(".temp/audio").listFiles( new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".wav");
				}
			});
			
			//Resample the bitrate of all the chunks
			printWriter.println("rm -f -r .temp/resampledAudio");
			printWriter.println("mkdir .temp/resampledAudio");
			for(int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				printWriter.println("if [ -f .temp/audio/"+name+"  ]; then");
				printWriter.println("ffmpeg -y -i .temp/audio/"+name+" -ar 25600 .temp/resampledAudio/"+name+" &> /dev/null");
				printWriter.println("fi");
			}
      
			printWriter.println("sox .temp/resampledAudio/*.wav .temp/voice.wav");
			
			if(!music.equals("No music")) {
				printWriter.println("sox -v 0.2 ./resources/music/"+music+".wav .temp/quiet.wav &> /dev/null");
				printWriter.println("ffmpeg -y -stream_loop 20 -i .temp/quiet.wav -codec copy .temp/loop.wav &> /dev/null");
				printWriter.println("length=$(soxi -D .temp/voice.wav)");
				printWriter.println("ffmpeg -y -i .temp/loop.wav -ss 0 -to $length -codec copy .temp/cutLoop.wav &> /dev/null");
				printWriter.println("sox -M .temp/voice.wav .temp/cutLoop.wav .temp/output.wav &> /dev/null");
				
				printWriter.println("rm -f .temp/quiet.wav");
				printWriter.println("rm -f .temp/loop.wav");
				printWriter.println("rm -f .temp/cutLoop.wav");
			}
			else {
				printWriter.println("cp .temp/voice.wav .temp/output.wav");
			}

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
			printWriter.println("rm -r .temp/selectedImages");
			printWriter.println("mkdir .temp/selectedImages");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File copySelectedImg(String name, String index) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("NAME="+name);
			printWriter.println("I="+index+".jpg");
			printWriter.println("cp .temp/images/${NAME} .temp/selectedImages/${I}");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}

	public File renamePreview(String name, String searched) {
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("mkdir -p ./creations/"+name+"/quiz");
			printWriter.println("mv .temp/preview/preview.mp4 ./creations/"+name+"/"+name+".mp4");
			printWriter.println("cp .temp/selectedImages/1.jpg ./creations/"+name+"/thumb.jpg");
			//Create text file for searched
			printWriter.println("echo "+searched+"> ./creations/"+name+"/quiz/searchTerm.text");
			printWriter.println("mv .temp/preview/noText.mp4 ./creations/"+name+"/quiz/noText.mp4");

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
			String cmd = "rm -fr .temp";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			printWriter.println("#!/bin/bash");
//			printWriter.println("rm -f pre.mp4");
//			printWriter.println("rm -f ./creations/preview.mp4");
//			printWriter.println("rm -f *.text");
//			printWriter.println("rm -f ./images/*");
//			printWriter.println("rm -f ./audio/*");
//			printWriter.println("rm -f ./selectedImages/*");
//			printWriter.println("rm -f ./resampledAudio/*");
//			printWriter.println("rm -f output.wav");
//			printWriter.println("rm -f selected.wav");
//			printWriter.println("rm -f selected");
//			printWriter.println("rm -f audios");
//			printWriter.println("rm -f listing");
//			printWriter.println("rm -f voice.wav");
//			
//			printWriter.println("if [ ! -d trash ]; then");
//			printWriter.println("mkdir -p trash;");
//			printWriter.println("fi");
//			
//			printWriter.println("mv temp trash");
//			printWriter.println("rm -rf trash");
//			printWriter.println("rm -rf images");
//			printWriter.println("rm -rf audio");
//			printWriter.println("rm -rf resampledAudio");
//			printWriter.println("rm -rf selectedImages");

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
	        printWriter.println("rm -f .temp/*.text");
	        printWriter.println("filename=.temp/${TERM}.text");
	        printWriter.println("wikit $TERM >.temp/${TERM}.text");
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
	        printWriter.println("mkdir .temp 2> /dev/null");
	        printWriter.println("touch .temp/listing 2> /dev/null");
	        printWriter.println("ls ./creations 1>.temp/listing 2> /dev/null");
	        
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
			printWriter.println("ls .temp/audio/*.txt >.temp/audios 2> /dev/null");

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
    
    public File nameValidScript(String name) {
    	try {
	        File tempScript = File.createTempFile("script", null);
	
	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);
	
	        printWriter.println("#!/bin/bash");
	        printWriter.println("NAME="+name);
	        printWriter.println("if [[ -d \"./creations/$NAME\" ]]; then");
	        printWriter.println("exit 1");
	        printWriter.println("fi");
	        printWriter.println("exit 0");
	
	        printWriter.close();
	
	        return tempScript;
    	}catch (Exception e) {
    		return null;
    	}
    }

	public File saveSelected(String selected, String name, String voice){
		try {
			File tempScript = File.createTempFile("script", null);

			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
					tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);

			printWriter.println("#!/bin/bash");
			printWriter.println("FILE=.temp/audio");
			printWriter.println("if [ ! -d \"$FILE\" ]; then");
			printWriter.println("mkdir .temp/audio 2> /dev/null");
			printWriter.println("fi");
			printWriter.println("NAME="+name);
			printWriter.println("TEXT='"+selected+"'");
			printWriter.println("echo $TEXT > .temp/audio/$NAME.txt");
			if(voice.equals("Robot Voice")) {
				printWriter.println("espeak -f .temp/audio/$NAME.txt -w .temp/audio/${NAME}.wav");
			}else if (voice.equals("New Zealand Accent")){
				printWriter.println("cat .temp/audio/$NAME.txt | text2wave -o .temp/audio/${NAME}.wav -eval ./resources/nz.scm &>/dev/null");
			}else {
				printWriter.println("cat .temp/audio/$NAME.txt | text2wave -o .temp/audio/${NAME}.wav &>/dev/null");
			}
			printWriter.println("length=$(wc -w < .temp/audio/${NAME}.wav)");
			printWriter.println("if [ $length -eq 0 ]");
			printWriter.println("then");
			printWriter.println("rm -f .temp/audio/$NAME.txt");
			printWriter.println("rm -f .temp/audio/${NAME}.wav");
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
			
			File[] files = new File(".temp").listFiles( new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".text");
				}
			});
			
			String name = files[0].getName().substring(0, files[0].getName().lastIndexOf("."));
			printWriter.println("length=$(soxi -D .temp/output.wav)");
			printWriter.println("framerate="+framerate);
			printWriter.println("mkdir .temp/preview 2> /dev/null");

			printWriter.println("cat .temp/selectedImages/*.jpg | ffmpeg -y -f image2pipe -framerate $framerate -i - -i .temp/output.wav -c:v libx264 -pix_fmt yuv420p -vf \"scale=1400:800\" -r 25 -max_muxing_queue_size 1024 -y .temp/preview/noText.mp4 &> /dev/null" );
			printWriter.println("ffmpeg -y -i .temp/preview/noText.mp4 -vf drawtext=\"fontfile=OpenSans-bold.ttf: text='"+name+"': fontcolor=white: fontsize=50: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy .temp/preview/preview.mp4 &> /dev/null");
			printWriter.println("rm -f ./creations/output.mp4");

			printWriter.close();

			return tempScript;
		}catch (Exception e) {
			return null;
		}
	}


}