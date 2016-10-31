/*
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.io.*;

public class Opgave6 {   

	/* RAM DB during execution*/ 
	public static ArrayList<String> database = new ArrayList<String>();

	public static void main (String[] args) {

	}

	/*Scans the input from the user*/
	public static String readInput() {

		Scanner input = new Scanner(System.in);
		String returnStr = "";

		if(input.hasNextLine()) {
			returnStr = input.nextLine();
	    }

	    return returnStr; 
	}

}


