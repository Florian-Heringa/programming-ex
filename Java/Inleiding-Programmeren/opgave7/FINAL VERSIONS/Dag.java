/* Florian Heringa */
/*     10385835    */

/* Deze klasse houdt een enkel integer veld voor een dag
 * om te gebruiken als object in een datum.
 */

public class Dag {

	private final int dag;

	/* Constructor voor een dag-object uit een int. */
	Dag(int dag) {
		correctDayNumberShort(dag);
		this.dag = dag;
	}

	/* Copy-constructor. */
	Dag(Dag d) {
		this.dag = d.getDag();
	}

	/* Getter methode voor het dag-veld. */
	public int getDag() {
		return this.dag;
	}

	/* Geeft het verschil tussen twee dag-velden van twee instantiaties
	 * van de dag-klasse. */
	public int difference(Dag d) {
		return this.dag - d.getDag();
	}

	/* Geeft of deze dag na d is. */
	public boolean isAfter(Dag d) {
		return this.dag > d.getDag();
	}

	/* Kijkt of de dag valide kan zijn. Maanden hebben nooit meer dan 31 dagen. */
	private void correctDayNumberShort(int dag) {
		if (dag > 31 || dag < 1) {
			throw new IncorrectDayException();
		}
	}

	/* Kijkt of de dag correct is voor de meegegeven  maand-jaar combinatie. */
	public static void correctDayNumber(int dag, int maand, int jaar) {

		switch (maand) {
			case 1: case 3: case 5:
			case 7: case 8: case 10:
			case 12: 
			if (dag > 31) {
				throw new IncorrectDayForMonthException();
			} break;
			case 4: case 6: case 9: case 11:
			if (dag > 30) {
				throw new IncorrectDayForMonthException();
			} break;
			case 2:
			if (Jaar.leapYear(jaar)) {
				if (dag > 29) {
					throw new IncorrectDayForMonthException();
				} break;
			} else {
				if (dag > 28) {
					throw new IncorrectDayForMonthException();
				} break;
			}
		}
	}

	public String toString() {
		return String.format("%d", this.dag);
	}
}

/* Exception klassen voor de Dag-klasse. */

/* Thrown als de dag fout is voor de bijbehorende maand. */
class IncorrectDayForMonthException extends RuntimeException {

	public String toString() {
		return String.format("Dag.IncorrectDayForMonthException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Te veel dagen voor de maand.";
	}
}

/* Thrown als: !(1 < dag < 31)*/
class IncorrectDayException extends RuntimeException {

	public String toString() {
		return String.format("Dag.IncorrectDayException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Dagen moet tussen 1 en 31.";
	}
}