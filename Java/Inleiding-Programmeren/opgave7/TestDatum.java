/*
 */

 class TestDatum {

 	public static void main(String[] args) {

 		try {
 			System.out.println("DATUM:");
 			testDatum();
 			// System.out.println("INTERVALLEN:");
 			// testInterval();
 			// System.out.println("JAREN:");
 			// testJaar();
 			// System.out.println("MAANDEN:");
 			// testMaand();
 			// System.out.println("DAGEN:");
 			// testDag();
 		} catch (Exception e) {
 			System.out.println(e);
 		}	
 	}

 	public static void testWasOnA(Datum test1, Datum test2) {

 		System.out.println(test1.wasOnA());
 		System.out.println(test2.wasOnA());
 	}

 	public static void testDag() {

 		Dag test1 = new Dag(1);
 		Dag test2 = new Dag(14);
 		Dag test3 = new Dag(31);

 		System.out.println();
 		System.out.println("dagen");
 		System.out.println(test1);
 		System.out.println(test2);
 		System.out.println(test3);
 	}

 	public static void testMaand() {

 		Maand test1 = new Maand(1);
 		Maand test2 = new Maand(4);
 		Maand test3 = new Maand(7);
 		Maand test4 = new Maand(12);

 		System.out.println();
 		System.out.println("maanden");
 		System.out.println(test1 + test1.toString(true));
 		System.out.println(test2 + test2.toString(true));
 		System.out.println(test3 + test3.toString(true));
 		System.out.println(test4 + test4.toString(true));
 	}

 	public static void testJaar() {

 		Jaar test1 = new Jaar(1992);
 		Jaar test2 = new Jaar(1800);
 		Jaar test3 = new Jaar(2000);

 		System.out.println();
 		System.out.println("jaren:");
 		System.out.println(test1);
 		System.out.println(test2);
 		System.out.println(test3);

 		System.out.println();
 		System.out.println("leapyear:");
 		System.out.println(test1.getLeapYear());
 		System.out.println(test2.getLeapYear());
 		System.out.println(test3.getLeapYear());

 		System.out.println();
 		System.out.println("difference:");
 		System.out.println(test1.difference(test2));
 		System.out.println(test1.difference(test3));
 	}

 	public static void testInterval() {

 		Interval test1 = new Interval("1950-2000");
 		Interval test2 = new Interval("1000-4000");
 		Interval test3 = new Interval("1992-2015");

 		System.out.println();
 		System.out.println("intervallen:");
 		System.out.println(test1);
 		System.out.println(test2);
 		System.out.println(test3);

 		System.out.println();
 		System.out.println("lengte:");
 		System.out.println(test1.length());
 		System.out.println(test2.length());
 		System.out.println(test3.length());
 	}

 	public static void testDatumMethods(Datum test1, Datum test2) {

 		Datum test3;
 		Datum test4;
 		int test3Int = 20;
 		int test4Int = 1992;


 		System.out.println(test1.wasOnA());
 		System.out.println(test2.wasOnA());

 		System.out.println();

 		System.out.println(test1.toString(true, true));
 		System.out.println(test1.toString(true, false));
 		System.out.println(test1.toString(false, true));
 		System.out.println(test1.toString(false, false));

 		System.out.println();

 		for (int i = 0 ; i < 10 ; i++) {

 			test3 = new Datum(String.format("%d-01-1992", test3Int++));

 			System.out.println(test3.toString(true, true));
 		}

 		System.out.println();

 		for (int i = 0 ; i < 10 ; i++) {

 			test4 = new Datum(String.format("20-01-%d", test4Int++));

 			System.out.println(test4.toString(true, true));
 		}
 	}

 	public static void testDifferenceDatum(Datum test1, Datum test2) {

 		System.out.println();
 		int[] test3 = test1.difference(test2);

 		System.out.println("aantal dagen: " + test3[0]);
 		System.out.println("aantal maanden: " + test3[1]);
 		System.out.println("aantal jaren: " + test3[2]);
 		System.out.println("aantal weken: " + test3[3]);
 	}

 	public static void testDatumDayNumber(Datum test1, Datum test2) {

 		
 		System.out.println(test1.dayNumber());
 		System.out.println(test1.differenceInDays(test2));
 		//System.out.println(Datum.fromDayNumber(test1.dayNumber()));
 	}

 	public static void testDatumWeekNumber(Datum test1, Datum test2) {

 		System.out.println(test1.weekNumber(1,0));
 	}

 	public static void testDatum() {

 		Datum test1 = new Datum("20-01-1992");
 		Datum test2 = new Datum("15-10-2015");
 		//Datum test1 = new Datum("27-08-1950");
 		//Datum test2 = new Datum("10-10-2013");

 		System.out.println(test1);
 		System.out.println(test2);
 		System.out.println();
 		

 		//testDatumMethods(test1, test2);
 		//testDatumDayNumber(test1, test2);
 		// System.out.println("DATUMDIFFERENCE:");
 		// testDifferenceDatum(test1, test2);
 		// System.out.println("DATUMWEEKNUMMER: ");
 		// testDatumWeekNumber(test1, test2);
 		System.out.println("WASONA TEST: ");
 		testWasOnA(test1, test2);
 	}
 }