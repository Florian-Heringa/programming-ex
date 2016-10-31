    /* Florian Heringa */
       /* 10385835 */
/* Universiteit van Amsterdam */



/* Deze klasse slaat een polynoom op als een collectie
 * van paren (welke gedefinieerd zijn in de klass 'Paar')
 * Het is mogelijk om simpele operaties uit te voeren op de
 * polynomen zoals: optellen,
 *                  aftrekken,
 *                  vermenigvuldigen,
 *                  differentieren en
 *                  integreren.
 * Het is mogelijk om simpele tweedegraadspolynomen te maken, of om
 * polynomen in te laden uit een txt file. Hiervoor is een altererend patroon
 * van coefficient en macht gescheiden door spaties nodig.
 */

import java.io.*;
import java.util.*;

public class Polynoom implements PolynoomInterface {   

	private final ArrayList<Paar> termen;

	/* Construeert polynoom uit txt file. */
	Polynoom (String filename, boolean print) {

		ArrayList<Paar> tempTerm = new ArrayList<Paar>();

		leesPolynoom(filename, tempTerm, print);

		this.termen = removeEmpty(tempTerm);
        Collections.sort(this.termen);
	}

	/* Construeert polynoom uit arraylist. */
	Polynoom (ArrayList<Paar> termenOther) {

		ArrayList<Paar> tempTerm = new ArrayList<Paar>();

		for (Paar p : termenOther) {
			tempTerm.add(p);
		}

		this.termen = removeEmpty(tempTerm);
        Collections.sort(this.termen);
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

		this.termen = removeEmpty(tempTerm);
        Collections.sort(this.termen);
	}

	/* Defaultconstructor; een term; coeff = 1 en macht = 0. */
	Polynoom () {

		termen = new ArrayList<Paar>();
		Paar empty = new Paar(1, 0);

		this.termen.add(empty);

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
			Paar toAdd;

			while (input.hasNext()) {
                /* Gaat uit van afwisselend coeff en macht in txt file. */
				double first = input.nextDouble();
				int second = input.nextInt();
				
				if (print) {
					System.out.println("Ingelezen de coefficient: " + first);
					System.out.printf("Ingelezen de bijbehorende macht: x%s\n", superscript(second));
				}
                /* Voegt het ingelezen paar toe aan de arraylist. */
				toAdd = new Paar(first, second);
				termen.add(toAdd);
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

	/* Telt twee polynomen bij elkaar op, en combineert de coefficienten van
     * corresponderende machten. */
	public Polynoom telop(Polynoom that) {

		ArrayList<Paar> tempParen = new ArrayList<Paar>();
		Polynoom tempPol;

		for (Paar p : that.termen) {
			tempParen.add(p);		
		}
		for (Paar p : this.termen) {
			tempParen.add(p);	
		}

		tempPol = new Polynoom(tempParen);
		tempPol = tempPol.combineer();

		return tempPol;
	}

	/* Trekt het meegegeven polynoom (that) af van het bronpolynoom. */
    public Polynoom trekaf(Polynoom that) {

    	ArrayList<Paar> tempParen = new ArrayList<Paar>();
    	Paar tempPaar;
		Polynoom tempPol;

		for (int i = 0 ; i < that.termen.size() ; i++) {
			/* Nieuw paar, negatief voor aftrekken. */
			tempPaar = new Paar(-that.getPaar(i).coeff(), 
								that.getPaar(i).macht());
			tempParen.add(tempPaar);		
		}
		for (int i = 0 ; i < this.termen.size() ; i++) {
			tempParen.add(this.getPaar(i));	
		}

		tempPol = new Polynoom(tempParen);
		tempPol = tempPol.combineer();

		return tempPol;
    }

    /* Vermenigvuldigt de polynomen met elkaar. */
    public Polynoom vermenigvuldig(Polynoom that) {

    	ArrayList<Paar> tempList = new ArrayList<Paar>();
    	Paar tempPaar;
    	Polynoom tempPol;

        /* Vermenigvuldigt elke term van this met elke term van that. */
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

    /* Combineert de coefficienten van de machten van x die gelijk zijn. */
    public Polynoom combineer() {

    	ArrayList<Paar> tempParen = new ArrayList<Paar>();
    	Paar tempPaar;
    	boolean skipNext = false;

        /* Kijkt voor elk paar of de macht van het volgende paar hetzelfde is
         * en combineert ze als dit het geval is. */
        for (int i = 0 ; i < this.termen.size() ; i++) {
            try {
                if (!skipNext & this.getPaar(i).macht() == this.getPaar(i + 1).macht()) {
                    tempPaar = new Paar(this.getPaar(i).coeff() + this.getPaar(i + 1).coeff(),
                                        this.getPaar(i).macht());
                    tempParen.add(tempPaar);
                    skipNext = true;
                } else {
                    if (skipNext) {
                        skipNext = false;
                    } else {
                        tempParen.add(this.getPaar(i));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                if (!skipNext) {
                    tempParen.add(this.getPaar(i));
                }
            }
        }
    	return new Polynoom(tempParen);
    }

    /* Superscriptmethode voor gebruik in de machten van x. 
     * Werkt alleen correct voor n < 10, en returnt anders
     * '^n'.*/
    public static String superscript(int n) {

    	String returnString = "";

    	switch (n) {
    		case 1: returnString = "\u00B9"; break;
    		case 2: returnString = "\u00B2"; break;
    		case 3: returnString = "\u00B3"; break;
    		case 4: returnString = "\u2074"; break;
    		case 5: returnString = "\u2075"; break;
    		case 6: returnString = "\u2076"; break;
    		case 7: returnString = "\u2077"; break;
    		case 8: returnString = "\u2078"; break;
    		case 9: returnString = "\u2079"; break;
    		default: returnString = String.format("^%d", n); break;
    	}

    	return returnString;
    }

    /* Subscriptmethode voor de constanten. Werkt alleen voor 
     * n < 10, en returnt anders '_n'.*/
    public static String subscript(int n) {

    	String returnString = "";

    	int posVal = n * -1;

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

    	/* Als de polynomen geen gelijke lengte hebben zijn ze niet gelijk. */
    	if (other.getTermen().size() == this.termen.size()) {

    		for (int i = 0 ; i < this.termen.size() ; i++) {
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


    /* Slaat het polynoom op in een txt file. */
    public void savePolynoom(String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter((filename)));
				for (Paar p : this.termen) {
					out.write(p.coeff() + " " + p.macht() + " ");
				}
			out.close();

			System.out.printf("Polynoom %s opgeslagen in %s.\n",
                            this.toString(), filename);
		} catch (IOException e) {
			System.out.println("Error:");
			System.out.println(e.getMessage());
		}
	}

    /* Deze methode zorgt er voor dat het polynoom correct wordt weergegeven
     * in een string. */
    public String toString() {

    	String returnString = "";
    	Paar currentPaar;
    	int size = this.termen.size();
        Paar laatstePaar = this.getPaar(this.termen.size() - 1);

    	/* Voegt elk paar toe aan een string door ze op te halen uit termen. */
        if (size == 1) {
            returnString += getPaar(0);
        } else if (size == 0) {
            returnString = "Leeg polynoom...";
        } else {
            /* Voeg eerste paar toe aan string. */
            returnString += getPaar(0);
            for (int i = 1 ; i < size - 1; i++) {
                currentPaar = getPaar(i);
                if (currentPaar.constant() && currentPaar.macht() < 0) {
                    /* Zorgt dat de constante de juiste macht van x toegewezen 
                     * krijgt, afhankelijk van zjn positie in de arraylist. */
                    returnString += String.format(" + %sx%s", currentPaar,
                                                  superscript(size - i - 1));                    
                } else {
                    /* Voegt, afhnakelijk van of de coefficient negatief of 
                     * positief is, deze toe aan de string met een plus of min. */
                    returnString += posOrNegCoeff(currentPaar);
                }
            }
        }

        if (laatstePaar.constant()) {
            returnString += String.format(" + %s", laatstePaar);
        } else {
            returnString += posOrNegCoeff(laatstePaar);
        }   
        return returnString;
    }

    /* Kijkt of de coefficient positief of negatief is en gebruikt deze
     * informatie om een corresponderende string te genereren. */
    private String posOrNegCoeff(Paar p) {

        String returnString;

        if (p.coeff() < 0) {
            returnString = String.format(" - %s", 
                                          new Paar(-p.coeff(),
                                          p.macht()));
        } else {
            returnString = String.format(" + %s", p);
        }

        return returnString;
    }

    /* Returnt het paar van het polynoom op index 'i'. */
    private Paar getPaar(int i) {
        return this.termen.get(i);
    }

    public ArrayList<Paar> getTermen() {
        return this.termen;
    }
}


 