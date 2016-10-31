/*Florian Heringa
 * 10385835
 *
 *	TestBreuken.java
 * 
 * Deze klasse bevat teststatements voor de 'Breuk'-klasse
 * alle methoden worden getest en voor inversen van elkaar wordt
 * gekeken of de originele breuk weer terug gegeven wordt. 
 */

class TestBreuken {

	public static void main(String[] args) {

		Breuk a = new Breuk(1,5);
		Breuk b = new Breuk(9,12);
		Breuk c = new Breuk(b);
		Breuk d = new Breuk();
		Breuk e = new Breuk(5);

		System.out.println("Tests voor de constructoren:");
		System.out.printf("Breuk a\t\t\t= %s\n", a);
		System.out.printf("Breuk b\t\t\t= %s\n", b);
		System.out.printf("Breuk c\t\t\t= %s\n", c);
		System.out.printf("Breuk d\t\t\t= %s\n", d);
		System.out.printf("Breuk e\t\t\t= %s\n\n", e);

		System.out.printf("Test de breuken a en b: %s en %s.\n", a, b);		
		System.out.printf("(a + b)\t\t\t= %s\n", a.telop(b));
		System.out.printf("(a - b)\t\t\t= %s\n", a.trekaf(b));
		System.out.printf("(a - b + b)\t\t= %s\n", a.trekaf(b).telop(b));
		System.out.printf("(a - b) + (b - a)\t= %s\n", a.trekaf(b).telop(b.trekaf(a)));
		System.out.printf("(a * b)\t\t\t= %s\n", a.vermenigvuldig(b));
		System.out.printf("(a / b)\t\t\t= %s\n", a.deel(b));
		System.out.printf("(a / b) * b\t\t= %s\n", a.deel(b).vermenigvuldig(b));
		System.out.printf("(a / b) * (b / a)\t= %s\n", a.deel(b).vermenigvuldig(b.deel(a)));
		System.out.printf("omgekeerde(a)\t\t= %s\n", a.omgekeerde());
		System.out.printf("a * omgekeerde(a)\t= %s\n", a.vermenigvuldig(a.omgekeerde()));
		System.out.printf("omgekeerde(b)\t\t= %s\n", b.omgekeerde());
		System.out.printf("b * omgekeerde(b)\t= %s\n", b.vermenigvuldig(b.omgekeerde()));
		System.out.printf("-a\t\t\t= %s\n", a.negate());
	}
}