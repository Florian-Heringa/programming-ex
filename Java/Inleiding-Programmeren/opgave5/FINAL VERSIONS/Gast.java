/* Florian Heringa */
/*     10385835    */

/* Deze klasse bevat alle informatie voor de gast
 * 	- de naam
 *	- de achternaam
 *	- de geboortedatum
 * De enige methode die gebruikt wordt is om te kijken
 * of de gast ouder is dan 18.
 */


public class Gast {

	String name = "";
	String surName = "";
	Datum dob;

	public Gast () {
		this("John", "Doe", "01-01-2000");
	}

	public Gast (String name, String surName, String dob){
		this.dob = new Datum(dob);
		this.name = name;
		this.surName = surName;
	}


	/* Checkt of de gast jonger is dan 18 */
	public boolean underEighteen () {

		Datum vandaag = Opgave5.vandaag;
		int dY = vandaag.year - this.dob.year;

		if (dY < 18) {
			return true;
		} else if (18 >= dY && dY < 19 && this.dob.month > vandaag.month) {
			return true;
		} else if (18 >= dY && dY < 19 && this.dob.month <= vandaag.month && this.dob.day > vandaag.day) {
			return true;
		}else {
			return false;
		}
	}

	/* Voor stringconversie, format:
	 * naam, achternaam, (dd-mm-jjjj)
	 * met een sterretje er achter indien de gast jonger is dan 18
	 */
	public String toString () {
		if (underEighteen()) {
			return String.format("%s, %s (%s)*", this.surName, this.name, this.dob);
		} else {
			return String.format("%s, %s (%s)", this.surName, this.name, this.dob);
		}
	}
}