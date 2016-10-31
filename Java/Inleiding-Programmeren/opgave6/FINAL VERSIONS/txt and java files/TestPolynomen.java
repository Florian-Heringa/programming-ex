    /* Florian Heringa */
       /* 10385835 */
/* Universiteit van Amsterdam */

/* Testklasse voor de Polynoom- en Paarklasse.
 * Maakt 4 polynomen aan uit een txt-file en kijkt of
 * alle methoden correct werken.
 */

class TestPolynomen { 

	public static void main(String[] args) {
	 	
	 	Polynoom pol1 = new Polynoom("polynoom1.txt", false);
	 	Polynoom pol2 = new Polynoom("polynoom2.txt", false);
	 	Polynoom pol3 = new Polynoom("polynoom3.txt", false);
	 	Polynoom pol4 = new Polynoom("polynoom4.txt", false);

	 	testPolynoom(pol1, pol2);
	 	testPolynoom(pol3, pol4);
	}

	static void testPolynoom(Polynoom pol1, Polynoom pol2) {

		Polynoom diffInt;
		Polynoom intDiff;

		System.out.println("Polynoom 1\t\t: " + pol1);
		System.out.println("Polynoom 2\t\t: " + pol2);
		System.out.println();

		System.out.println("Som\t\t\t\t\t: " + pol1.telop(pol2));
		System.out.println("Verschil\t\t\t\t: " + pol1.trekaf(pol2));
		System.out.println("Product\t\t\t\t\t: " + pol1.vermenigvuldig(pol2));

		System.out.printf("Differentieer pol1\t\t\t: %s\n", pol1.differentieer());
		System.out.printf("Differentieer pol2\t\t\t: %s\n", pol2.differentieer());

		System.out.println("Integreer pol1\t\t\t\t: " + pol1.integreer());
		System.out.println("Integreer pol2\t\t\t\t: " + pol2.integreer());

		diffInt = pol1.differentieer().integreer();
		System.out.println("Differentieer en dan integreer pol1\t: " + diffInt);
		System.out.println("\t\t\t\t" + testEquals(pol1, diffInt));
		intDiff = pol1.integreer().differentieer();
		System.out.println("Integreer en dan Differentieer pol1\t: " + intDiff);
		System.out.println("\t\t\t\t" + testEquals(pol1, intDiff));

		diffInt = pol2.differentieer().integreer();
		System.out.println("Differentieer en dan integreer pol2\t: " + pol2.differentieer().integreer());
		System.out.println("\t\t\t\t" + testEquals(pol2, diffInt));
		intDiff = pol2.integreer().differentieer();
		System.out.println("Integreer en dan differentieer pol2\t: " + pol2.integreer().differentieer());
		System.out.println("\t\t\t\t" + testEquals(pol2, intDiff));

		System.out.println();
	}

	private static String testEquals(Polynoom pol1, Polynoom pol2) {
		if (pol1.equals(pol2)) {
			return "(Is gelijk aan origineel.)";
		} else {
			return "(Is NIET gelijk aan origineel.)";
		}
	}
}