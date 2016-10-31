/*Florian Heringa
* 10385835
*
*	Opgave2.java
* 
* Dit programma verwerkt een zin die de gebruiken invoert via de commandline
* of vanaf een tekstfile. Eerst wordt de zin verwerkt en de karakters geteld
* van zowel de input als de gefilterde versie.
* Vervolgens wordt gekeken of de zin een palindroom is.
* Een palindroom is een woord of zin, welke omgekeerd hetzelfde is, bijvoorbeeld
* het woord 'lepel' of 'parterretrap'.
* Ten slotte wordt er een grafiek geprint die geschaald laat zien hoe vaak alle
* letters voorkomen in de gefilterde input. Ook wordt de waarde van elk sterretje
* gegeven als 'binsize', zodat eenvoudig gekeken kan worden hoe vaak elk karakter
* voor kwam in de input.
*/

import java.util.*;


public class SortedFilterZin {

	/*Initializing constants*/
	static int A_LOWER = 97;
	static int Z_LOWER = 122;
	static int A_UPPER = 65;
	static int Z_UPPER = 90;
	static int ZERO_UNICODE = 48;
	static int NINE_UNICODE = 57;

	public static void main(String[] args) {

		String zin = getInput();

		printAllInfo(zin);
	}

	/*Alle input verwerking*/
	/**/

/*Input: none
*Verwerkt user input, haalt spaties van begin en einde
*Output: De ingevoerde string*/
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

		char[] charArray = zin.toCharArray();

		for (char c : charArray) {
			if (c == 0) {
				System.out.println("Voer een regel in.");
				System.exit(0);
			}
		}

		return zin;
	}


	/*Alle String manipulaties*/
	/**/

/*Input: String (ongefilterd)
*Stopt de inputstring in een array van karakters
*maakt van alle hoofdletters kleine letters en van leestekens
*spaties. Maakt vervolgens van de array weer een string.
*Output: Verwerkte string*/
	static String filterZin (String zin) {

		char[] charArray = zin.toCharArray();
		String returnString = "";

		/*Loopt door de inputstring, laat de kleine letters en getallen met rust
		*vervangt hoofdletters door kleine letters (kleine letters en hoofdletters
		*verschillen 32 van elkaar in unicode) en verandert alle andere tekens
		*in spaties (unicode 32)*/
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

		/*Maakt van de array weer een string om terug te geven aan de caller*/
		for (char c : charArray) {
				returnString += c;
		}
		return returnString;
	}

/*Input: String (gefilterd)
*Maakt van de input een array van karakters en haalt de spaties er uit.
*Output: String zonder spaties*/
	static String noSpacesString (String input) {

		String returnString = "";
		char[] charArray = input.toCharArray();

		/*Loopt door de array van een gefilterde zin en plaatst alles wat geen
		*spatie is terug in de returnstring*/
		for (char c : charArray) {
			if (c != 32) {
				returnString += c;
			}
		}
		return returnString;
	}

/*Input: String (gefilterd)
*Kijkt of de ingevoerde zin een Palindroom is
*Output: Boolean*/
	static Boolean checkIfPalindrome (String input) {

		char[] charArray = input.toCharArray();
		int arrayLength = charArray.length;
		Boolean returnBoolean = true;

		/*Kijkt tot de helft van de array, in paren, of de karakters gelijk zijn
		*als karakters niet meer gelijk zijn wordt de loop afgebroken*/
		for (int i = 0 ; i < arrayLength / 2 ; i++) {
			if (charArray[i] != charArray[arrayLength - 1 - i]) {
				returnBoolean = false;
				break;
			}
		}
		return returnBoolean;
	}


/*Input: String (beide), Boolean
*Telt het aantal hoofdletters, kleine letters en getallen in de
*ingevoerde string als deze gefilterd is.
*Telt alle karakters als deze niet gefilterd is.
*Output: Integer*/
	static int getCharactersAmount (String input, Boolean filtered) {

		int count = 0;
		char[] inputArray = input.toCharArray();

		/*Verschillende loops voor gefilterde en ongefilterde zinnen
		*loopt door de zinnen en geeft het aantal karakters die geen spaties
		*zijn.*/
		if (filtered) {
			for (char c : inputArray) {
				if (c <= Z_LOWER && c >= A_LOWER ||
					c <= Z_UPPER && c >= A_UPPER ||
					c <= NINE_UNICODE && c >= ZERO_UNICODE)
					count += 1;	
			}
		}
		else {
			for (char c : inputArray) {
				if (c != 32)
					count += 1;	
			}
		}
		return count;
	}

/*Input: String (gefilterd)
*Telt het aantal kleine letters en getallen en stopt deze in een array
*Output: Array van frequenties*/
	static int[] toFrequencyArray (String input) {

		int inputLength = input.length();
		char[] inputArray = input.toCharArray();
		int[] frequency = new int[36];

		/*Vult de integer-array met de frequentie van elk getal*/	
		for (char c : inputArray) {
			/*a-z in array voor index 0-25*/
			if (c <= Z_LOWER && c >= A_LOWER) {
				frequency[c-97] += 1;
			}
			/*0-9 in array voor index 26-36*/
			else if (c <= NINE_UNICODE && c >= ZERO_UNICODE) {
				frequency[c-22] += 1;
			}
		}
		return frequency;
	}

	/*Methoden om de grafiek te maken*/
	/**/

/*Input: String (gefilterd)
*Maakt een tweedimensionaal Array van de relatieve frequenties van
*de karakters (a-z, 0-9) van de ingevoerde string.
*Output: Tweedimensionaal array*/
	static char[][] twoDimensionalArray(String input, int[] absoluteFrequency) {

		char[][] arrayOut = new char[10][36];
		double lowerBound = 1.0;
		int dataMin = findMin(absoluteFrequency);
		int dataMax = findMax(absoluteFrequency);
		double binSize = (dataMax / dataMin) / 10.0;

		/*Vult de array vanaf linksonder [9,0], per rij naar boven
		*dit is zodat de grafiek juist geprint wordt later*/
		for (int i = 0 ; i < 36 ; i++) {
			for (int j = 9 ; j >= 0 ; j--) {
				/*absoluteFrequency * 10 scheelt enkele conversiestappen*/
				/* '*' als het getal vaak genoeg voorkomt, anders een spatie*/
				if (absoluteFrequency[i] > lowerBound) {
					arrayOut[j][i] = 42;
				}
				else {
					arrayOut[j][i] = 32;
				}
				lowerBound += binSize;	
			}	
			lowerBound = 0;
		}
		return arrayOut;
	}

/*Vind het minimum en maximum van een array van integers.*/
	static int findMax (int[] input) {

		int max = 0;

		for (int i : input) {
			if (i > max) 
				max = i;
		}
		return max;
	}

	static int findMin (int[] input) {

		int min = findMax(input);

		for (int i : input) {
			if (i < min  && i != 0) 
				min = i;
		}
		return min;
	}

/*Berekent de grootte van de verticale bins voor een verdeling
*van 10 bins.*/
	static double binSize (int[] absoluteFrequency) {
		int dataMin = findMin(absoluteFrequency);
		int dataMax = findMax(absoluteFrequency);
		double binSize = (dataMax / dataMin) / 10.0;

		return binSize;
	}

	/*Alle output en print methoden*/
	/**/

/*Input: String
*Verwerkt alle data, stopt het in variabelen en geeft het door aan de printfuncties
*Output: Print alle data op het scherm*/
	static void printAllInfo (String zin) {

		String filteredZin = filterZin(zin);
		String noSpaces = noSpacesString(filteredZin);
		int zinLength = getCharactersAmount(zin, false);
		int filteredLength = getCharactersAmount(filteredZin, true);
		Boolean palindrome = checkIfPalindrome(noSpaces);
		int[] absoluteFrequency = toFrequencyArray(filteredZin);
		char[][] histogram = twoDimensionalArray(filteredZin, absoluteFrequency);

		printData (zin, zinLength, filteredZin, filteredLength, palindrome);
		printGraph (histogram, filteredLength, absoluteFrequency);
	}

/*Input: 2-D array (unicode geencodeerde grafiek), 
*		 integer (lengte van de gefilterde zin).
*Print de array als grafiek op het scherm.*/
	static void printGraph (char[][] histogram, int filteredLength,
						    int[] absoluteFrequency) {

		/*Print de gemaakte array per rij op het scherm*/
		for (int i = 0 ; i < 10 ; i++) {
			for (int j = 0 ; j < 36 ; j++) {
				System.out.print(histogram[i][j] + " ");
			}
			System.out.print("\n\n");
		}
		printAxis();

		System.out.printf("De binsize is: %.2f\n", (binSize(absoluteFrequency)));
	}

/*Print de x-as van de grafiek*/
	static void printAxis (){
		/*Print de karakters a tot z op een rij*/
		for (int i = 0 ; i < 26 ; i++) {
			char c = (char)(i + A_LOWER);
			System.out.print(c + " ");
		}
		/*Print de getallen 0 tot 9 op een rij*/
		for (int i = 0 ; i < 10 ; i++) {
			char c = (char)(i + ZERO_UNICODE);
			System.out.print(c + " ");
		}
		System.out.print("\n");
	}

	static void printData (String zin, int zinLength,
						   String filteredZin, int filteredLength,
						   Boolean palindrome) {

		/*Als de zin langer is dan 20 karakters, print alleen de eerste 20*/
		if (zinLength > 20) {
			System.out.printf("De ongefilterde zin is: %.20s ...\n", zin);
		}
		else {
			System.out.printf("De ongefilterde zin is: %s\n", zin);
		}
		System.out.printf("Met %d karakters.\n\n", zinLength);

		if (filteredLength > 20) {
		System.out.printf("De gefilterde zin is: %.20s ...\n", filteredZin);
		}
		else {
		System.out.printf("De gefilterde zin is: %s\n", filteredZin);
		}	
		System.out.printf("Met %d karakters.\n\n", filteredLength);
		System.out.printf("Is palindroom: %b\n\n", palindrome);
	}
}