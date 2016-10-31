/* Florian Heringa
*  10385835
*  
*  A program for printing a certain amount of Lucs Numbers to the terminal.
*  Lucas numbers obey the following equation:
*  									/ 2				  if n = 0
*                         L_n :=   |  1				  if n = 1
*                                   \ L_(n-1)+L_(n-2) if n > 1
*
*  The terminal prompts the user to enter a number via the Scanner class
*  then, the first Lucas numbers up to the input number are shown.
*  Output is limited to the first 45 numbers
*
*  Input: A single value via the scanner class
*  Output: The first n Lucas numbers up to n = 45
*/

import java.util.Random;
import java.util.*;


public class Deel4 {
	public static void main(String[] args)
	{
		Random random = new Random();
		Scanner  scanner = new Scanner(System.in);

		int currentL = 2;
		int nextL = 1;
		int tempL = 1;


		System.out.println("Print de eerste n Lucas getallen naar de terminal");
		System.out.println("Tellen begint bij n = 0");
		System.out.print("Geef een natuurlijk getal 'n': ");
		
		if (!scanner.hasNextInt()) 
		{
			System.out.println("Foute invoer.");
			return;
		}
		
		int getal = scanner.nextInt();

		/*First two if statements are for error control, the next two are for 
		*the exceptions (n = (1 or 2)). The last else contains a for-loop which
		*loops through the LucasNumbers and logs them in the terminal*/
		if (getal > 45)
		{
			System.out.println("\nGetal te groot, past niet");
		}
		else if (getal <= 0) {
			System.out.println("\nTe lage n; fout");
		}
		else if (getal == 1) {
			System.out.println("Het eerste Lucasgetal is:\n2");
		}
		else if (getal == 2) {
			System.out.println("De eerste twee Lucasgetallen zijn:\n2 1");
		}
		else 
		{
			System.out.printf("De eerste %d Lucasgetallen zijn:\n2 1 ", getal);
			for (int i = 2; i < getal; i++)
			{
				tempL = nextL;
				nextL += currentL;
				currentL = tempL;

				System.out.printf("%d ", nextL);
			}
		System.out.println("\nFinished");
		}
	}
}