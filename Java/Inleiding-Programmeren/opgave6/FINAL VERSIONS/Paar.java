    /* Florian Heringa */
       /* 10385835 */
/* Universiteit van Amsterdam */

/* Deze klasse slaat een paar getallen op in om te gebruiken in de vorm
 * a*x^b. Met de coefficient 'a' een double en de macht 'b' een integer.
 * De klasse bevat ook methoden voor het integreren of differentieren van
 * een paar. Ook het vermenigvuldigen van twee paren is aanwezig.
 * 
 */

class Paar implements Comparable<Paar> {   

	final private double coeff;
	final private int macht;
	private boolean constant = false;

	Paar (double a, int b) {
		this.coeff = a;
		if (b < 0) {
			this.constant = true;
		}
		this.macht = b;
	}

	Paar (double a, int b, boolean constant) {
		this(a,b);
		this.constant = constant;
	}
	
	Paar () {
		this(0,0);
	}

	/* Methoden om de velden te bereiken. */
	public double coeff() {
		return this.coeff;
	}

	public int macht() {
		return this.macht;
	}

	public boolean constant() {
		return this.constant;
	}

	/* Vermenigvuldigt twee paren volgens: (a*x^b) * (c*x^d) = (a*c)*x^(b+d). */
	public Paar multiply(Paar other) {
		return new Paar(this.coeff * other.coeff(), this.macht + other.macht());
	}

	/* Differentieert een paar volgens: D[a*x^b] = (a*b)*x^(b-1). */
	public Paar differentiate() {
		return new Paar(this.coeff * this.macht, this.macht - 1);
	}

    /* Integreert een paar volgens: I[a*x^b] = (a/b)*x^(b+1).
     * Als de macht 0 was wordt een nieuw paar aangemaakt, anders ontstaat er
     * een 'DivideByZero' error. */
	public Paar integrate() {
		if (this.macht == 0) {
			return new Paar(this.coeff, 1);
		} else {
			return new Paar(this.coeff / (this.macht + 1), this.macht + 1);
		}
	}

	/* Extensie van de compareTo methode voor gebruik bij sorteren.
	 * Zorgt voor het primair aflopend sorteren van de machten,
	 * en secundair aflopend sorteren van de coefficienten.*/
	public int compareTo (Paar other) {

		if (this.macht < other.macht) {
			return 1;
		} else if (this.macht > other.macht) {
			return -1;
		} else {
			if (this.coeff < other.coeff) {
				return 1;
			} else if (this.coeff > other.coeff) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/* Kijkt of de coefficient en de macht gelijk zijn. */
	public boolean equals (Paar other) {

		boolean coeffEq = other.coeff == this.coeff;
		boolean machtEq = other.macht == this.macht;

		return coeffEq && machtEq;
	}

	/* Geeft het paar in het juiste format weer door gebruik te maken van de
	 * super- en subscript methoden uit de Polynoom-klasse. */
	public String toString() {

		if (this.constant) {
			if (this.macht > 0) {
				return String.format("c%sx%s", Polynoom.subscript(this.macht), Polynoom.superscript(this.macht));
			} else {
				return String.format("c%s", Polynoom.subscript(this.macht));
			}
		} else {
			if (this.macht <= 0) {
				return String.format("%.2f", this.coeff);
			} else {
				if (this.coeff == 1) {
					return String.format("x%s", Polynoom.superscript(this.macht));
				} else {
					return String.format("%.2fx%s", this.coeff, Polynoom.superscript(this.macht));
				}
			}
		}
	}
}


