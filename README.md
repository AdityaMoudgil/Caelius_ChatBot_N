# Caelius_ChatBot_N
Caelius Chat Bot Project made using Java Swing Framework, OOPS Concepts, MySQL Database.

This is a Java-based chat bot that can answer questions based on a predefined set of questions and answers stored in a MySQL database. If a question is not available in the database, it can perform a Google search and display the results.

## Features

- User-friendly GUI for interacting with the chat bot.
- Retrieves answers from a MySQL database based on user's questions.
- Performs Google search for unanswered questions.
- Displays results in the chat window.

## Requirements

- Java Development Kit (JDK) installed
- MySQL database server
- MySQL Connector/J JDBC driver

## Installation

1. Clone the repository to your local machine:

   ```shell
   git clone https://github.com/AdityaMoudgil/Caelius_ChatBot_N
   
2. Import the project into your preferred Java IDE (e.g., Eclipse, IntelliJ IDEA).
3. Set up the MySQL database:
4. Create a database named chatbot_db.
5. Create a table named qa with columns question and answer.
6. Populate the qa table with relevant question-answer pairs.
7. Add the MySQL Connector/J JDBC driver to your project's dependencies:
   a) Download the MySQL Connector/J driver from the MySQL website.
   b) Add the JAR file to your project's build path or dependencies.
8. Update the database connection details:
   a) Open the Main.java file in your IDE. 
   b) Update the url, username, and password variables in the loadQuestionsFromDatabase and getAnswerFromDatabase methods with your MySQL database connection details.
   c)Compile and run the Main.java file to start the chat bot application.
## Usage

1. Launch the Java Chat Bot application.
2. Enter your question in the input field and press the Enter key or click the ">" button.
3. The chat bot will display your question and provide an answer based on the available data in the database or perform a Google search and display the results.
4. Continue the conversation by entering more questions or queries.
5. If the question does not exist in the MySQL Database, it can perform a Google search and display the results.
