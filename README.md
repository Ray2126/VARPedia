## Introduction
Thank you for choosing VARpedia for helping you to learn English!

The aim of this learning tool is to appeal to all types of learners - those who like to listen, those who like to read, those who are more visual and those who like to get hands-on with the content. 

Visual learners can take advantage of the image slideshow, listening learners can learn from the text to speech functionality, learners who like to read can read the descriptions and hands-on learners can test their knowledge in the quiz.

Use this manual to help you understand the application and hit the ground running in your English language journey!
## Running VARpedia
### System requirements
This application requires a UNIX/Linux based operating system and updated versions of FFmpeg.
### File System
In the main directory, you should see a resources folder, a jar file and a script file. The resources folder contains all important files necessary to have VARpedia function properly. Do not change this file.

The jar file contains the executable files that make the VARpedia magic happen. The script file is how YOU make the magic happen.
### Flickr API
To run VARpedia, you must include a Flickr API key. This can be obtained at https://www.flickr.com/services/api/misc.api_keys.html. Once you get a key and secret, enter these into the Flickr-api-key-sample.txt file (do not change formatting) found in the resources folder.
### Executing the programs script file
To run VARpedia you must open a terminal in the applications main directory. This is the level that contains the script file mentioned above. Open a terminal in this file and execute the command 

						./var.sh

If you are told you do not have the permissions to execute the file then execute the command

						chmod +x var.sh
	
Then you can attempt to run the first command again.

This will run a script that will execute all the commands necessary to get VARpedia up and running!

## Tutorial

### Your home menu
When first loading VARpedia you will be greeted with our home menu. There will be several
buttons leading to the different things you can choose to do.

#### Creating a new creation
This is where you will be able to make new creations on different topics. You choose the
audio, music and image in your new creation!

If this is your first time running the application or have removed all your previous creations,
this is where you want to go.

#### Viewing past creations
This is where you’ll go when you have already made some creations and want to revise and
learn more from them.

#### Quiz
The quiz helps you revise all your current creations and test your recognition of the content
and spelling of the English terms.

Go here when you are ready to challenge yourself on all your current creations!

#### Help me
This presents a comprehensive manual of the application embedded into VARpedia. This
popup can stay open when using the other functions incase you are ever stuck.

#### Quit
Use this button when you have finished using VARpedia for now and want to have a break
or do something else. This button will close the VARpedia application.

### Creating a new creation
#### Navigating the menus
When doing all the various tasks required to create a new creation there will always be three
buttons at the bottom right of the screen.

The next button is used once you are happy with the content selected on the current screen
and wish to move on. This button can be accessed by clicking on it or pressing the Enter key
on the keyboard.

Sometimes when you press the next button the new screen does not begin loading. This will
be because the content of the current page is not yet valid. Reasons for invalid content
include:

Improper or no search term
No audio chunks saved for your creation
No images selected for your creation
Improper/duplicated/missing name for your creation
Simply fix this issue on your current step to continue to the next one.

There is a back button that can be used if you go to another screen and realise you want to
change something in a previous section.

If you no longer wish to continue with your creation you can press the cancel button. This
will take you back to the home page and remove the current creation you were working on
content.

Don’t worry if you accidentally press this button a warning will appear asking you to confirm
this action. Simply press continue creating and you can continue creating and not return to
the home menu!

In between each screen for the various steps a loading icon will appear over the screen and
you will not be able to interact with the screen during the loading process. This is to ensure
you VARpedia application functions as smoothly as possible.

#### Searching for your topic
The first screen you will come to is the search term screen. This is where you will enter the
topic you wish to learn about.

VARpedia will search this topic on Wikipedia and return information on it.

You will need to enter an unambiguous search term with no spaces.

#### Choosing your audio
Here is where you select the chunks of audio you want to be spoken in your creation.

##### Searched term
In the top left corner of the screen, you will see the term you have searched for to generate
the text given below.

##### Text Area
All the text that was retrieved from Wikipedia on your chosen topic will be displayed here.
You can edit this text by clicking on the text and simply changing what is written.

##### Playing and saving audio
You can highlight chunks of text from the text area mentioned above and play or save this
audio.

Previewing the audio will simply use text to speech of the highlighted words and play them
back to you.

Saving audio will use text to speech on the highlighted text and save this audio to be used in
the creation. These can be deleted and replayed in the audio table below.

The selected text has to be less than 30 words and at least one word to be able to be played
and saved.

##### Voices
There is a voice selection dropdown menu next to the play and save buttons below the text
area. Here you can select the accent of the text to speech voice used. This is done by
clicking on the dropdown icon and clicking on the accent that you want to have for the
selected text

There are three options American, robotic and New Zealand to choose from.

This voice will be selected per chunk of text played/saved. Each chunk can have a different
voice.

##### Audio table
After saving a chunk of text it will appear in the audio chunk table at the bottom of the
screen. These will be the only audio chunks that will be played in your creation.

##### Playing and deleting saved audio
Saved audio chunks can be played and deleted. This is done by pressing the relevant button
in the row for that chunk of saved text.

Each saved audio will only take up one row of text and each row has its own play and delete
button to be clicked

The play button will play the audio back to the user and the delete button will remove that
audio and it will no longer be contained in the creation. If you would like to re-add this audio
you can simply reselect it from the text area above and save again.

#### Selecting background music
You can select from a range of music to add to your creation, whatever you think will help
you focus and learn. No music can be selected if that is not a feature you want.

You can play the music first to see if you like how it sounds. This is done by selecting the
play button in the row for that audio.

To select the music option you would like for your creation simply check the radio button in
the row corresponding to your music choice.

#### Choosing your images
On this screen, ten images will be displayed for you to pick and choose from for what will be
in your creation. All images will be played for equal amounts of time in your creation.

You must select at least one image to be able to go to the next screen.

To add an image to your creation simply click the checkbox below your desired image. When
the checkbox is ticked, the image will be present in your creation.

#### Final preview and naming your creation
##### Preview
A preview of your creation will be played to you before saving to allow you to confirm it is
exactly how you want it.

##### Naming your creation
Below the displayed video is an area for you to enter the name you want to give to your
creation.

You simply enter the desired name in this box and click finish creation or enter to attempt
using this name.
If the name is already taken you can choose the override the current creation or change the
chosen name. If you choose the override the previously named creation will be removed and
replaced by your new creation. If you choose to rename you will return to the preview screen
and can enter a new name.

The name entered can not have any special characters or spaces and must be entered. The
allowed characters are

_, -, numbers and letters

If you don’t do this you won’t be able to name your creation and will stay on this screen.

##### Going back and making changes
If you find you do not like your creation you can go back using the back button and change
the features you do not desire.

However, going back to the search screen will cause you to lose all progress on the other
screens.

### Using your past creations
#### Viewing your creations
To view your current creations, go to the home screen and click the “View Creations” button.
You will then see a screen similar to figure.

#### Playing your creations
To play your creations, press the button with the play icon in the table on the left side of the
screen (annotated 1 in the figure above). The creation will then start playing on the right side
of the screen (annotated 2 in the figure above).

#### Deleting your creations
Press the delete icon in the table which will make a pop-up appear. The pop-up will ask to
confirm that you want to delete the creation. Press “Yes” to confirm the deletion. Press “No”
if you change your mind and don’t want to delete. The creation will then disappear from the
table if you pressed “Yes”.

#### Going back to the home menu
To return back to the main menu, press the button with the home icon.

### Using the active learning component
The active learning component is a quiz where you guess the search term of each creation
video that is displayed to you. You are given feedback at the end of the quiz on whether your
guesses were correct or not.

This quiz is designed to help you judge your knowledge of the content and your spelling of
the search terms. While the capitalisation does not matter the correct spelling is necessary.

#### How to play the quiz
Each question will display to you the creation and have a text entry below it for you to enter
your guess to what search topic was used to create that creation.

Once you have entered your guess for that creation you can either press the next button at
the bottom of the screen or press the enter button on your keyboard.

#### Cancelling the quiz
If you no longer wish to complete the quiz you can click the cancel button at the bottom of
the window. This will cause you to lose your progress in the quiz and return to the main
menu.

If you wish to continue with the quiz you can press the quiz button on the home menu,
however, you will have to add all new guesses.

#### Viewing your feedback
At the end of the quiz, you will be shown your feedback on your guesses for each creation.
The thumbnail, name, topic and your guess will be displayed in each row as well as a tick or
cross representing whether you guessed correctly or not for this creation.

#### Going back to the home menu
Once you have finished reading through your feedback you can press the home icon button
at the bottom of the screen so you can return to the home menu and choose your next
action.

## Credits
Green and red ticks: https://www.needpix.com/photo/750885/right-wrong-red-green-icon-symbol-sign-simple-drawing

Icons: https://material.io/resources/icons/

Book image: http://pngimg.com/download/51049

Upbeat: http://ccmixter.org/files/speck/60371 converted to .wav resampled to 25600hz trimmed 12 secs to 30 secs

Piano: http://ccmixter.org/files/destinazione_altrove/60432 converted to .wav resampled to 25600hz trimmed 8 secs to 25 secs

Peaceful: http://ccmixter.org/files/panumoon/60396 converted to .wav resampled to 25600hz trimmed 00:01:17  00:01:34
