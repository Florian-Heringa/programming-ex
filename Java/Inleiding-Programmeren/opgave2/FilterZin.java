/*
*
*
*/

import java.util.*;


public class FilterZin {

	/*Initializing constants*/
	static int A_LOWER = 97;
	static int Z_LOWER = 122;
	static int A_UPPER = 65;
	static int Z_UPPER = 90;
	static int ZERO_UNICODE = 48;
	static int NINE_UNICODE = 57;

	public static void main(String[] args) {
		
		String zin = "01234567891234567899876543219876543210";//getInput();
		String filteredZin = filterZin(zin);
		String noSpaces = noSpacesString(zin);
		int zinLength = getCharactersAmount(zin);
		int filteredLength = getCharactersAmount(filteredZin);
		Boolean palindrome = checkIfPalindrome(noSpaces);
		char[][] histogram = twoDimensionalArray(filteredZin);

		/*if length > 20 print only first part use %.20d*/
		System.out.printf("De ongefilterde zin is: %.20s ...\n", zin);
		System.out.printf("Met %d karakters.\n", zinLength);		
		System.out.printf("De gefilterde zin is: %.20s ...\n", filteredZin);
		System.out.printf("Met %d karakters.\n", filteredLength);
		System.out.printf("Is palindroom: %b\n", palindrome);

		int[] freqArray = toFrequencyArray(filteredZin);
		int[] relFreqAr = relativeFrequencyPercentage(filteredZin);

		for (int i = 0 ; i < 10 ; i++) {
			for (int j = 0 ; j < 36 ; j++) {
				System.out.print(histogram[i][j] + " ");
			}
			System.out.print("\n\n");
		}
		printLegend();
	}

/*Takes the user input and returns that value as a string*/
	static String getInput () {
		Scanner scan = new Scanner(System.in);
		String zin = "";

		System.out.print("Voer een zin in:");
		if (scan.hasNextLine()) {
			zin = scan.nextLine().trim();
		}

		if (zin.equals("")) {
			System.out.println("Geen invoer!");
			System.exit(0);
		}

		return zin;
	}

	static void printLegend (){
		for (int i = 0 ; i < 26 ; i++) {
			char c = (char)(i + A_LOWER);
			System.out.print(c + " ");
		}
		for (int i = 0 ; i < 10 ; i++) {
			char c = (char)(i + ZERO_UNICODE);
			System.out.print(c + " ");
		}
		System.out.print("\n");
	}

/*Takes a string as input and returns that string in only lowercase,
*numbers and spaces*/
	static String filterZin (String zin) {
		char[] charArray = zin.toCharArray();
		String returnString = "";

		for (int i = 0 ; i < zin.length() ; i++) {

			if (charArray[i] <= Z_UPPER && charArray[i] >= A_UPPER) {
				charArray[i] += 32;
			}
			else if (charArray[i] <= Z_LOWER && charArray[i] >= A_LOWER) {
				continue;
			}
			else if (charArray[i] <= NINE_UNICODE && charArray[i] >= ZERO_UNICODE) {
				continue;
			}
			else {
				charArray[i] = 32;
			}
		}

		for (char c : charArray) {
			if (c <= Z_LOWER && c >= A_LOWER ||
			c <= NINE_UNICODE && c >= ZERO_UNICODE ||
			c == 32) {
				returnString += c;
			}
		}

		return returnString;
	}

/*Input is een String met alleen lowercase karakters, getallen en spaties,
*en verwijdert de spaties*/
	static String noSpacesString (String input) {

		String returnString = "";
		char[] charArray = input.toCharArray();

		for (char c : charArray) {
			if (c != 32) {
				returnString += c;
			}
		}

		return returnString;
	}


/*Input is een String met alleen getallen en lowercase characters en kijkt 
*of het een palindroom is*/
	static Boolean checkIfPalindrome (String input) {

		char[] charArray = input.toCharArray();
		int arrayLength = charArray.length;
		Boolean returnBoolean = true;

		for (int i = 0 ; i < arrayLength / 2 ; i++) {
			if (charArray[i] != charArray[arrayLength - 1 - i]) {
				returnBoolean = false;
				break;
			}
		}

		return returnBoolean;
	}

/*Invoer is een String en geeft als uitvoer het aantal lowercase- en uppercase-characters
*en getaller*/
	static int getCharactersAmount (String input) {
		int count = 0;
		char[] inputArray = input.toCharArray();

		for (char c : inputArray) {
			if (c <= Z_LOWER && c >= A_LOWER ||
				c <= Z_UPPER && c >= A_UPPER ||
				c <= NINE_UNICODE && c >= ZERO_UNICODE)
				count += 1;	
		}		
	
		return count;
	}


/* takes a filtered string (with only lowercase, numbers and spaces)
*and creates an array with the frequency of lowercase characters,
*numbers and spaces*/
	static int[] toFrequencyArray (String input) {
		int inputLength = input.length();
		char[] inputArray = input.toCharArray();
		int[] frequency = new int[36];

/*Loops through the inputArray and adds 1 for every character to the
*frequency array*/	
		for (char c : inputArray) {
			/*a-z in array at index 0-25*/
			if (c <= Z_LOWER && c >= A_LOWER) {
				frequency[c-97] += 1;
			}
			/*0-9 in array at index 26-36*/
			else if (c <= NINE_UNICODE && c >= ZERO_UNICODE) {
				frequency[c-22] += 1;
			}
		}
		return frequency;
	}

/*Neemt als input een string en kijkt wat het percentage per karakter is wat
*er in voor komt, het resultaat wordt met 1000 vermenigvuldigd en afgerond om
*zo met integers door te kunnen rekenen*/
	static int[] relativeFrequencyPercentage (String input) {

		int[] absoluteFrequency = toFrequencyArray(input);
		double totalChars = getCharactersAmount(input);
		int[] relativeFrequency = new int[36];

		for (int i = 0 ; i < 36 ; i++) {
			relativeFrequency[i] = (int)((absoluteFrequency[i] / totalChars) * 1000);			
		}

		return relativeFrequency;
	}

	static char[][] twoDimensionalArray(String input) {

		char[][] arrayOut = new char[10][36];
		int lowerBound = 1;
		int[] relativeFrequency = relativeFrequencyPercentage(input);

		for (int i = 0 ; i < 36 ; i++) {
			for (int j = 9 ; j >= 0 ; j--) {
				if (relativeFrequency[i] > lowerBound) {
					arrayOut[j][i] = 42;
				}
				else {
					arrayOut[j][i] = 126;
				}
				lowerBound += 100;	
			}	
			lowerBound = 0;
		}
		return arrayOut;
	}


}
