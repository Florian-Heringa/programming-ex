import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Oefententamen2 {

	public static void main(String[] args) {

		int i = 0, j = 1, k = 2;

		for (int iter : Arrays.asList(1, 2, 3)) {
			//System.out.println(iter);
			i += iter + k;
			j += k * i;
			k = iter * i / j;
			System.out.println("iter = " + iter);	
			System.out.println("i = " + i);	
			System.out.println("j = " + j);	
			System.out.println("k = " + k);			
		}

		System.out.println(k);		
		//printer(new int[5], 0);
		//System.out.println(methode());
	}

	static void printer(int[] p, int index) {
		if (index >= p.length) {
			System.out.println(Arrays.toString(p));
		} else {
			p[index] = index + 1;
			printer(p, index + 1);
		}
	}

	static int methode() {

		int waarde = 0;
		try {
			waarde = 2;
			doIets();
			waarde = 3;
		} catch (NullPointerException e) {
			waarde = waarde + 4;
		} catch (ArithmeticException e) {
			waarde = waarde + 5;
		}
		return waarde;
	}

	static void doIets() {
		throw new NullPointerException();
	}

}