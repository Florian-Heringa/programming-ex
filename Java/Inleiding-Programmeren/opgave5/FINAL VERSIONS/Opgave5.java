/* Florian Heringa */
/*     10385835    */

 /*
 * Dit programma verzorgt de interface tussen hoteleigenaar en
 * de informatie betreffende de bezetting van de kamers.
 */

import java.util.Scanner;

public class Opgave5 {

	/* Initialisatievariabelen zichtbaar voor het volledige programma */
	public static int numRooms = 1;
	public static Datum vandaag = new Datum("28-09-2015");

	public static void main (String[] args) {

		/* Kijkt of er een integer is meegegeven voor het aantal kamers,
		 * default is 1 kamer */
		try {
			numRooms = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		if (numRooms == 0) {
			System.out.println("Een hotel moet kamers hebben....");
			System.exit(0);
		}

		Hotel hotel = new Hotel(numRooms);

		makeUI(numRooms, hotel);
	}

	/* Verkrijgt de input van de user */
	public static String readInput() {

		Scanner input = new Scanner(System.in);
		String returnStr = "";

		if(input.hasNextLine()) {
			returnStr = input.nextLine();
	    }

	    return returnStr; 
	}

	/* Maakt de user interface, en bevat error handling */
	private static void makeUI (int numRooms, Hotel hotel) {

		boolean quit = false;

		String[] guestArray = new String[3];

		do {
			System.out.println("Kies uit [1] Statusoverzicht, [2] Gasten inchecken, [3] Gasten " + 
				"uitchecken, [4] Einde.");
			System.out.print("Uw keuze: ");
			String command = readInput();

			if (command.equals("1")) {
				System.out.println("Status van dit hotel:");
				hotel.showRooms();
			} else if (command.equals("2")) {
				if (hotel.freeRooms == 0) {
					System.out.println("Geen kamers vrij...");
				} else {
					System.out.println("Er zijn vrije kamers:");
					System.out.print("Geef achternaam gast: ");
					guestArray[0] = readInput();
					System.out.print("Geef voornaam gast: ");
					guestArray[1] = readInput();
					System.out.print("Geef geboortedatum gast [dd-mm-jjjj]: ");
					guestArray[2] = readInput();

					try {
						if (Datum.isCorrectFormat(guestArray[2])) {
							hotel.checkIn(guestArray[1], guestArray[0], guestArray[2]);
						} else {
							System.out.println("Verkeerde datuminvoer...");
						}
					} catch (DateIndexOutOfReachException e) {
						System.out.println(e);
					}					
				}
			} else if (command.equals("3")) {
				if (hotel.allRoomsFree()) {
					System.out.println("Geen gasten in het hotel...");
				} else {
					System.out.print("In welke kamer heeft u geslapen: ");
					try {
						int roomNum = Integer.parseInt(readInput());
						hotel.checkOut(roomNum);
					} catch (NumberFormatException e) {
						System.out.println("Please enter a number...");
					}
				}
			} else if (command.equals("4")) {
				quit = true;
			} else {
				System.out.println("Invoer onbekend...");
			}
			System.out.println("");
		} while (quit == false);
	}
}