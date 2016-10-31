/*
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.io.*;
import java.util.*;

public class Polynoom implements PolynoomInterface {   

	private ArrayList<Paar> termen;

	/* Construeert polynoom uit txt file. */
	Polynoom (String filename, boolean print) {

		ArrayList<Paar> tempTerm = new ArrayList<Paar>();

		leesPolynoom(filename, tempTerm, print);

		emptyAndSort(tempTerm);
	}

	/* Construeert polynoom uit arraylist. */
	Polynoom (ArrayList<Paar> termenOther) {

		ArrayList<Paar> tempTerm = new ArrayList<Paar>();

		for (Paar p : termenOther) {
			tempTerm.add(p);
		}

		emptyAndSort(tempTerm);
	}

	/* Tweedegraadspolynoom constructor met machten 0, 1 en 2. */
	Polynoom (double a, double b, double c) {

		ArrayList<Paar> tempTerm = new ArrayList<Paar>();

		Paar p1 = new Paar(a, 0);
		Paar p2 = new Paar(b, 1);
		Paar p3 = new Paar(c, 2);

		tempTerm.add(p2);
		tempTerm.add(p3);
		tempTerm.add(p1);

		emptyAndSort(tempTerm);
	}

	/* Defaultconstructor; een term; coeff en macht zijn 0. */
	Polynoom () {

		termen = new ArrayList<Paar>();
		Paar empty = new Paar(0, 0);

		this.termen.add(empty);

		Collections.sort(this.termen);
	}

	private void emptyAndSort(ArrayList<Paar> tempTerm) {

		this.termen = removeEmpty(tempTerm);
		Collections.sort(this.termen);
	}

    /* Verwijdert alle termen met coefficient = 0. */
    private ArrayList<Paar> removeEmpty(ArrayList<Paar> termen) {

    	ArrayList<Paar> temp = new ArrayList<Paar>();

    	for (Paar p : termen) {
    		if (p.coeff() == 0) {
    			continue;
    		} else {
    			temp.add(p);
    		}
    	}

    	return temp;
    }

	/* Leest paren in uit een txt file. */
	private static void leesPolynoom(String filename, ArrayList<Paar> termen, boolean print) {
		try {
			Scanner input = new Scanner(new File(filename));
			Paar temp;

			while (input.hasNext()) {
				double first = input.nextDouble();
				int second = input.nextInt();
				
				if (print) {
					System.out.println("Ingelezen de coefficient: " + first);
					System.out.printf("Ingelezen de bijbehorende macht: x%s\n", superscript(second));
				}

				temp = new Paar(first, second);

				termen.add(temp);

			}
			input.close();
		} catch (IOException e) {
			System.out.println("Fout: ");
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (InputMismatchException e) {
			System.out.println("Fout: onbekend symbool in file.");
			System.exit(0);
		}
	}

	/* Telt twee polynomen paarsgewijs bij elkaar op. */
	public Polynoom telop(Polynoom that) {

		ArrayList<Paar> tempParen = new ArrayList<Paar>();
		Polynoom tempPol;

		for (int i = 0 ; i < that.termen.size() ; i++) {
			tempParen.add(that.termen.get(i));		
		}
		for (int i = 0 ; i < this.termen.size() ; i++) {
			tempParen.add(this.termen.get(i));	
		}

		tempPol = new Polynoom(tempParen);
		tempPol = tempPol.combineer();

		return tempPol;
	}

	/* Trekt het meegegeven polynoom af van het bronpolynoom. */
    public Polynoom trekaf(Polynoom that) {

    	ArrayList<Paar> tempParen = new ArrayList<Paar>();
    	Paar tempPaar;
		Polynoom tempPol;

		for (int i = 0 ; i < that.termen.size() ; i++) {
			/* Nieuw paar, negatief voor aftrekken. */
			tempPaar = new Paar((-1)*that.getTermen().get(i).coeff(), 
								that.getTermen().get(i).macht());
			tempParen.add(tempPaar);		
		}
		for (int i = 0 ; i < this.termen.size() ; i++) {
			tempParen.add(this.termen.get(i));	
		}

		tempPol = new Polynoom(tempParen);
		tempPol = tempPol.combineer();

		return tempPol;
    }

    /* Vermenigvuldigt de polynomen met elkaar. */

    /*KLOPT NOG NIET: VERBETEREN*/
    public Polynoom vermenigvuldig(Polynoom that) {

    	ArrayList<Paar> tempList = new ArrayList<Paar>();
    	Paar tempPaar;
    	Polynoom tempPol;

    	for (Paar p : that.getTermen()) {
    		for (Paar q : this.termen) {
    			tempPaar = p.multiply(q);
    			tempList.add(tempPaar);
    		}
    	}

    	tempPol = new Polynoom(tempList);
    	tempPol = tempPol.combineer();
    	return tempPol;
    }

    /* Differentieert het polynoom naar x. */
    public Polynoom differentieer() {

    	ArrayList<Paar> tempList = new ArrayList<Paar>();
    	Paar tempPaar;

    	for (Paar p : this.termen) {
    		tempPaar = p.differentiate();
    		if (tempPaar.macht() < 0) {
    			continue;
    		}
    		tempList.add(tempPaar);
    	}

    	return new Polynoom(tempList);
    }

    /* Integreert het polynoom naar x. */
    public Polynoom integreer() {

    	ArrayList<Paar> tempList = new ArrayList<Paar>();
    	Paar tempPaar;
    	int readPairs = 0;
    	int constants;

    	/* Leest alle paren in en integreert per paar,
    	 * totdat een constante wordt gevonden. */
    	for (Paar p : this.termen) {
    		if (p.constant()) {
    			break;
    		}
    		readPairs++;
    		tempPaar = p.integrate();
    		tempList.add(tempPaar);
    	}

    	/* Het aantal constanten is hoe veel indices er nog in de lijst over waren. */
    	constants = this.termen.size() - readPairs;
    	for (int i = 0 ; i < constants ; i++) {
    		tempList.add(new Paar(1, -i - 1, true));
    	}
    	/*Voeg ten slotte de laatste integratieconstante toe.*/
    	tempList.add(new Paar(1, -constants - 1, true));

    	return new Polynoom(tempList);
    }

    public Polynoom combineer() {

    	ArrayList<Paar> tempParen = new ArrayList<Paar>();
    	Paar tempPaar;
    	boolean skipNext = false;

    	for (int i = 1 ; i < this.termen.size() ; i++) {
    		if (this.termen.get(i).macht() == this.termen.get(i - 1).macht()) {
    			tempPaar = new Paar(this.termen.get(i).coeff() + this.termen.get(i - 1).coeff(),
    								this.termen.get(i).macht());
    			tempParen.add(tempPaar);
    			skipNext = true;
    		} else {
    			if (!(skipNext)) {
    				tempParen.add(this.termen.get(i));
    			}
    		}
    	}
    	return new Polynoom(tempParen);
    }

    public static String superscript(int val) {

    	String returnString = "";

    	switch (val) {
    		case 1: returnString = "\u00B9"; break;
    		case 2: returnString = "\u00B2"; break;
    		case 3: returnString = "\u00B3"; break;
    		case 4: returnString = "\u2074"; break;
    		case 5: returnString = "\u2075"; break;
    		case 6: returnString = "\u2076"; break;
    		case 7: returnString = "\u2077"; break;
    		case 8: returnString = "\u2078"; break;
    		case 9: returnString = "\u2079"; break;
    		default: returnString = String.format("^%d", val); break;
    	}

    	return returnString;
    }

    public static String subscript(int val) {

    	String returnString = "";

    	int posVal = val * -1;

    	switch (posVal) {
    		case 1: returnString = "\u2081"; break;
    		case 2: returnString = "\u2082"; break;
    		case 3: returnString = "\u2083"; break;
    		case 4: returnString = "\u2084"; break;
    		case 5: returnString = "\u2085"; break;
    		case 6: returnString = "\u2086"; break;
    		case 7: returnString = "\u2087"; break;
    		case 8: returnString = "\u2088"; break;
    		case 9: returnString = "\u2089"; break;
    		default: returnString = String.format("_%d", posVal); break;
    	}

    	return returnString;
    }

    /* Kijkt of twee polynomen gelijk zijn. */
    public boolean equals(Polynoom other) {

    	boolean b = true;

    	/* Als de polynomen geen gelijke lengte hebben zijn ze neit gelijk. */
    	if (other.getTermen().size() == this.termen.size()) {

    		/* Voorkomt 'ArrayIndexOutOfBoundsException. '*/
    		int loopLength = other.getTermen().size() < this.termen.size() ? other.getTermen().size() : this.termen.size();

    		for (int i = 0 ; i < loopLength ; i++) {
    			if (this.termen.get(i).coeff() != other.getTermen().get(i).coeff() ||
    				this.termen.get(i).macht() != other.getTermen().get(i).macht()) {
    				b = false;
    			}
    		}

    	} else {
    		b = false;
    	}
    	return b;
    }

    public ArrayList<Paar> getTermen() {
    	return this.termen;
    }

    /* Slaat het polynoom op in een txt file. */
    public void savePolynoom(String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter((filename)));
				for (Paar p : this.termen) {
					out.write(p.coeff() + " " + p.macht() + " ");
				}
			out.close();

			System.out.printf("Polynoom %s opgeslagen in %s.\n", this.toString(), filename);
		} catch (IOException e) {
			System.out.println("Error:");
			System.out.println(e.getMessage());
		}
	}

    public String toString() {

    	String returnString = "";
    	Paar currentPaar;
    	int size = this.termen.size();

    	/* Voegt elk paar toe aan een string door ze op te halen uit termen. */
    	for (int i = 0 ; i < size - 1 ; i++) {
    		currentPaar = this.termen.get(i);
    		if (currentPaar.constant() && currentPaar.macht() < 0) {
				returnString += currentPaar + String.format("x%s + ", superscript(size - i - 1));
    		} else {
    		returnString += currentPaar + " + ";
    		}
    	}
    	/* Voegt de laatste term toe, zonder '+'. */
    	returnString += this.termen.get(termen.size() - 1);
    	return returnString;
    }

    public void test() {
    	for (Paar p : this.termen) {
    		System.out.println(p);
    	}
    }
}


 