# StudentChatApp
 Android chat application for student

##Features
###Authentication
>Requires an email address ending with ‘cuny.edu'.
>We wanted to port this over to just require a phone# and cuny ID but this was already in place and we didn’t have the time to change it.
 
###Chat
>User can create a group and send text messages in the group chat

###Vote
>Users can vote on group messages the find useful
>We didn’t get to fully implement a proper voting weight system. RIght now it’s just based off of the raw numbers of votes. We decided to prioritize the other parts of the project over this.

###View Top Posts
>Users can view the top posts of a group
>Top post retrieval works but we needed to create another function to show the appropriate amount of posts in the right order(by vote count).

###View Active users
>Users can see other users who are online

###Report Messages
>Report function for messages 
>While the button is there, there is not functionality on it other than a pop-up 

##Requirements
-Android version - SDK 29
-Firebase
>We used a firebase server for our instant messaging and authentication. This will require a google account. You can include firebase in the project by going to tools in android studio, clicking firebase, select connect to firebase. Then sign in and follow the prompts. When we have a firebase account set up, got to console, select authentication and click email. Now go to database and select realtime database. You will need to create a single node named Groups.

Documentation for mySQL setup can be found in HTTPS Request Servlets Project folder

##Manual 
#Setting up Firebase


