/*Florian Heringa
* 10385835
*
*	TestBreuken.java
* 
*
*/

//import java.util.*;

class testBCG {

	public static void main(String[] args) {

		BCG bcg1 = new BCG(5, 3, 1, 2);
		BCG bcg2 = new BCG(1, 6, -1, 3);

		System.out.printf("bcg1				= %s\n", bcg1);
		System.out.printf("bcg2				= %s\n", bcg2);
		System.out.printf("bcg2	+ bcg1		= %s\n", bcg2.telop(bcg1));
		System.out.printf("bcg1	- bcg2		= %s\n", bcg1.trekaf(bcg2));

	}
}
