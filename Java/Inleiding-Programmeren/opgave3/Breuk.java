/*Florian Heringa
 * 10385835
 *
 *	Breuk.java
 * 
 * Deze klasse zorgt er voor dat breuken exact weergegeven kunnen worden
 * in plaats van met een niet nauwkeurige floating point notatie.
 * De breuken worden opgeslagen met twee integer waarden voor teller en noemer
 * waar envoudig operaties op kunnen worden uitgevoerd.
 * Om de breuk op te vragen kunnen gewone stringformat methodes gebruiken omdat de 
 * uitvoer van de toString methode zorgt voor een string in de vorm: "a/b"
 *
 */


class Breuk implements BreukInterface {

	int teller, noemer;

/* Vier constructors, drie voor verschillende hoeveelheden parameters:
 *		1. Zelfgekozen teller en noemer;
 *		2. Zelfgekozen teller, noemer default op 1;
 *		3. Defaultconstructor voor 0/1;
 * en een kopieer constructor met een breuk als input.
 */
	public Breuk (int t, int n) {

		/* Brengt de breuk in standaardvorm
		 * ex.: 3/-5 => -3/5
		 */
		if (n < 0) {
			n *= -1;
			t *= -1;
		}

		if (n == 0) {
			System.out.println("Cannot divide by zero!");
			System.exit(0);
		}

		int ggd = ggd(t, n);

		teller = t / ggd;
		noemer = n / ggd;

	}
	public Breuk (int t) {
		this(t, 1);
	}
	public Breuk () {
		this(0);
	}
	public Breuk (Breuk original) {
		this(original.teller, original.noemer);
	}

/* Vermenigvuldigt twee breuken volgens:
 * a/b * c/d = ac/bd
 */
	public Breuk vermenigvuldig (Breuk b2) {

		int nieuweTeller = this.teller * b2.teller;
		int nieuweNoemer = this.noemer * b2.noemer;

		Breuk nieuw = new Breuk(nieuweTeller, nieuweNoemer);
		return nieuw;
	}

/* Deelt twee breuken volgens:
 * a/b * c/d = ad/bc
 */
    public Breuk deel(Breuk b2) {

    	int nieuweTeller = this.teller * b2.noemer;
    	int nieuweNoemer = this.noemer * b2.teller;

		Breuk nieuw = new Breuk(nieuweTeller, nieuweNoemer);
		return nieuw;
    }

/* Maakt de breuken gelijknamig en telt de tellers 
 * en de noemers bij elkaar op.
 */ 
    public Breuk telop(Breuk b2) {

    	int nieuweTeller = (this.teller * b2.noemer) + (this.noemer * b2.teller);
    	int nieuweNoemer = this.noemer * b2.noemer;

    	Breuk nieuw = new Breuk(nieuweTeller, nieuweNoemer);
    	return nieuw;
	}

/* Maakt de breuken gelijknamig en trekt de tellers 
 * en de noemers van elkaar af.
 */ 
    public Breuk trekaf(Breuk b2) {
    	int nieuweTeller = (this.teller * b2.noemer) - (this.noemer * b2.teller);
    	int nieuweNoemer = this.noemer * b2.noemer;

    	Breuk nieuw = new Breuk(nieuweTeller, nieuweNoemer);
    	return nieuw;
    }

/* Maakt van de ingevoerde breuk zijn negatieve tegenhanger
 * ex.: 2/3 => -2/3
 */
    public Breuk negate() {

    	int nieuweTeller = -1 * this.teller;

    	Breuk nieuw = new Breuk(nieuweTeller, this.noemer);
    	return nieuw;
    }

/* Verwisselt de teller en de noemer van de breuk om zo de 
 * inverse te krijgen ex.: 2/5 => 5/2
 */
    public Breuk omgekeerde () {

    	Breuk returnBreuk = new Breuk(this.noemer, this.teller);
    	return returnBreuk;
    }

/* Gebruikt recursie om de grootste gemene deler te vinden van de 
 * teller en noemer van een breuk
 *
 *
 */
    private static int ggd (int a, int b) {

    	if (a < 0) {
    		a *= -1;
    	}

    	if (b > a) {
    		int temp = a;
    		a = b;
    		b = temp;
    	}

    	if (b == 0) {
    		return a;
    	}
    	else {
    		return ggd(b, (a % b));
    	}
    }

/* Bepaalt de uitvoer indien een breukobject naar een string wordt geconverteerd
 * Print alleen de teller als de noemer gelijk is aan 1, want 5/1 = 5.
 */
    public String toString () {

    	if (this.noemer == 1) {
    		return String.format("%s", this.teller);
    	}

    	return String.format("%s/%s", this.teller, this.noemer);
    }


}