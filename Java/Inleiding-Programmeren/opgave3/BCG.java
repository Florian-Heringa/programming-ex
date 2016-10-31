/*Florian Heringa
* 10385835
*
*	Breuk.java
* 
*
*/

//import java.util.*;

class BCG /*implements ComplexGetalInterface*/  {

	Breuk a, b;

	public BCG (int a, int b, int c, int d) {
		this.a = new Breuk(a,b);
		this.b = new Breuk(c,d);
	}

	public BCG () {
		this(0, 1, 0, 1);
	}

	public BCG telop(BCG cg) {

		BCG returncg = new BCG();

		returncg.a = this.a.telop(cg.a);
		returncg.b = this.b.telop(cg.b);

		return returncg;
	}

    public BCG trekaf(BCG cg) {

		BCG returncg = new BCG();

		returncg.a = this.a.trekaf(cg.a);
		returncg.b = this.b.trekaf(cg.b);

		return returncg;
    }
/*
    public ComplexGetal vermenigvuldig(ComplexGetal cg) {

		ComplexGetal returncg = new ComplexGetal();

		returncg.a = (this.a * cg.a) - (this.b * cg.b);
		returncg.b = (this.a * cg.b) + (this.b * cg.a);

		return returncg;
    }

    public ComplexGetal deel(ComplexGetal cg) {

		ComplexGetal returncg = new ComplexGetal();

		returncg.a = (this.a * cg.a) + (this.b * cg.b) /
					 ((cg.a * cg.a) + (cg.b * cg.b));
		returncg.b = (this.b * cg.a) - (this.a * cg.b) /
					 ((cg.a * cg.a) + (cg.b * cg.b));

		return returncg;
    }

    public ComplexGetal omgekeerde() {

		ComplexGetal returncg = new ComplexGetal();

		returncg.a = (this.a) / ((this.a * this.a) + (this.b * this.b));
		returncg.b = (-this.b) / ((this.a * this.a) + (this.b * this.b));

		return returncg;
    }
*/
    public String toString () {

    	if (this.b.teller < 0) {
    		int tempTeller = this.b.teller * -1;
    		Breuk tempBreuk = new Breuk(tempTeller, this.b.noemer);
    		return String.format("%s - %s i", this.a, tempBreuk);
    	}
    	return String.format("%s + %s i", this.a, this.b);	

    }
}