/* Florian Heringa */
/*     10385835    */

/* Deze klasse heeft drie velden; een Dag-, Maand- en Jaar-object
 * welke samen een volledig datum vormen.
 */

public class Datum {

	private final Dag dag;
	private final Maand maand;
	private final Jaar jaar;

	private final static String[] dayMap = {"zaterdag",
											"zondag",
											"maandag",
											"dinsdag",
											"woensdag",
											"donderdag",
											"vrijdag"};

	Datum(String datum) {

		if (correctFormat(datum)) {
			/* Splitst de inputString op in dag, maand en jaar en
			 * converteert naar integers. */
			int dag = Integer.parseInt(datum.split("-")[0]);
			int maand = Integer.parseInt(datum.split("-")[1]);
			int jaar = Integer.parseInt(datum.split("-")[2]);

			Dag.correctDayNumber(dag, maand, jaar);

			this.dag = new Dag(dag);
			this.maand = new Maand(maand);
			this.jaar = new Jaar(jaar);
		} else {
			throw new IncorrectDateFormatException();
		}
	}

	Datum(int day, int month, int year) {

		Dag.correctDayNumber(day, month, year);

		this.dag = new Dag(day);
		this.maand = new Maand(month);
		this.jaar = new Jaar(year);
	}

	Datum(Datum datum, int jaar) {

		Dag.correctDayNumber(datum.getDagInt(), datum.getMaandInt(), jaar);

		this.dag = new Dag(datum.getDagInt());
		this.maand = new Maand(datum.getMaandInt());
		this.jaar = new Jaar(jaar);
	}

	/* Getter methoden. */
	public Dag getDag() {
		return this.dag;
	}

	public int getDagInt() {
		return this.dag.getDag();
	}

	public Maand getMaand() {
		return this.maand;
	}

	public int getMaandInt() {
		return this.maand.getMaand();
	}

	public Jaar getJaar() {
		return this.jaar;
	}

	public int getJaarInt() {
		return this.jaar.getJaar();
	}

	/* Berekent het verschil in jaren, maanden, weken en dagen tussen twee data.
	 * het redultaat wordt in een integer-array gestopt. */
	public int[] difference(Datum d) {

		if (d.isAfter(this)) {

			int[] returnAr = new int[4];

			/* Verschil in dagen, totaal. */
			returnAr[3] = this.differenceInDays(d);

			/* Verschil in jaren, totaal. */
			if (d.getMaand().isAfter(this.maand)) {
				returnAr[0] = Math.abs(this.jaar.difference(d.getJaar()));
			} else if (d.getMaand().equals(this.maand) && d.getDag().isAfter(this.dag) ) {
				returnAr[0] = Math.abs(this.jaar.difference(d.getJaar()));
			} else {
				returnAr[0] = this.jaar.difference(d.getJaar()) - 1;
			}

			/* Verschil in maanden, totaal. */
			if (d.getMaand().equals(this.maand) && d.getDag().isAfter(this.dag)) {
				returnAr[1] = returnAr[0] * 12 + this.maand.difference(d.getMaand());
			} else if (this.maand.isAfter(d.getMaand())) {
				returnAr[1] = returnAr[0] * 12 - this.maand.difference(d.getMaand());
			} else {
				returnAr[1] = returnAr[0] * 12 - this.maand.difference(d.getMaand()) - 1;
			}
		
			/* Verschil in weken, totaal. */		
			returnAr[2] = returnAr[3] / 7;

			return returnAr;
		} else {
			throw new IsNotAfterException(String.format("%s is niet na %s.", d, this));
		}
	}

	/* Kijkt of de datum na datum 'd' komt. */
	public boolean isAfter(Datum d) {

		if (this.jaar.difference(d.getJaar()) > 0) {
			return true;
		} else if (this.maand.difference(d.getMaand()) > 0) {
			return true;
		} else if (this.dag.difference(d.getDag()) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/* Berekent het dagnummer van de datum gezien vanaf 0. */
	public int dayNumber() {

		int month = (this.maand.getMaand() + 9) % 12;
		int year = this.jaar.getJaar() - (month / 10);

		return (365 * year) + (year / 4) - (year / 100) + (year / 400) + ((month * 306) + 5) / 10 + (this.dag.getDag() - 1);
	}

	/* Berekent het verschil in dagen tussen twee datums, gebaseerd op het
	 * aantal dagen vanaf 00-00-0000. */
	public int differenceInDays(Datum d) {
		return Math.abs(this.dayNumber() - d.dayNumber());
	}

	/* Berekent door middel van recursie de hoeveelheid donderdagen tot dat moment 
	 * in het jaar. Volgens de definitie van het weeknummer is week 1 de week waarin
	 * de eerste donderdag van het jaar valt. Dus de n-de dondedag in het jaar
	 * valt in de n-de week. */
	public int weekNumber(int x, int weekCounterVar) {

		int weekCounter = weekCounterVar;
		Datum testDatum;

		if (x > this.getMaandInt()) {
			return weekCounter - (int)Math.ceil(((31 - this.getDagInt()) / 7.0));
		}
		
		try {
			for (int i = 1 ; i <= 50 ; i++) {
				testDatum = new Datum(i, x, this.getJaarInt());
				if (testDatum.wasOnA().equals("donderdag")) {
					weekCounter++;
				}
			}
		} catch (IncorrectDayForMonthException e) {
			return this.weekNumber(x + 1, weekCounter);
		} catch (IncorrectMonthException e) {
			return weekCounter;
		}

		return 0;
	}


	/* Kijkt op welke dag de ingevoerde datum viel in het ingevoerde jaar. */
	/* Gebruikt 'Zeller's Congruentie'-algoritme. */
	/* https://en.wikipedia.org/wiki/Zeller's_congruence */
	public String wasOnA() {

		int thisJaar = this.jaar.getJaar();

		/* Aangepast jaar, jaar - 1 voor januari en februari. */
		int y;
		int m;
		int q = this.dag.getDag();

		if (this.maand.equals(1) || this.maand.equals(2)) {
			y = thisJaar - 1;
			m = this.getMaandInt() + 12;
		} else {
			y = thisJaar;
			m = this.getMaandInt();
		}

		int h = (q + (int)((26 * (m + 1)) / 10.0) + 
				 y + (int)(y / 4.0) + 6 * (int)(y / 100.0) + 
				 (int)(y / 400.0)) % 7;

		return dayMap[h];
	}

 	/* Kijkt of de ingevoerde datum de correcte indeling heeft. */
	private boolean correctFormat(String datum) {

		String patroon = "\\d{2}-\\d{2}-\\d{4}";
		if (datum.matches(patroon)) {
			return true;
		} else {
			return false;
		}
	}

	/* Basis toString methode zonder argumenten. */
	public String toString() {

		return String.format("%s-%s-%s", this.dag, this.maand, this.jaar);
	}

	/* Uitgebreide toString methode voor variatie. */
	public String toString(boolean year, boolean maand) {

		if (year && maand) {
			return String.format("%s %s %s, %s", this.wasOnA(), this.dag, this.maand.toString(maand), this.jaar);
		} else if (year) {
			return this.toString();
		} else if (maand) {
			return String.format("%s %s", this.dag, this.maand.toString(maand));
		} else {
			return String.format("%s %s %s", this.dag, this.maand.toString(true), this.jaar);
		}
	}
}


/* Exceptions voor de Datum klasse. */
class IncorrectDateFormatException extends RuntimeException {

	public String toString() {
		return String.format("Datum.IncorrectDateFormatException: %s", this.getMessage());
	}

	public String getMessage() {
		return "Incorrecte opmaak van datum.";
	}
}

class IsNotAfterException extends RuntimeException {

	String message = "Fout; data niet correct.";

	IsNotAfterException(String s) {
		this.message = s;
	}

	IsNotAfterException() {
	
	}

	public String toString() {
		return String.format("Datum.IsNotAfterException: %s", this.getMessage());
	}

	public String getMessage() {
		return this.message;
	}
}