/* Florian Heringa */
/*     10385835    */

/* Deze klasse bevat informatie voor het correct weergeven
 * van een datum
 *	- dag
 *	- maand
 *	- jaar
 * Bij initalisatie word gekeken of de datum het juiste formaat heeft.
 * Ook wordt gekeken of de datum kan bestaan.
 */


public class Datum {

	int day;
	int month;
	int year;
	int[] datum = new int[3];

	public Datum (String datum) {

		boolean b = isCorrectFormat(datum);

		if (b) {
			this.datum = toIntArr(datum);
			this.isCorrectDate(this.datum);

			this.day = this.datum[0];
			this.month = this.datum[1];
			this.year = this.datum[2];
		}
		else {
			System.out.println("Datum in onjuist formaat, sluit programma....");
			System.exit(0);
		}
	}

	/* Kijkt of de ingevoerde datum de juiste vorm heeft */
	static public boolean isCorrectFormat(String datum) {

		String patroon = "\\d{2}-\\d{2}-\\d{4}";
		boolean b = datum.matches(patroon);

		return b;
	}

	/* checkt of de datum kan bestaan, geeft exception als dit niet
	 * het geval is */
	static public void isCorrectDate(int[] date) throws DateIndexOutOfReachException{

		int leapyear = date[2] % 4;

		if (date[1] > 12) {
			throw new DateIndexOutOfReachException();
			
		}
		switch (date[1]) {
					case 1: 
					case 3: 
					case 5: 
					case 7:
					case 8:
					case 10:
					case 12:
					if (date[0] > 31) {
						throw new DateIndexOutOfReachException();
					} break;
					case 4: 
					case 6: 
					case 9: 
					case 11:
					if (date[0] > 30) {
						throw new DateIndexOutOfReachException();
					} break;
					case 2:
					if (leapyear == 0) {
						if (date[0] > 29) {
							throw new DateIndexOutOfReachException();
						}
					} else {
						if (date[0] > 28) {
							throw new DateIndexOutOfReachException();
						}
					} break;
		}
		return;
	}
	
	/* placeholder array voor datumoperaties */
	public int[] toIntArr(String datum) {

		int[] returnArr = new int[3];

		try {
			String[] splitDate = datum.split("-");
			returnArr[0] = Integer.parseInt(splitDate[0]);
			returnArr[1] = Integer.parseInt(splitDate[1]);
			returnArr[2] = Integer.parseInt(splitDate[2]);
		} catch (NumberFormatException e) {
			System.out.println("Voer enkel getallen in, sluit programma....");
			System.exit(0);
		}
		return returnArr;
	}

	/* Print de datum in de vorm dd-mm-jjj */
	public String toString() {
			return String.format("%s-%s-%s", this.day, this.month, this.year);
	}
}

/* custom exception voor datum error */
class DateIndexOutOfReachException extends RuntimeException {

	public String toString () {
		return "Date index out of reach";
	}
}