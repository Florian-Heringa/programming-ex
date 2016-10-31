/* * DNAMatch v0.1 * 
* author  :  Dr. Quakerjack. 
* date    :  17-09-2015 *
* version :  0.1 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class bopgave4 {

	public static ArrayList<String> database = new ArrayList<String>();

	public static void main(String[] args) {

		String str = "hello world";

		astdb(str);
		lsdb();

		//System.out.println(str.length());

	 	int minimum = min_3(2,3,1);
	 	System.out.println(minimum);
	}

	public static void astdb(String i) {
	    database.add(i);    
	}

	private static int min_3(int a, int b, int c) {
	    return (a < b) ? (a < c ? a : c) : (b < c ? b : c);
	}

	public static void lsdb() {
	    for(String s : database) {
			System.out.println(s);
	    }
		System.out.println();
	}
}