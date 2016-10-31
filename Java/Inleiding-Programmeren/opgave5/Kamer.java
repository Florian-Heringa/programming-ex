/* Florian Heringa */
/*     10385835    */

/* Deze klasse bavat alle informatie nodig voor een kamer
 * 	- het totale aantal kamers
 *	- nummer van de huidige kamer
 * 	- huidige gast
 *	- of de kamer in gebruik is
 * Er is geen speciale constructor, omdat elke kamer identiek is 
 * bij initialisatie, afgezien van het kamernummer.
 */


public class Kamer {

	static int totalRooms = 0;
	int thisRoom;
	Gast curGuest;
	boolean occupied;

	public Kamer () {
		totalRooms += 1;
		this.thisRoom = totalRooms;
		this.curGuest = new Gast();
		this.occupied = false;
	}

	public String toString() {

		if (this.occupied == false) {
			return "Vrij";
		} else {
			return String.format("%s", curGuest);
		}
	}
}