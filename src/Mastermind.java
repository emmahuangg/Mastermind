import java.util.Arrays; //to access array methods
import java.util.Collections;
import java.util.Scanner; //to collect user input
import java.util.Random; //random number generator
import java.util.stream.Collectors; //to sort array

public class Mastermind {

  public static int[] code = new int[4]; //declared outside of methods so that all methods can share these variables
  public static boolean won = false; //the player is assumed to have lost the game / not have won the game yet

  public static void main(String[] args) {
    intro();
  } //brings player to starting point

  public static void intro() { //starting point of the game
    Scanner sc = new Scanner(System.in); //later collects user input

    //introductory message
    System.out.println("Welcome to Mastermind.\n" +
      "Please enter Yes to view game rules, No to skip.");

    //collect user input on whether game rules should be displayed
    String gameRules = sc.nextLine();
    switch (gameRules.toLowerCase()) { //find out what the user has inputted
      case "yes": case "y":
        gameRules(); //method is split out for clarity
        break;
      case "no": case "n":
        System.out.println("Skipping game rules!");
        break;
      default:
        System.out.println("I'll assume that means a no."); //unexpected input, assume no
    }

    startGame(); //after game rules are displayed/skipped, actual game begins
  }

  public static void gameRules() {
    System.out.println(); //line break for clarity
    //the following are Mastermind game rules, modified to fit the player vs. program situation
    System.out.println("Mastermind Game Rules:\n" +
      "• I will be the codemaker, and you will be the codebreaker.\n" +
      "• I will randomly chooses a pattern of four integers from 1-8, allowing duplicates.\n" +
      "• You, the codebreaker, will try to guess the pattern, in both order and integer value, within ten turns. \n" +
      "• Each guess is made by you entering four integers, such as 1234. \n" +
      "• Once you've made your guess, I will provide feedback by giving you a set of x's and o's. \n" +
      "• An x represents that one of your guessed integers is in the proper position AND integer value.\n" +
      "• An o represents that one of your guessed integers is the correct integer but placed in the wrong position.\n" +
      "• Note that the order of x's and o's does not relate to your guess. \n" +
      "• If there are duplicate integers in the guess, they cannot all be awarded an o unless they correspond to the same number of duplicate integers in the hidden code.\n" +
      "• Once feedback is provided, you'll make another guess; guesses and feedback continue to alternate until either you've guessed all integers correctly, or you've run out of your 10 tries.\n" +
      "Ready to start?\n" +
      "Let's go!");
  }

  public static void startGame() {
    won = false; //every time the game restarts, the player is assumed to not have won the game yet
    Scanner sc = new Scanner(System.in);
    System.out.println(); //empty line for clarity

    //the following segment generates the 4-integer secret code
    System.out.println("Generating the code...");
    Random rand = new Random(); //create a random instance
    for (int i=0; i<4; i++) { //repeat 4 times
      code[i] = (rand.nextInt(8) + 1); //choose one random integer from a range of 1-8 (a range of 8 moved up by 1)
    }

    //all "important" messages will be highlighted through surrounding by ---------- ---------- to improve clarity
    System.out.println("Code generated. The code is:\n" +
      "----------  ? ? ? ?  ----------");

    //the following segment provides the player with 10 tries to guess the code
    for (int i=1; i<=10; i++) {
      if (won) { //if the player has already guessed the code, the loop discontinues
        break;
      } else { //otherwise, move onto a new guess
        guess(i); //pass i into guess method
      }
    }

    //at this point, the player has either won or gone through all 10 tries.
    if (won) { //if the player has won the game
      System.out.println("Congratulations. Would you like to play again?"); //congratulatory message for winners
      String playAgain = sc.nextLine();
      //repeat back to starting point of game
      //game ends
      switch (playAgain.toLowerCase()) {
        case "yes":
        case "y":
          intro();
          break;
        case "no":
        case "n":
          System.out.println("Bye!");
          break;
        default:
          System.out.println("Bye!");
          break;
      }
    } else {
      //the segment below informs the player of the correct code, which they failed to guess within 10 tries.
      System.out.println("You've made 10 incorrect attempts. The correct code was: \n" +
        "----------  "+Arrays.stream(code)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "))+"  ----------");
      System.out.println("Unfortunately, you lost. Would you like to play again?");
      String playAgain = sc.nextLine();
      switch (playAgain.toLowerCase()) {
        case "yes":
        case "y":
          intro();
          break;
        case "no":
        case "n":
          System.out.println("Bye!");
          break;
        default:
          System.out.println("Bye!");
          break;
      }
    }
  }

  public static void guess(int i) { //this method asks for user's guesses and evaluates them.
    Scanner sc = new Scanner(System.in);
    System.out.println("Guess #" + String.valueOf(i)); //informing user of their current number of tries
    int[] guessArray = new int[4]; //an array that will store the user's guesses

    do { //repeat this at least once
      System.out.println("Please enter 4 integers from 1-8 corresponding to your guess. E.g. 1234"); //prompt for user input
      String guessInput = sc.nextLine();
      String guess = guessInput.trim().replaceAll(" ", ""); //first, any spaces are removed

      //the segment below verifies the user's input
      if (guess.length() != 4) { //evaluates whether there are 4 separate values
        System.out.println("You don't seem to have entered 4 entries! Please try again."); //the player has not entered 4 things
      } else {
        int countValid = 0; //a variable that will count the number of valid integers entered by the user
        //since all integers are single digit numbers, the user input should be formatted like "x x x x"
        for (int x=0; x<4; x++) { //loop through every item
          try { //a try-catch in case the user input fails the parseInt
            int temp = Integer.parseInt(guess.substring(x,x+1)); //a temporary variable to hold each number from the user input
            if (temp >= 1 && temp <= 8) { //if the number is within the valid range
              guessArray[countValid] = temp; //the number is added into guessArray
              countValid ++ ; //the number was a valid integer, countValid counter increases by one
              //the loop continues to check other parts of the user input
            } else {
              System.out.println("The numbers you've entered are out of the range 1-8. Please try again."); //the number is out of range -> invalid
              break; //there has already been an invalid number, so there is no point in checking the rest
            }
          } catch (Exception e) { //there was an exception. likely, the user entered a non-integer.
            System.out.println("You don't seem to have entered 4 valid numbers! Please try again.");
            break; //there has already been an invalid entry, so there is no point in checking the rest
          }
        }
        if (countValid == 4) { //if all four entries are valid integers within range
            break; //breaks out of the do loop; the user has finished placing a valid guess.
        }
      }
    } while (true);

    //reiterating user guess for clarity
    System.out.print("You've guessed:                   |  My feedback for your guess is... \n" +
      "----------  " + Arrays.stream(guessArray) //prints user's guess
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" ")) + "  ----------   |  ");

    //we will use the feedback() method to find out what feedback we should give the user
    String feedback = provideFeedback(guessArray);
    System.out.println(feedback); //print out the feedback
    if (feedback.equals("x x x x ")) { //if all four entries were correct
      System.out.println("You Won!"); //the player has won!
      won = true; //sets won to true to communicate the player's winning state with the rest of intro()
    }
  }

  public static String provideFeedback(int[] guessArray) { //takes in guessArray as a parameter
    String[] feedback = new String[4]; //create a string array to store feedback, as arrays are easier to sort than plain strings
    int[] temporaryCodeClone = Arrays.copyOf(code, 4); //create a temporary clone for the code to avoid messing up the secret code itself

    for (int x=0; x<4; x++) { //repeat 4 times
      if (guessArray[x] == temporaryCodeClone[x]) { //this indicates that both the position and integer is correct at that index
        feedback[x] = "x"; //provide "x", indicating a completely correct entry
        temporaryCodeClone[x] = 0; //remove this digit from the secret code, since we do not want to award duplicate x's and o's for the same digit
        guessArray[x] = -1; //also remove this digit from the user's guess, for the same reasons as above
      }
    }
    /*the following is not included as an "else" to the if loop above because we want to check over all the user's guesses first.
    for example, if the secret code is x 1 1 x, and the user has guessed 1 1 x x.
    the first 1 that the user guessed is awarded only an o  as it does not match the position of either 1s.
    but if the o is awarded, the first 1 from the secret code would be removed, which prevents the second guessed 1 from getting the x it deserves.
    hence, these two loops must be broken off.
    */
    for (int x=0; x<4; x++) {
        for (int y = 0; y < 4; y++) { //another inner loop of 4
          if (guessArray[x] == temporaryCodeClone[y]) { //checks if the integer the player guessed matches with ANY digit in the secret code (except those already matched)
            feedback[x] = "o"; //awards an o if there's an entry with correct integer, incorrect positioning
            temporaryCodeClone[y] = 0; //remove this digit from the secret code as well
            break; //if the entry has already been matched successfully, it should not be compared any longer as that could create duplicate feedback
          }
        }
    }

    //the following segment adds spaces to the feedback so that all the ---------- formatting match up neatly & clearly
    String feedbackStr = ""; //this will hold the feedback after it is converted into a string
    for (int x=0; x<4; x++) { //repeat 4 times
      if (feedback[x] == null) { //if there is no feedback at that index——the player has entered something that has incorrect position AND integer
        feedback[x] = " ";
      }
    }
    Arrays.sort(feedback, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER)); //sort the feedback to prevent giving away clues about positioning of x's and o's
    for (int x=0; x<4; x++) {
      feedbackStr = feedbackStr.concat(feedback[x] + " "); //add a space after each x & o for clarity
    }
    return feedbackStr; //return the string back to the guess method
  }
}
