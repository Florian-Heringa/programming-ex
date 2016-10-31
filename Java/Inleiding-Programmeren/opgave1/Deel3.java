/* Florian Heringa
*  10385835
*  
*  A simple 'guess the number' game. The user gets three chances to guess
*  the randomly generated number.
*  
*  Input: None initially, accepts three seperate inputs via the Scanner class
*  		  before terminating the program.
*  Output: Shows the user if they have to guess higher or lower, after three
*          guesses, the terminal displays the number and ends the program.
*/

import java.util.Random;
import java.util.*;


public class Deel3 {
	public static void main(String[] args)
	{
		Random random = new Random();
		Scanner  scanner = new Scanner(System.in);

		int toGuess = random.nextInt(10) + 1;
		boolean guessed = false;
		boolean error = false;

		/*Loops for three times through the guess routine and breaks out when
		*the number is guessed. i is gekozen vanaf 1 tot 4 om later makkelijk 
		*te gebruiken in de display text.*/
		for(int i = 1; i < 4; i++)
		{
			System.out.printf
		("\nGeef een getal tussen 1 en 10, je mag nog %d keer raden:\n", (4-i));
			
			if (!scanner.hasNextInt())
			{
				System.out.println("Foute invoer, sluit programma.");
				error = true;
				break;
			}
			int guess = scanner.nextInt();
			System.out.printf("Keer %d: %d\n", i, guess);

			if (guess > 10) {
				System.out.println("Geen getal tussen de 1 en 10");
				break;
			}

			else if (guess == toGuess) {
				System.out.println("Gewonnen!");
				guessed = true;
				break;			
			}

			else if (guess > toGuess) {
				System.out.println("Te Hoog");
			}

			else if (guess < toGuess) {
				System.out.println("Te Laag");
			}
		}

		if (guessed == false && error == false)
		{
			System.out.printf("Verloren, het getal was %d\n", toGuess);
		}
	}	
}