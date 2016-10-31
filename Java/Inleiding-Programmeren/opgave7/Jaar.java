/* Florian Heringa */
/*     10385835    */

/* Deze klasse houdt een integer veld voor een dag
 * om te gebruiken als object in een datum. Ook is een boolean
 * veld aanwezig als check of het een schrikkeljaar is.
 */

public class Jaar {

	private final int jaar;
	private final boolean leapYear;

	/* Constructor voor een Jaar-object uit een int. */
	Jaar(int jaar) {
		if (isCorrectYear(jaar)) {
			this.jaar = jaar;
			this.leapYear = leapYear(jaar);
		} else {
			throw new IncorrectYearFormatException();
		}
	}

	/* Copy-constructor. */
	Jaar(Jaar j) {
		this.jaar = j.getJaar();
		this.leapYear = j.getLeapYear();
	}

	/* Getter-methoden. */
	public int getJaar() {
		return this.jaar;
	}

	public boolean getLeapYear() {
		return this.leapYear;
	}

	/* Berekent het verschil tussen twee opgegeven Jaar-objecten. */
	public int difference(Jaar j) {
		return this.jaar - j.getJaar();
	}

	/* Kijkt of het jaar een correct format heeft. Dit werkt alleen met jaren
	 * na 1000, ofwel jaren met 4 digits. */
	private boolean isCorrectYear(int jaar) {
		int length = (int)(Math.log10(jaar) + 1);
		return length == 4;
	}

	/* Kijkt of het opgegeven jaar een schrikkeljaar is. */
	public static boolean leapYear(int jaar) {

		if (jaar % 4 == 0) {
			if ((jaar % 100 == 0) && !(jaar % 400 == 0)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/* Basis toString-methode. */
	public String toString() {
		return String.format("%d", this.jaar);
	}
}

/* Exception-klasse voor de Jaar-klasse. */
/* Thrown als het jaar niet uit 4 digits bestaat. */
class IncorrectYearFormatException extends RuntimeException {

	public String toString() {
		return String.format("Jaar.IncorrectYearFormatException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Incorrecte hoeveelheid getallen; jaar moet na 1000.";
	}
}