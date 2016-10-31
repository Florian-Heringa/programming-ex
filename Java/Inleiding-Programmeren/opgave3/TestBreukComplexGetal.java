/*Florian Heringa
 * 10385835
 *
 *	TestBreuken.java
 * 
 * Test de 'ComplexGetal'-klasse door elke methode
 * te proberen.
 */


class TestBreukComplexGetal {

	public static void main(String[] args) {

		BreukComplexGetal a = new BreukComplexGetal(5, 3, 1, 2);
		BreukComplexGetal b = new BreukComplexGetal(1, 6, -1, 3);

		System.out.printf("a \t\t\t= %s\n", a);
		System.out.printf("b \t\t\t= %s\n", b);
		System.out.printf("a + b \t\t\t= %s\n", a.telop(b));
		System.out.printf("a - b \t\t\t= %s\n", a.trekaf(b));
		System.out.printf("a - b + b \t\t= %s\n", b.telop(a.trekaf(b)));
		System.out.printf("(a - b) + (b - a) \t= %s\n", a.trekaf(b).telop(b.trekaf(a)));
		System.out.printf("a * b \t\t\t= %s\n", a.vermenigvuldig(b));
		System.out.printf("a / b \t\t\t= %s\n", a.deel(b));
		System.out.printf("(a / b) * b \t\t= %s\n", a.deel(b).vermenigvuldig(b));
		System.out.printf("(a / b) * (b / a)\t= %s\n", a.deel(b).vermenigvuldig(b.deel(a)));
		System.out.printf("Omgekeerde van a \t= %s\n", a.omgekeerde());
		System.out.printf("Omgekeerde van b \t= %s\n", b.omgekeerde());

	}
}
