    /* Florian Heringa */
       /* 10385835 */
/* Universiteit van Amsterdam */

/* Interface voor de Polynoomklasse.
 */
interface PolynoomInterface {
    Polynoom telop(Polynoom that);
    Polynoom trekaf(Polynoom that);
    Polynoom vermenigvuldig(Polynoom that);
    Polynoom differentieer();
    Polynoom integreer();
    boolean equals(Object o);
    String toString();
}