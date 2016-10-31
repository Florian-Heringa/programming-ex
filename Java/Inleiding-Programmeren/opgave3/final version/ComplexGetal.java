/*Florian Heringa
 * 10385835
 *
 *	ComplexGetal.java
 * 
 * Deze klasse geeft de mogelijkheid om compleze getallen met 
 * coefficienten in de vorm van breuken weer te geven.
 * Een complex getal is van de vorm a +/- b*i waar i^2 = -1, en
 * a en b zijn de reeele coefficienten.
 * Omdat het bewerken van complexe getallen vaak floating point
 * getallen achterlaat is het handig om deze coefficienten exact weer
 * te kunnen geven, dit kan met behulp van de 'Breuk'-klasse.
 * 
 * In het commentaar, indien er een operatie wordt uitgevoerd op twee
 * complexe getallen worden de getallen a +/- b*i en c +/- d*i gebruikt.
 */


class BreukComplexGetal implements BreukComplexGetalInterface  {

	Breuk re, im;

/* Drie constructoren;
 * 		1. Complex getal met breuken.
 *		2. COmplex getal met integer coefficienten.
 *		3. Complex getal met a = b = 0.
 */
	public BreukComplexGetal (int a, int b, int c, int d) {
		this.re = new Breuk(a,b);
		this.im = new Breuk(c,d);
	}

	public BreukComplexGetal (int a, int b) {
		this(a, 1, b, 1);
	}

	public BreukComplexGetal () {
		this(0, 1, 0, 1);
	}

/* Telt twee getallen bij elkaar op volgens:
 * (a+c) + (b+d)*i
 */
	public BreukComplexGetal telop(BreukComplexGetal cg) {

		BreukComplexGetal returncg = new BreukComplexGetal();

		returncg.re = this.re.telop(cg.re);
		returncg.im = this.im.telop(cg.im);

		return returncg;
	}

/* Trekt cg van het brongetal af:
 * (a-c) + (b-d)*i
 */
    public BreukComplexGetal trekaf(BreukComplexGetal cg) {

		BreukComplexGetal returncg = new BreukComplexGetal();

		returncg.re = this.re.trekaf(cg.re);
		returncg.im = this.im.trekaf(cg.im);

		return returncg;
    }

/* Vermenigvuldigt twee getallen met elkaar volgens:
 * (a*c - b*d) + (a*d + b*c)*i
 */

    public BreukComplexGetal vermenigvuldig(BreukComplexGetal cg) {

		BreukComplexGetal returncg = new BreukComplexGetal();

		returncg.re = (this.re.vermenigvuldig(cg.re)).trekaf(this.im.vermenigvuldig(cg.im));
		returncg.im = (this.re.vermenigvuldig(cg.im)).telop(this.im.vermenigvuldig(cg.re));

		return returncg;
    }

/* Berekent de inverse van een complex getal: 1/(a +/- b*i) volgens:
 * (a/(a*a + b*b)) - (b/(a*a + b*b))*i
 */
    public BreukComplexGetal omgekeerde() {

		BreukComplexGetal returncg = new BreukComplexGetal();

		returncg.re = this.re.deel(this.re.vermenigvuldig(this.re).telop(this.im.vermenigvuldig(this.im)));
		returncg.im = this.im.deel(this.re.vermenigvuldig(this.re).telop(this.im.vermenigvuldig(this.im))).negate();

		return returncg;
    }

/* Deelt het brongetal door cg te vermenigvuldigen met
 * het omgekeerde.
 */
    public BreukComplexGetal deel(BreukComplexGetal cg) {

		BreukComplexGetal returncg = new BreukComplexGetal();

		returncg = this.vermenigvuldig(cg.omgekeerde());

		return returncg;
    }

/* Bepaalt de uitvoer indien een complex getal als een string geprint moet worden
 * roept automatisch de toString methode van de Breuk klasse aan.
 * Geeft re - im*i als het imaginaire gedeelt negatief is
 */
    public String toString () {

    	if (this.im.teller < 0) {
    		int tempTeller = this.im.teller * -1;
    		Breuk tempBreuk = new Breuk(tempTeller, this.im.noemer);
    		return String.format("%s - %s i", this.re, tempBreuk);
    	}
    	return String.format("%s + %s i", this.re, this.im);	

    }
}