/*
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.io.*;

class Paar implements Comparable<Paar> {   

	final private double coeff;
	final private int macht;
	private boolean constant = false;

	Paar (double a, int b) {
		this.coeff = a;
		if (b < 0) {
			this.constant = true;
		}
		this.macht = b;
	}

	Paar (double a, int b, boolean constant) {
		this(a,b);
		this.constant = constant;
	}
	
	Paar () {
		this(0,0);
	}

	public double coeff() {
		return this.coeff;
	}

	public int macht() {
		return this.macht;
	}

	public boolean constant() {
		return this.constant;
	}

	public Paar multiply(Paar other) {
		return new Paar(this.coeff * other.coeff(), this.macht + other.macht());
	}

	public Paar differentiate() {
		return new Paar(this.coeff * this.macht, this.macht - 1);
	}

	public Paar integrate() {
		if (this.macht == 0) {
			return new Paar(this.coeff, 1);
		} else {
			return new Paar(this.coeff / (this.macht + 1), this.macht + 1);
		}
	}

	public boolean getConstant() {
		return this.constant;
	}

	public int compareTo (Paar other) {

		if (this.macht < other.macht) {
			return 1;
		} else if (this.macht > other.macht) {
			return -1;
		} else {
			if (this.coeff < other.coeff) {
				return 1;
			} else if (this.coeff > other.coeff) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public boolean equals (Paar other) {

		boolean coeffEq = other.coeff == this.coeff;
		boolean machtEq = other.macht == this.macht;

		return coeffEq && machtEq;
	}

	public String toString() {

		if (this.constant) {
			if (this.macht > 0) {
				return String.format("c%sx%s", Polynoom.subscript(this.macht), Polynoom.superscript(this.macht));
			} else {
				return String.format("c%s", Polynoom.subscript(this.macht));
			}
		} else {
			if (this.macht <= 0) {
				return String.format("%.2f", this.coeff);
			} else {
				return String.format("%.2fx%s", this.coeff, Polynoom.superscript(this.macht));
			}
		}
	}
}


