/*
 * UserRegistration uses multiple methods and a main menu that can register new users, allow users to complete a competency assessment, and let users login to view their past scores. 
 * 
 * @author Anjing Li
 * @version 1.0
 * @since 2019-12-13
 */

// Import to use in code
import java.util.Scanner; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.io.FileWriter; 
import java.io.File; 

public class UserRegistration {
  // Main method: Asks user if they want to access a previously created account, make a new account, or exit. Will call the necessary methods.
  public static void main(String[] args) {
    // Initialize scanner
    Scanner input = new Scanner (System.in); 
    
    // Declare variable
    String strAnswer; 
    
    // Boolean to use in do while loop, to make sure program can exit
    boolean exit = false; 
    do {
      // Prompt user for response
      System.out.println("Do you already have an account in the database? (Type yes or no.) If you wish to exit, please type exit."); 
      
      // Assign variable to user input 
      strAnswer = input.nextLine(); 
      
      // If statements to check if yes/no/exit, and then proceed to the proper method/display message/exit
      if (strAnswer.equalsIgnoreCase("yes")) {
        UserLogin(input);
        System.out.println("Successfully logged out."); 
      } else if (strAnswer.equalsIgnoreCase("no")) {
        UserSignUp(input);
        System.out.println("Successfully logged out.");
      } else if (strAnswer.equalsIgnoreCase("exit")) {
        exit = true; 
      }
    } while (!exit); // Do while loop runs unless exit is true
    // Display closing program to user
    System.out.println("Exit is successful.");
  } // End main
  
  /**
   * This method lets the user sign up if they meet all the requirements for the username and password. It will then tell the user they will
   * do the competency assessment, and stores all data. 
   * 
   * @param input This is the first parameter and it is a Scanner for user input
   * @return void This does not return anything.
   */
  
  public static void UserSignUp (Scanner input) {
    // Declare variables and Boolean variables are set to false
    boolean verifyName = false; 
    boolean verifyPassword = false; 
    boolean verifyExisting = false; 
    boolean valid = false; 
    String strUsername, strPassword; 
    
    // Do while loop to check if valid user sign up (new user/follows all requirements)
    do {
      // Prompt user to enter a new username
      System.out.println("Please enter your desired username. Requirements: No spaces.");
      // Get user input and store as username
      strUsername = input.nextLine();
      // Prompt user to enter a new password
      System.out.println("Please enter your desired password. Requirements: Minimum of 7 characters, at least one capital letter with lowercase letters, have at least one symbol and one number, and no spaces."); 
      // Get user input and store as password
      strPassword = input.nextLine(); 
      
      // Use verifyUsername/verifyValidPassword methods to check if the username/password satisfies the requirements
      verifyName = verifyUsername (strUsername); 
      verifyPassword = verifyValidPassword (strPassword); 
      
      // If statement/else ifs to see if username and/or password satisfies requirements, and if they don't, tell the user why/which requirement they didn't satisfy, and will lead to a retry
      if (verifyName == false && verifyPassword == false) { // Checks if both username and password don't satisfy requirements (if false, will ask user to retry)
        System.out.println("Invalid username and password. Review the requirements for the username and the password and try entering a different one. Please retry.");
      } else if (verifyName == false) { // Checks only username (if false, will ask user to retry)
        System.out.println("Please try a different username that meets the requirements.");
      } else if (verifyPassword == false) { // Checks only password (if false, will ask user to retry)
        System.out.println("Please try a different password that meets the requirements."); 
      } // End if statement
      
      // Use verifyExistingUser method to check if the username already exists
      verifyExisting = verifyExistingUser (strUsername);
      
      // If statement to tell user that if false, the username has already been used, and to retry with a different username
      if (verifyExisting == false) {
        System.out.println("Username has already been taken. Please try a different username."); 
      } // End if statement
      
      // If statement to make user sign up valid if username does not already exist, and both username and password satisfies requirements
      if (verifyName && verifyPassword && verifyExisting) {
        valid = true; 
      } // End if statement
    } while (!valid); // While causes loop to continue only if still not valid, once valid, loop stops 
    
    // Display statement to inform user that both are valid, and they can now do the competency assessment
    System.out.println("Username and password are valid! Please wait for the competency assessment."); 
    
    // Get user's score from CompetencyAssessment method
    int intScore = CompetencyAssessment(input);
    
    // use storeData method to save username, password, and score in the txt file
    storeData(strUsername, strPassword, intScore); 
  } // End method
  
  /**
   * This method will check if the username is valid and that there are no blank spaces.
   * 
   * @param strUsername This is the first parameter and it is the user inputted username
   * @return boolean This returns the result of true if the username is valid (no blank spaces), false otherwise. 
   */
  
  public static boolean verifyUsername (String strUsername) {
    // For loop to check that there are no spaces in the username, repeats the same amount of times as the username's length
    for (int i = 0; i < strUsername.length(); i++) {
      // Sets x as one character in user's desired username, x will change depending on value of i (increasing/to the right in username), until each character has been checked
      char x = strUsername.charAt(i); 
      // If there is a blank space, this method will return false, resulting in the user needing to retry with a valid username
      if (Character.isWhitespace(x)) {
        return false; 
      }
    }
    // Returns true if satisfies the no spaces requirement
    return true; 
  }
  
  /**
   * This method checks if the password is valid and satisfies all requirements.
   * (Has an uppercase and lowercase character, a symbol, a number, and at least 7 characters.)
   * 
   * @param strPassword This is the first parameter and it is the user inputted password
   * @return boolean This returns the result of true if password is valid and satisfies all requirements (refer to above), false otherwise.
   */
  
  public static boolean verifyValidPassword(String strPassword) {
    // Declare boolean variables and set all as false
    boolean upperCase = false; 
    boolean lowerCase = false; 
    boolean sym = false; 
    boolean number = false; 
    
    // Store the password into a char array
    char[] arrPassword = strPassword.toCharArray(); 
    
    // If statement to check password length, if less than 7, method will return false
    if (strPassword.length() < 7) {
      return false;
    }
    
    // This will check each individual character in the password, using the previously created char array
    for (char i : arrPassword) {
      // Change the previous char into an int, and set it equal to intASCII, so it can be compared as an ASCII value
      int intASCII = (int) i; 
      
      // If statement checks if it has any blank spaces
      if (intASCII == 0 || intASCII == 9 || intASCII == 32) {
        return false; // Returns false if there are blank spaces
      } // End if statement
      
      if (!upperCase && intASCII >= 65 && intASCII <= 90) { // If statement checks if there is an uppercase character, and sets upperCase as true if it satisfies if statement
        upperCase = true;
        // Else if checks if there are symbols in the password
      } else if (!sym && (intASCII >= 33 && intASCII <= 47) || (intASCII >=58 && intASCII <= 64) || (intASCII >= 91 && intASCII <= 96) || (intASCII >= 123 && intASCII <= 126)) {
        sym = true;
      } else if (!number && intASCII >= 48 && intASCII <= 57){ // Else if checks if there is a number present in the password, and sets Boolean number as true if it satisfies if statement
        number = true;
      } else if (!lowerCase && intASCII >= 97 && intASCII <= 122) { // Else if checks if there is a number present in the password, and sets Boolean lowerCase as true if it satisfies if statement
        lowerCase = true;
      } 
    }
    
    // If statement checks that all four boolean variables are now true, and then returns true if that is the case
    if (lowerCase && upperCase && number && sym) {
      return true;
    } else { 
      return false; // Will return false if not all four boolean variables are true
    }
  } // End method
  
  /**
   * This method checks if the username exists in the database (txt file).
   * 
   * @param strUsername This is the first parameter and it is the user inputted username. 
   * @return boolean This returns the result of true if the username exists in the database, false otherwise.
   */
  
  public static boolean verifyExistingUser (String strUsername) {
    // Try/catch used for this code, since it may cause an exception to arise
    try {
      // Scanner for database.txt file
      Scanner fileInput = new Scanner (new File ("database.txt"));
      // While loop to check the file
      while (fileInput.hasNext()) {
        // String array is used, answers are split with a comma
        String [] line = fileInput.nextLine().split(","); 
        if (strUsername.equals(line[0].trim())){ // If the username exists, then it will return false
          fileInput.close();
          return false;
        }
      }
      fileInput.close();
      return true; // Returns true if username does not exist
    }catch (IOException ioException) {
      // Displays reason for exception, tells user to retry when there is a database.txt file
      System.out.println("IO Exception, try again when file database.txt has already been made."); 
      System.exit(0); // Exits program
      return false; // Returns false 
    } // End catch
  } // End method
  
  /**
   * This method performs a competency assessment/quiz about BTS, it will then display the final score for the user.
   * 
   * @param input This is the first parameter and it is the Scanner (user input)
   * @return int This returns the result of the score that the user gets from the competency assessment/quiz.
   */
  
  public static int CompetencyAssessment (Scanner input) {
    // Display topic for competency assessment
    System.out.println("The topic of the competency assessment is... BTS! This 13 question quiz will test your knowledge about the K-Pop group. Please enter A, B, C, or D."); 
    
    // Declare and initialize array for the questions
    String arrQuestion[] = new String [13];
    // Declare, initialize, and store answers in the answer array, uses String array so the user input (also String) can be directly compared to array
    String arrAnswer [] = {"B", "A", "D", "C", "A", "B", "D", "A", "C", "B", "A", "A", "A"}; 
    
    // Declare and set variable to blank
    String strAnswer = ""; 
    
    // Declare and set variable to 0 
    int intScore = 0;
    
    // Questions are stored in the question array
    arrQuestion[0] = "What year was BTS formed? (Hint: Look at the number of questions) \nA: 2015 \nB: 2013  \nC: 2017 \nD: 2010";
    arrQuestion[1] = "What show did RM learn English from? \nA: Friends \nB: How I Met Your Mother  \nC: Suits \nD: Brooklyn 99";
    arrQuestion[2] = "Who is the youngest member? \nA: Jimin \nB: RM  \nC: Suga \nD: Jungkook";
    arrQuestion[3] = "What is the name of their debut song? \nA: Butterfly \nB: Fake Love  \nC: No More Dream \nD: Dionysus";
    arrQuestion[4] = "Which member is a rapper in their group? \nA: J-Hope \nB: Jimin  \nC: Jungkook \nD: V";
    arrQuestion[5] = "What is the name of Suga's dog? \nA: Mickey \nB: Holly  \nC: Gureum \nD: Rapmon";
    arrQuestion[6] = "What is the main award that BTS hopes to win one day? \nA: An award from the AMAs \nB: A Billboard Music Award  \nC: A MTV Music Award \nD: A Grammy";
    arrQuestion[7] = "When did BTS's second Korean studio album come out, and what is it called? \nA: Oct 10 2016, Wings \nB: Oct 2 2015, LY: Answer \nC: Oct 10 2016, LY: Tear \nD: Jan 10 2016, Wings";
    arrQuestion[8] = "Which song is BTS's longest charting song on Melon? \nA: Fake Love \nB: Love Yourself: Answer  \nC: Spring Day \nD: Not Today";
    arrQuestion[9] = "Which member is a vocalist in their group? \nA: Suga \nB: Jin \nC: J-Hope \nD: RM";
    arrQuestion[10] = "Around how much do they contribute to South Korea's economy? \nA: $3.6 billion \nB: $1.2 million  \nC: $2.2 billion \nD: $2.9 billion";
    arrQuestion[11] = "What product did BTS's Jungkook recently cause to sell out? \nA: Downy Adorable fabric softener \nB: Recording equipment  \nC: Cartier rings \nD: Chanel earrings";
    arrQuestion[12] = "BTS partnered with UNICEF to help end violence. What was the campaign called? \nA: Love Myself \nB: Butterfly  \nC: Wings \nD: You Never Walk Alone";
    
    // For loop that repeats for as long as the question array is, increments by 1 each time
    for (int i = 0; i < arrQuestion.length; i++) {
      // Displays question in array to user
      System.out.println(arrQuestion[i]); 
      // Stores user input into previously declared variable
      strAnswer = input.nextLine(); 
      // Compares user input to the answer stored in the array
      if (strAnswer.equalsIgnoreCase(arrAnswer[i])){
        intScore++; // Increments score by 1 
        System.out.println("Correct answer!"); // Displays that the answer is correct to the user
      }else { // If incorrect, will display that it is incorrect and show the correct answer
        System.out.println("Sorry, incorrect! The correct answer is " + arrAnswer[i] + "."); 
      } // End else
    } // End for loop
    
    // Display final score, and tell user that their info has been stored and how to access them again
    System.out.println("Your score is " + intScore + " out of 13. This has been stored in the database. Please use your username and password to access your scores next time."); 
    
    // Returns user score
    return intScore;
  } // End method    
  
  /**
   * This method stores the username, password, and score that the user receives.
   * 
   * @param strUsername This is the first parameter and it is the username
   * @param strPassword This is the second parameter and it is the password
   * @param intScore This is the second parameter and it is the score from the competency assessment/quiz
   * @return void This does not return anything.
   */
  
  public static void storeData (String strUsername, String strPassword, int intScore) {
    // Try/catch used for this code, since it may cause an exception to arise
    try {
      // Declare and set variable to username, password, and score, split w/ commas
      String strInfo = strUsername + ", " + strPassword + ", " + intScore; 
      
      // FileWriter/PrintWriter to append information to database.txt (txt file)
      FileWriter appendFile = new FileWriter ("database.txt", true);
      PrintWriter fileOutput = new PrintWriter (appendFile); 
      fileOutput.println(strInfo); 
      fileOutput.close(); 
    } catch (IOException ioException) { // Catches any exceptions
      // Shows that database.txt (txt file) does not exist, which is why there is a problem
      System.out.println("IO Exception, try again when file database.txt has already been made.");
    } // End catch
  } // End method
  
  /**
   * This method is the user login, and it makes sure the username and password are correct by using if statements/calling other methods.
   * If correct, it will display their score. 
   * 
   * @param input This is the first parameter and it is the scanner (User input)
   * @return void This does not return anything.
   */
  
  public static void UserLogin (Scanner input){
    // Declare variables
    String strUsername, strPassword, strScore; 
    
    // Do while loop to check if username exists in database (txt file) 
    do {
      // Prompts user to enter username
      System.out.println("Please enter your username: ");
      strUsername = input.nextLine(); // Get username from user input, and assign variable as inputted username
      
      // If statement checks if verifyExistingUser method returns true
      if (verifyExistingUser(strUsername) == true) {
        System.out.println("Sorry, username not found."); // Display to user that username cannot be found in txt file
      }
    } while (verifyExistingUser(strUsername)); // Will continue to repeat if username does not exist/returns true, so user can enter an existing username
    
    // Do while loop to check if password is correct alongside the previously inputted username
    do {
      // Prompts user to enter password
      System.out.println("Please enter your password: "); 
      strPassword = input.nextLine(); // Get password from user input, and assign variable as inputted password
      
      // If statement to check if the password for the previous username is incorrect
      if (verifyPassword(strUsername, strPassword) == false){
        System.out.println("Your password is incorrect."); // Displays incorrect password to user
      }
    } while (!verifyPassword(strUsername, strPassword)); // Will continue to repeat if incorrect password
    
    // Display to user that username and password is correct and they logged in 
    System.out.println("Your username and password is correct. Login successful."); 
    
    // Uses retrieveScore method to get the stored score based on their username
    strScore = retrieveScore(strUsername); 
    
    // Display score for user and thanks user for completing the assessment
    System.out.println("Your score is " + strScore + " out of 13. Thank you for doing the competency assessment, " + strUsername + "."); 
  } // End method
  
  /**
   * This method checks if the password is correct and corresponds w/ the username they entered in the database (txt file). 
   * 
   * @param strUsername This is the first parameter and it is the username
   * @param strPassword This is the second parameter and it is the password
   * @return boolean This returns the result of true if password is correct and corresponds w/ the username, false otherwise.
   */
  
  public static boolean verifyPassword (String strUsername, String strPassword) {
    // Try/catch used for this code, since it may cause an exception to arise
    try {
      // Scanner for the database.txt file
      Scanner fileInput = new Scanner (new File("database.txt")); 
      // While loop that continues for lines in file
      while (fileInput.hasNext()){
        String [] arrLine = fileInput.nextLine().split(","); // String array, will work with info in file being split with commas
        // If statement to check if valid password with inputting username
        if (strUsername.equals(arrLine[0].trim())){
          if (strPassword.equals(arrLine[1].trim())){
            fileInput.close();
            return true; // Returns true if password matches
          } // End second if statement
        } // End first if statement
      } // End while loop
      fileInput.close(); 
      return false; // Returns false if password does not match
      // Catching any exceptions to prevent program from crashing
    } catch (IOException ioException){
      // Display what is wrong, missing txt file
      System.out.println("IO Exception, try again when file database.txt has already been made.");
      // Exits program
      System.exit(0);
      // Blank return
      return false;
    } // End catch
  } // End method
  
  /**
   * This method retrieves the score that the user got when they first registered and completed the competency assessment/quiz.
   * 
   * @param strUsername This is the first parameter and it is the username
   * @return String This returns score that the user received during the competency assessment/quiz.
   */
  
  public static String retrieveScore (String strUsername) {
    // Try/catch used for this code, since it may cause an exception to arise
    try {
      // Scanner for the database.txt file
      Scanner fileInput = new Scanner(new File("database.txt")); 
      // While loop that continues for each line in file
      while (fileInput.hasNext()) {
        // Array to check the lines
        String [] arrLine = fileInput.nextLine().split(","); 
        // If statement to see if the username matches the information in the txt file
        if (strUsername.equals(arrLine[0].trim())){
          fileInput.close();
          return arrLine[2].trim(); // Return the score
        } // End if statement
      } // End while loop
      fileInput.close(); 
      return ""; 
    // Catching any exceptions to prevent program from crashing
    } catch (IOException ioException){
      // Display what is wrong, missing txt file
      System.out.println("IO Exception, try again when file database.txt has already been made.");
      // Exits program
      System.exit(0);
      // Blank return
      return "";
    } // End catch
  } // End method
} // End class