/* Florian Heringa */
/*     10385835    */

/* Deze klasse maakt de UI voor een programma voor het bekijken
 * van een geboortedatum. Van deze datum wordt berekend in welke
 * jaren deze op een zondag valt. Ook wordt de volledige datum
 * gegeven in de vorm 'maandag 1 januari, 2000', en wordt
 * het weeknummer berekend.
 * Vervolgens wordt gevraagd om een tweede datum en wordt
 * berekend hoeveel jaren, maanden, weken en dagen tussen de twee
 * data zitten. Tenzij de gebruiker 'quit' invoert, blijft dit herhalen.
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Opgave7 {


	public static void main (String[] args) {

		makeUI();
	}

	/* Verkrijgt de input van de user. */
	public static String readInput() {

		Scanner input = new Scanner(System.in);
		String returnStr = "";

		if(input.hasNextLine()) {
			returnStr = input.nextLine();
	    }

	    return returnStr; 
	}

	/* Maakt de user interface, en bevat error handling. */
	private static void makeUI () {

		boolean quit = false;
		String q = "quit";
		Datum inputDatum = null;
		Datum tweedeInputDatum = null;
		String datum = null;
		String tweedeDatum = null;
		String intervalTemp = null;
		Interval interval = null;
		ArrayList<Integer> zondagen = null;
		int[] difference = new int[4];

		Jaar test = new Jaar(2015);

		do {
			System.out.print("Voer 'quit' in om te stoppen: \n");
			System.out.print("Geef een datum (dd-mm-jjjj): ");
			datum = readInput();
			
			/* Probeer of de user input een correcte datum is. */
			try {
				inputDatum = new Datum(datum);				
				System.out.print("Geef een interval (jjjj-jjjj): ");
				intervalTemp = readInput();
				
				/* Probeer of de user input een correct interval is. */
				try {
					interval = new Interval(intervalTemp);
					System.out.println("\n");

					/* Probeert de jaren te vinden waarop de datum op een zondag viel. */
					try {
						zondagen = findIf(inputDatum, interval, "zondag");
						System.out.printf("%s is een zondag in de volgende jaren: \n",
										  inputDatum.toString(false, true));
						for (int i : zondagen) {
							System.out.printf("%d, ", i);
						}

						System.out.println("\n");
						System.out.printf("De volledige datum is: %s\n", inputDatum.toString(true, true));
						System.out.println();
						System.out.printf("Het weeknummer is: %d\n", inputDatum.weekNumber(1,0));
						System.out.println();

						System.out.print("Geef een tweede datum (dd-mm-jjjj): ");
						tweedeDatum = readInput();

						/* Probeert of de ingevoerde datum een correcte datum is. */
						try {
							tweedeInputDatum = new Datum(tweedeDatum);

							/* Probeert of het verschil gevonden kan worden. */
							try {
								difference = inputDatum.difference(tweedeInputDatum);

								System.out.printf("Verschil in jaren: %d \t\n", difference[0]);
								System.out.printf("Verschil in maanden: %d \t\n", difference[1]);
								System.out.printf("Verschil in weken: %d \t\n", difference[2]);
								System.out.printf("Verschil in dagen: %d \t\n\n", difference[3]);

								System.out.println("Deze getallen zijn naar beneden afgerond.");
							} catch (Exception e) {
								System.out.println(e.toString());
							}
						} catch (Exception e) {
								System.out.println(e.toString());
						}
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				} catch (NumberFormatException e) {
					System.out.println("Voer getallen in.");
				} catch (Exception e) {
				System.out.println(e.toString());
				}
			} catch (Exception e) {
				if (!(datum.equals(q))) {
					System.out.println(e.toString());
				} else {
					quit = true;
				}
			}

			System.out.println();

		} while (!quit);
	}

	/* Loopt door alle jaren heen in een bepaald interval en kijkt of de 
	 * opgegeven datum op een zondag was. */
	public static ArrayList<Integer> findIf(Datum datum, Interval interval, String dag) {

		ArrayList<Integer> years = new ArrayList<Integer>();

		Datum testDate;

		for (int i = interval.getLowerInt() ; i <= interval.getUpperInt() ; i++) {
			testDate = new Datum(datum, i);
			if (testDate.wasOnA().equals(dag)) {
				years.add(i);
			}
		}

		return years;
	}
}