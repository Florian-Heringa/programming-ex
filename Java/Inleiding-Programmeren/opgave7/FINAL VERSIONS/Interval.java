/* Florian Heringa */
/*     10385835    */

/* Deze klasse heeft twee velden die samen een interval vormen,
 * een lower- en een upperbound.
 */

public class Interval {

	private final Jaar lowerBound;
	private final Jaar upperBound;

	/* Constructor voor een Interval-object uit twee integers. */
	Interval(int lower, int upper) {

		if (isCorrectInterval(lower, upper)) {
			this.lowerBound = new Jaar(lower);
			this.upperBound = new Jaar(upper);
		} else {
			throw new IncorrectIntervalSequenceException();
		}
	}

	/* Constructor voor een Interval-object uit een String. */
	Interval(String range) {

		if (correctFormat(range)) {
			int lower = Integer.parseInt(range.split("-")[0]);
			int upper = Integer.parseInt(range.split("-")[1]);
			if (isCorrectInterval(lower, upper)) {
				this.lowerBound = new Jaar(lower);
				this.upperBound = new Jaar(upper);
			} else {
				throw new IncorrectIntervalSequenceException();
			}
		} else {
			throw new IncorrectIntervalFormatException();
		}
	}

	/* Kijkt of de ingevoerde string de correcte opmaak heeft voor een interval. */
	private boolean correctFormat(String range) {

		String patroon = "\\d{4}-\\d{4}";
		if (range.matches(patroon)) {
			return true;
		} else {
			return false;
		}
	}

	/* Getter-methoden. */
	public int getLowerInt() {
		return this.lowerBound.getJaar();
	}

	public int getUpperInt() {
		return this.upperBound.getJaar();
	}

	/* Berekent de lengte van het interval. */
	public int length() {
		return upperBound.getJaar() - lowerBound.getJaar(); 
	}

	/* Kijkt of het interval correct is: lower < upper. */
	private boolean isCorrectInterval(int lower, int upper) {
		return lower < upper ? true : false;
	}

	/* Basis toString-methode. */
	public String toString() {
		return String.format("%s-%s", lowerBound, upperBound);
	}
}

/* Exception-klasse voor Interval-Klasse. */

/* Thrown als het interval niet voldoet aan de correcte opmaak. */
class IncorrectIntervalFormatException extends RuntimeException {

	public String toString() {
		return String.format("Interval.IncorrectIntervalFormatException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Incorrecte opmaak, moet van de vorm 'iiii-iiii'.";
	}
}

/* Thrown als het tweede getal van het interval lager is dan het eerste getal. */
class IncorrectIntervalSequenceException extends RuntimeException {

	public String toString() {
		return String.format("Interval.IncorrectIntervalSequenceException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Incorrect interval.";
	}
}