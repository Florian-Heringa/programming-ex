/* * DNAMatch v0.1 *
 * author     :  Dr. Quakerjack. 
 * revised by :  Florian Heringa
 * date       :  17-09-2015  / 26-09-2015
 * version    :  0.1 
 */

/* This program is designed to manipulate databases of 6-letter
 * DNA-codes, consisting of the letters A, C, G and T.
 * For comparison, the Levenshtein-algorithm is used en sorting
 * while comparing is handled by the Bubblesort-algorithm.
 * The program only accepts correct sequences into the database,
 * while comparing outside the database accepts all input.
 * Also, the current database can be save to a text file with the
 * 'save' - command, and specifying a filname.
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.io.*;

public class Opgave4 {   

	/* RAM DB during execution*/ 
	public static ArrayList<String> database = new ArrayList<String>();

	public static void main (String[] args) {

		System.out.println("Welcome to DNA Matcher v0.1\n");
		System.out.println("Enter 'help' for a list of commmands\n");

		callUI();
	}

	/*Scans the input from the user*/
	public static String readInput() {

		Scanner input = new Scanner(System.in);
		String returnStr = "";

		if(input.hasNextLine()) {
			returnStr = input.nextLine();
	    }

	    return returnStr; 
	}

	/*Logs the helptext to the screen*/
	public static void helpuser() {      
		System.out.println("COMMANDS...\n");
		System.out.println("\t list \t\t\t print database");
		System.out.println("\t add ... \t\t add to database");
		System.out.println("\t compare ... ... \t compare two strings");
		System.out.println("\t retrieve ... \t\t find in database");
		System.out.println("\t remove ... \t\t remove from database");
		System.out.println("\t populate n \t\t fills the database with n random elements");
		System.out.println("\t\t\t\t (max. 100)");
		System.out.println("\t clearall \t\t clears the entire database");
		System.out.println("\t save ____ \t\t save current database to a txt file");
		System.out.println("\t quit \t\t\t stop");
		System.out.println("\t help \t\t\t print this text\n");
		System.out.println("\t '...' is a 6 letter DNA-sequence using the");
		System.out.println("\t\tletters 'a', 'c', 'g' en 't'.");
		System.out.println("\t n is an integer, designating the number of"); 
		System.out.println("\t\telements to fill the database with");
		System.out.println("\t ____ specifies the filename");
	} 

	/*Database access methods:*/

	/*Loads all elements of the database to the screen*/
	public static void loadStringFromDatabase() {
	    for(String s : database) {
			System.out.println(s);
	    }
	}    

		/*Adds a string to the database*/
	public static void addStringToDatabase(String i, Boolean print) {

		/*Only accept strings with length 6 as valid input*/
		if (i.length() == 6) {
			String upper = i.toUpperCase();
			/*Checks if the string is a correct DNA-sequence*/
	    	for (char c : upper.toCharArray()) {
	    		if (c != 65 && c != 67 && c != 71 && c != 84) {
	    			System.out.println("Incorrect sequence....");
	    			return;
	    		} 
	    	}
	    	database.add(upper);
	    	if (print) {
	    		System.out.println("added: " + i);
	    	}
	    } 
	    else {
	    	System.out.println("String not of correct length....");
	    }
	}

	/*Vult de database met n random elementen*/
	private static void populateDatabase (int numElements) {

		Random randomGen = new Random();
		String sequence = "";

		for (int i = 0 ; i < numElements ; i++) {
			for (int j = 0 ; j < 6 ; j++) {
				int randomNr = randomGen.nextInt(100);
				if (randomNr <= 24) {
					sequence += "A";
				} else if (24 < randomNr && randomNr <= 49) {
					sequence += "C";
				} else if (49 < randomNr && randomNr <= 74) {
					sequence += "G";
				} else {
					sequence += "T";
				}	
			}
			addStringToDatabase(sequence, false);
			sequence = "";
		}
	}
	
	/*Verwijdert de opgegeven string uit de database*/
	public static void removeFromDatabase(String i) {
		try {
			String iUpper = i.toUpperCase();
			int tempIndex = database.indexOf(iUpper);
			database.remove(database.indexOf(iUpper));
			System.out.println("removed " + iUpper +"from index " + tempIndex);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("not found in database....");
		}
	}

	/*Verwijdert string op index i uit de database*/
	public static void removeFromDatabase(int i, boolean print) {
		try {
			String tempStr = database.get(i);
			database.remove(i);
			if (print) {
				System.out.println("removed " + tempStr + " from index " + i);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Index out of bounds....");
		}
	}

	public static void compareStrings(String a, String aa) {
		System.out.println("Difference = " + mss(a, aa, true));
	}

	/* Using Levenshtein algorithm. 
	 * 					/ max(i,j) 			if min(i,j) = 0
	 * lev_{a,b}(i,j) = |     / lev_{a,b}(i-1,j) + 1			\
	 * 	                | min | lev_{a,b}(i,j-1) + 1			| otherwise
	 *					\     \ lev_{a,b}(i-1,j-1) + 1_{a!=b}	/
	 * Places the two strings in the first row and column of a matrix, then populates 
	 * the matrix according to wether the characters of the string are different.
	 */        
	public static int mss(String source, String target, boolean print) {

		int[][] levenshteinMatrix = new int[source.length() + 1][target.length() + 1];

	    for(int i = 0 ; i <= source.length() ; i++) {
	    	levenshteinMatrix[i][0] = i;
	    }
		for(int j = 0; j <= target.length() ; j++) {
			levenshteinMatrix[0][j] = j;
		}        

		for(int j = 1 ; j <= target.length() ; j++) {

			for(int i = 1 ; i <= source.length() ; i++) {

				if(source.charAt(i-1) == target.charAt(j-1)) {
					levenshteinMatrix[i][j] = levenshteinMatrix[i-1][j-1];
				}
				else {                    
					levenshteinMatrix[i][j] = min_3 (levenshteinMatrix[i-1][j] + 1,
										levenshteinMatrix[i][j-1] + 1, 
										levenshteinMatrix[i-1][j-1] + 1);
					}        
				} 
			}        

			if(print) {
				for(int l1 = 0; l1 <= source.length(); l1++) {
					for(int l2 = 0; l2 <= target.length(); l2++) {
						System.out.print(levenshteinMatrix[l1][l2] + "  ");
					}
				System.out.println("\n");
				}        
			}        

		return levenshteinMatrix[source.length()][target.length()];
	}    

	/*Return lowest integer using ternary operations */    
	private static int min_3(int a, int b, int c) {
	    return (a < b) ? (a < c ? a : c) : (b < c ? b : c);
	}

	/* Finds the three best matches in the database by sorting
	 * according to Levensteihn distance using the bubble sort
	 * algorithm.
	 */   
	public static void getFromDatabase(String i) {

	    String[] sq = new String[database.size()];
		database.toArray(sq);        
		int r = 0;
		int[] differenceStrings = new int[sq.length];
		String iUpper = i.toUpperCase();

		for (String s : database) {
			differenceStrings[r++] = mss(iUpper, s, false); 
		}

		/*Using Bubble Sort algorithm*/
		for (int x = 0 ; x < (sq.length - 1) ; x++) {
			for (int y = 0 ; y < (sq.length - x - 1) ; y++) {
				if (differenceStrings[y] > differenceStrings[y + 1]) {

					int t1 = differenceStrings[y];
					differenceStrings[y] = differenceStrings[y + 1];
					differenceStrings[y + 1] = t1;

					String t2 = sq[y];
					sq[y] = sq[y + 1];
					sq[y + 1] = t2;                
				}
			}        
		}

		/*Show the three best matches*/
		System.out.println("Best matches: ");
		for (r = 0; r < Math.min(3, sq.length); r++) {
			System.out.println(differenceStrings[r] + "\t" + sq[r]);      
		}    
	}

	private static void saveDatabase(String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter((filename + ".txt")));
				for (String s : database) {
					out.write(s + "\n");
				}
			out.close();
		} catch (IOException e) {
			System.out.println("Error...");
		}
	}

	/*User interface, including error-handling.*/
	public static void callUI() {

		boolean quit = false;  

		do {
			System.out.print("console> ");
			String userInput = readInput();
			String[] stringArray = userInput.split(" ");
			String command = stringArray[0].toUpperCase();

			if(command.equals("HELP")) {
				System.out.println();
				helpuser();            
				System.out.println();
			}
			else if(command.equals("QUIT")) {
				quit = true;
				System.out.println("QUITTING....\n");
			}
			else if(command.equals("POPULATE")) {
				try {
					int numElements = Integer.parseInt(stringArray[1]);
					if (numElements > 100) {
						numElements = 100;
					}
					populateDatabase(numElements);
					System.out.println("Added " + numElements + " to database....\n");
				} catch (NumberFormatException e1) {
					System.out.println("please enter an integer 'n'");
					System.out.println();
				} catch (ArrayIndexOutOfBoundsException e2) {
					System.out.println("please enter an integer 'n'");
					System.out.println();
				}
			}
			else if(command.equals("CLEARALL")) {
				int databaseSize = database.size();
				for (int i = 0 ; i < databaseSize; i++) {
					removeFromDatabase(0, false);
				}
				System.out.println("cleared database....\n");
			}
			else if(command.equals("SAVE")) {
				try {
					saveDatabase(stringArray[1]);
					System.out.printf("saving database to %s.txt....\n\n", stringArray[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("please specify the filename ____");
				}         
			}
			else if(command.equals("LIST")) {
				loadStringFromDatabase();
				System.out.println();           
			}
			else if(command.equals("ADD")) {
				try {
					addStringToDatabase(stringArray[1], true);
					System.out.println();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("nothing entered to add....");
					System.out.println();
				}
			} 
			else if(command.equals("REMOVE")) {
				try {
					int index = Integer.parseInt(stringArray[1]);
					removeFromDatabase(index, true);
				} catch (NumberFormatException e) {
					removeFromDatabase(stringArray[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("nothing entered to remove....");
				}
				System.out.println();
			} 
			else if(command.equals("COMPARE")) {
				try {
					compareStrings(stringArray[1], stringArray[2]);
					System.out.println();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("not enough parameters entered....");
					System.out.println();
				}
			} 
			else if(command.equals("RETRIEVE")) {
				try {
					getFromDatabase(stringArray[1]);
					System.out.println();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("nothing entered to retrieve....");
					System.out.println();
				}    
			}
			else {
				System.out.println("\tSkipping...");
			}
		} while(quit == false);  
	}
}


