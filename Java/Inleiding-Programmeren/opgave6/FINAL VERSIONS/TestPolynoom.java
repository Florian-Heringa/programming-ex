    /* Florian Heringa */
       /* 10385835 */
/* Universiteit van Amsterdam */

/* Testklasse voor de Polynoom- en Paarklasse.
 * Maakt 4 polynomen aan uit een txt-file en kijkt of
 * alle methoden correct werken.
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;


public class TestPolynoom {

	public static void main(String[] args) {

		//testParen();
		
		testPolynomen();

		System.out.println();
	}

	public static void testPolynomen() {

		Polynoom polynoom1 = makeRandomPolynoom(3, 10);
		Polynoom polynoom2 = makeRandomPolynoom(2, 10);
		Polynoom polynoom3 = new Polynoom(12, 4, 1);
		Polynoom polynoom4 = new Polynoom(-7, 2, 5);
		testSave(polynoom3, polynoom1);
		Polynoom polynoom5 = new Polynoom("test1.txt", false);
		Polynoom polynoom6 = new Polynoom(2, 2, 2);
		Polynoom polynoom7 = new Polynoom(3, 3, 3);
		Polynoom polynoom8 = new Polynoom("testtest.txt", false);

		System.out.println(polynoom6);

		testConstructorenPolynoom(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testEquals(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testOptellen(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testAftrekken(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testOptellen(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testDifferentieren(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testIntegreren(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
		testDiffAndInt(polynoom1, polynoom2, polynoom3, polynoom4, polynoom5, polynoom6, polynoom7);
	}

	public static void testParen () {

		Paar test1 = new Paar(0, 0);
		Paar test2 = new Paar(1.1, 1);
		Paar test3 = new Paar(-15.5, 1);
		Paar test4 = new Paar(80, -156);
		Paar test5 = new Paar(-100, -8);

		testConstructorenParen(test1, test2, test3, test4, test5);
		testIfConstant(test1, test2, test3, test4, test5);
		testEqualsParen(test1, test2, test3, test4, test5);
		testCompareToParen(test1, test2, test3, test4, test5);
	}

	public static void testIfConstant(Paar test1, Paar test2, Paar test3, Paar test4, Paar test5) {
		System.out.println("Paar 5 is constant: " + test5.constant());
		System.out.println(test5.toString());
		System.out.println();
	}

	public static void testConstructorenParen(Paar test1, Paar test2, Paar test3, Paar test4, Paar test5) {
		System.out.println("\nTests voor de constructie van de klasse Paar:");
		System.out.println("Het eerste testpaar is: " + test1);
		System.out.println("Het tweede testpaar is: " + test2);
		System.out.println("Het derde testpaar is: " + test3);
		System.out.println("Het vierde testpaar is: " + test4);
		System.out.println("Het vijfde testpaar is: " + test5);
		System.out.println();
	}

	public static void testEqualsParen(Paar test1, Paar test2, Paar test3, Paar test4, Paar test5) {
		System.out.println("\nTests voor de 'equals' methode van de klasse Paar:");
		System.out.printf("%s en %s zijn gelijk: %b \n", test1, test1, test1.equals(test1));
		System.out.printf("%s en %s zijn gelijk: %b \n", test2, test3, test2.equals(test3));
		System.out.println();
	}

	public static void testCompareToParen(Paar test1, Paar test2, Paar test3, Paar test4, Paar test5) {
		System.out.println("\nTests voor de 'compareTo' methode van de klasse Paar:");
		System.out.printf("vergelijk %s en %s: %d \n", test2, test2, test2.compareTo(test2));
		System.out.printf("vergelijk %s en %s: %d \n", test1, test5, test1.compareTo(test5));
		System.out.printf("vergelijk %s en %s: %d \n", test5, test1, test5.compareTo(test1));
		System.out.printf("vergelijk %s en %s: %d \n", test3, test2, test3.compareTo(test2));
		System.out.printf("vergelijk %s en %s: %d \n", test2, test3, test2.compareTo(test3));
		System.out.println();
	}

	public static void testDiffAndInt(Polynoom polynoom1, Polynoom polynoom2,
									  Polynoom polynoom3, Polynoom polynoom4,
									  Polynoom polynoom5, Polynoom polynoom6,
									  Polynoom polynoom7) {
		System.out.println("\nTests voor het integreren en differentieren van polynomen: ");
		System.out.printf("I[%s] = (%s)\n", polynoom6, polynoom6.integreer());
		System.out.printf("I[I[%s]] = (%s)\n", polynoom6, polynoom6.integreer().integreer());
		System.out.printf("I[I[I[%s]]] = (%s)\n", polynoom6, polynoom6.integreer().integreer().integreer());
		System.out.printf("D[I[%s]] = (%s)\n", polynoom6, polynoom6.integreer().differentieer());
		System.out.printf("I[D[%s]] = (%s)\n", polynoom6, polynoom6.differentieer().integreer());
		System.out.printf("D[D[I[I[%s]]]] = (%s)\n", polynoom6, 
										polynoom6.integreer().integreer().differentieer().differentieer());
		System.out.printf("I[I[D[D[%s]]]] = (%s)\n", polynoom6, 
										polynoom6.differentieer().differentieer().integreer().integreer());
		System.out.println();
	}

	public static void testIntegreren(Polynoom polynoom1, Polynoom polynoom2,
									  Polynoom polynoom3, Polynoom polynoom4,
									  Polynoom polynoom5, Polynoom polynoom6,
									  Polynoom polynoom7) {
		System.out.println("\nTests voor het integreren van polynomen: ");
		System.out.printf("I[%s] = (%s)\n", polynoom6, polynoom6.integreer());
		System.out.printf("I[I[%s]] = (%s)\n", polynoom6, polynoom6.integreer().integreer());
		System.out.printf("I[I[I[%s]]] = (%s)\n", polynoom6, polynoom6.integreer().integreer().integreer());
		System.out.println();
	}

	public static void testDifferentieren(Polynoom polynoom1, Polynoom polynoom2,
									  	  Polynoom polynoom3, Polynoom polynoom4,
									  	  Polynoom polynoom5, Polynoom polynoom6,
									  	  Polynoom polynoom7) {
		System.out.println("\nTests voor het differentieren van polynomen: ");
		System.out.printf("D[D[%s]] = (%s)\n", polynoom6, polynoom6.differentieer().differentieer());
		System.out.printf("D[%s] = (%s)\n", polynoom1, polynoom1.differentieer());
		System.out.printf("D[D[%s]] = (%s)\n", polynoom1, polynoom1.differentieer().differentieer());
		System.out.printf("D[%s] = (%s)\n", polynoom4, polynoom4.differentieer());
		System.out.println();
	}

	public static void testVermenigvuldigen(Polynoom polynoom1, Polynoom polynoom2,
									  		Polynoom polynoom3, Polynoom polynoom4,
									  		Polynoom polynoom5, Polynoom polynoom6,
											Polynoom polynoom7) {
		System.out.println("\nTests voor het vermenigvuldigen van polynomen: ");
		System.out.printf("(%s) * (%s) = (%s)\n", polynoom6, polynoom6, polynoom6.vermenigvuldig(polynoom6));
		System.out.println();
	}

	public static void testAftrekken(Polynoom polynoom1, Polynoom polynoom2,
									 Polynoom polynoom3, Polynoom polynoom4,
									 Polynoom polynoom5, Polynoom polynoom6,
									 Polynoom polynoom7) {
		System.out.println("\nTests voor het aftrekken van polynomen: ");
		System.out.printf("(%s) + (%s) = (%s)\n", polynoom7, polynoom6, polynoom7.trekaf(polynoom6));
		System.out.println();
	}

	public static void testOptellen(Polynoom polynoom1, Polynoom polynoom2,
									Polynoom polynoom3, Polynoom polynoom4,
									Polynoom polynoom5, Polynoom polynoom6,
									Polynoom polynoom7) {
		System.out.println("\nTests voor het optellen van polynomen: ");
		System.out.printf("(%s) + (%s) = (%s)\n", polynoom3, polynoom3, polynoom3.telop(polynoom3));
		System.out.println();
	}

	public static void testSave(Polynoom polynoom1, Polynoom polynoom2) {
		System.out.println("\nTests voor het opslaan van een polynoom in een txt file: ");
		polynoom1.savePolynoom("test1.txt");
		polynoom2.savePolynoom("test2.txt");
		System.out.println();
	}

	public static void testEquals(Polynoom polynoom1, Polynoom polynoom2,
								  Polynoom polynoom3, Polynoom polynoom4,
								  Polynoom polynoom5, Polynoom polynoom6,
								  Polynoom polynoom7) {
		System.out.println("\nTests voor 'equals' methode van de klasse 'Polynoom': ");
		System.out.printf("%s en %s zijn\n\tgelijk: %b\n", polynoom1, polynoom1, polynoom1.equals(polynoom1));
		System.out.printf("%s en %s zijn\n\tgelijk: %b\n", polynoom1, polynoom3, polynoom1.equals(polynoom3));
		System.out.printf("%s en %s zijn\n\tgelijk: %b\n", polynoom3, polynoom4, polynoom3.equals(polynoom4));
		System.out.println();
	}

	public static void testConstructorenPolynoom(Polynoom polynoom1, Polynoom polynoom2,
									  			 Polynoom polynoom3, Polynoom polynoom4,
									  			 Polynoom polynoom5, Polynoom polynoom6,
									 			 Polynoom polynoom7) {
		System.out.println("\nTests voor constructie van de klasse 'Polynoom': ");
		System.out.println(polynoom1);
		System.out.println(polynoom2);
		System.out.println(polynoom3 + "(opgeslagen)");
		System.out.println(polynoom4);
		System.out.println(polynoom5 + "(ingeladen)");
		System.out.println(polynoom6);
		System.out.println(polynoom7);
		System.out.println();
	}

	public static Polynoom makeRandomPolynoom (int maxPower, double maxCoeff) {

		ArrayList<Paar> termen = new ArrayList<Paar>();		
		Random rd = new Random();
		Paar paar;
		double coeff;

		for (int i = 0 ; i <= maxPower ; i++) {
			coeff = maxCoeff * rd.nextDouble();
			paar = new Paar(coeff, i);
			termen.add(paar);
		}

		Polynoom returnPolynoom = new Polynoom(termen);
		return returnPolynoom;
	}
}