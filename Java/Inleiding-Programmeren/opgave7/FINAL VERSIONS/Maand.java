/* Florian Heringa */
/*     10385835    */

/* Deze klasse houdt een enkel integer veld voor een maand
 * om te gebruiken als object in een datum.
 */

public class Maand {

	private final int maand;

	/* Constructor voor een maand-object uit een int. */
	Maand(int maand) {
		this.maand = maand;

		if (!isCorrectMonth(maand)) {
			throw new IncorrectMonthException();
		}
	}

	/* Copy-constructor. */
	Maand(Maand m) {
		this.maand = m.getMaand();
	}

	/* Getter methode voor het maand-veld. */
	public int getMaand() {
		return this.maand;
	}

	/* Kijkt of de maand valide kan zijn. Een integer waarde voor maand
	 * is altijd tussen 1 (januari) en 12 (december). */
	private boolean isCorrectMonth(int maand) {
		if (maand > 12 || maand < 1) {
			return false;
		} else {
			return true;
		}
	}

	/* Vergelijkingsmethode voor maand met een integer. */
	public boolean equals(int i) {
		return i == this.maand;
	}

	/* Vergelijkingsmethode voor twee maanden. */
	public boolean equals(Maand m) {
		return m.getMaand() == this.maand;
	}

	/* Kijkt of this na de maand m komt. */
	public boolean isAfter(Maand m) {
		return this.maand > m.getMaand();
	}

	/* Geeft het verschil tussen de maand-velden in twee Maand-objecten. */
	public int difference(Maand m) {
		return this.maand - m.getMaand();
	}

	/* Converteert het integer maand-veld naar een String. */
	private String numberToMonth(int maand) {

		switch (maand) {
			case 1: return "januari"; 
			case 2: return "februari";
			case 3: return "maart";
			case 4: return "april";
			case 5: return "mei";
			case 6: return "juni";
			case 7: return "juli";
			case 8: return "augustus";
			case 9: return "september";
			case 10: return "oktober";
			case 11: return "november";
			case 12: return "december";
			default: return "";
		}
	}

	/* Basis toString-methode. */
	public String toString() {
		return String.format("%d", this.maand);
	}

	/* Variabele toString-methode voor het printen van de maand als een String. */
	public String toString(boolean full) {
		if (full) {
			return String.format("%s", numberToMonth(this.maand));
		} else {
			return this.toString();
		}
	}
}

/* Exceptions voor de Maand-klasse.*/

/* Thrown als het maand-veld niet tussen de 1 en 12 is. */
class IncorrectMonthException extends RuntimeException {

	public String toString() {
		return String.format("Maand.IncorrectMonthException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Maand moet tussen de 1 en 12.";
	}
}