/* Florian Heringa */
/*     10385835    */

/* Bevat een array van kamers en de methoden
 * benodigd om in- en uit te checken.
 * Ook de methoden voor het printen van de kamers en
 * een check of alle kamers vrij zijn.
 */


public class Hotel {

	/* Initialisatievariabelen voor de klasse */
	Kamer[] rooms;
	int freeRooms;

	/* Constructoren voor Hotel-klasse */
	public Hotel () {
		this(1);
	}

	public Hotel (int numRooms) {

		rooms = new Kamer[numRooms];
		freeRooms = numRooms;

		for (int i = 0 ; i < numRooms ; i++) {
			rooms[i] = new Kamer();
		}
	}

	/* Methode voor het inchecken van gasten */
	public void checkIn (String name, String surName, String dob) {

		for (Kamer k : rooms) {
			if (k.occupied == false) {
				k.curGuest.name = name;
				k.curGuest.surName = surName;
				k.curGuest.dob = new Datum(dob);
				k.occupied = true;
				this.freeRooms--;
				return;
			}
		}
	}

	/* Methode voor het uitchecken van gasten */
	public void checkOut (int roomNumber) {
		for (Kamer k : rooms) {
			if (roomNumber > Kamer.totalRooms) {
				System.out.println("Kamer bestaat niet");
				return;
			}
			if (k.occupied == false) {
				System.out.println("Kamer is al leeg...");
				return;
			}
			if (k.thisRoom == roomNumber) {
				k.occupied = false;
				this.freeRooms++;
				System.out.printf("%s, %s uitgecheckt\n", k.curGuest.name, k.curGuest.surName);
				return;
			}
		}
	}

	/* Methode om te kijken of er gasten in het hotel zijn */
	public boolean allRoomsFree() {
		if (this.freeRooms == Opgave5.numRooms) {
			return true;
		} else {
			return false;
		}
	}

	/* Logt alle kamers naar de terminal en laat zien of de kamer
	 * bezet of leeg is */
	public void showRooms () {

		int numGuests = 0;

		for (Kamer k : rooms) {
			if (k.occupied == true) {
				numGuests++;
			}
			System.out.printf("Kamer %d: %s\n", k.thisRoom, k);			
		}

		System.out.printf("Aantal gasten: %d\n", numGuests);
	}
}