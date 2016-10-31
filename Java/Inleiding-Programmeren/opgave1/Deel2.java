/* Florian Heringa
*  10385835
* 
*  Neemt een getal mee als argument en berekent de som van de even en oneven 
*  getallen tot het gekozen getal. Ten slotte wordt het verschil tussen de
*  twee berekende waarden gegeven.
*/

import java.util.*;


public class Deel2 {
	public static void main(String[] args)
	{
		int input = Integer.parseInt(args[0]);

		int resultEven = 0;
		int resultUneven = 0;

		
		/*Loops through all even numbers and adds them*/
		for (int x = 2; x < input; x += 2)
		{
			resultEven += x;
		}

		/*Loops through all uneven numbers and adds them*/
		for (int x = 1; x < input; x += 2)
		{
			resultUneven += x;
		}

		System.out.printf("Som van de oneven getallen tot en met %d is %d\n", 
			input, resultUneven);		
		System.out.printf("Som van de even getallen tot en met %d is %d\n",
		 input, resultEven);
		System.out.printf("Verschil tussen de twee sommen is %d\n\n",
		 (resultEven - resultUneven));
	}
}