# StudentChatApp
 Android chat application for City University Of NewYork student.

## Features

### Authentication
>Requires an email address ending with ‘cuny.edu'.
 
### Chat
>User can create a group and send text messages in the group chat

### Vote
>Users can vote on group messages the find useful

### View Top Posts
>Users can view the top posts of a group
>Show the appropriate amount of posts in the right order(by vote count).

### View Active users
>Users can see other users who are online

### Report Messages
>Report function for messages 
>While the button is there, there is not functionality on it other than a pop-up 

## Requirements
- Android version - SDK 29
- Firebase
>We used a firebase server for our instant messaging and authentication. This will require a google account. You can include firebase in the project by going to tools in android studio, clicking firebase, select connect to firebase. Then sign in and follow the prompts. When we have a firebase account set up, got to console, select authentication and click email. Now go to database and select realtime database. You will need to create a single node named Groups.

Documentation for mySQL setup can be found in HTTPS Request Servlets Project folder

## Manual 
1. Build the Servlet REST APIi
 - Create MySQL databse 
 - Create table by using Database.sql 
 - Change \source\keystore.ks file with your own credentials
2. Import Android Project
 - Change keys for firebase
 - Change keys for Test Fairy (Optinonal)