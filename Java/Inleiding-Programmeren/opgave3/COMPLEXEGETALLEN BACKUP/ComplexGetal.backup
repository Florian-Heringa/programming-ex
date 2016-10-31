/*Florian Heringa
* 10385835
*
*	Breuk.java
* 
*
*/

//import java.util.*;

class ComplexGetal implements ComplexGetalInterface  {

	double a, b;

	public ComplexGetal (double a, double b) {
		this.a = a;
		this.b = b;
	}

	public ComplexGetal () {
		this(0, 0);
	}

	public ComplexGetal telop(ComplexGetal cg) {

		ComplexGetal returncg = new ComplexGetal();

		returncg.a = this.a + cg.a;
		returncg.b = this.b + cg.b;

		return returncg;
	}

    public ComplexGetal trekaf(ComplexGetal cg) {

		ComplexGetal returncg = new ComplexGetal();

		returncg.a = this.a - cg.a;
		returncg.b = this.b - cg.b;

		return returncg;
    }

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

    public String toString () {
    	if (this.b < 0) {
    		double newB = this.b * -1;
    		return String.format("%1$,.2f - %2$,.2f i", this.a, newB);	
    	}
    	else {
    		return String.format("%1$,.2f + %2$,.2f i", this.a, this.b);
    	}
    }

}