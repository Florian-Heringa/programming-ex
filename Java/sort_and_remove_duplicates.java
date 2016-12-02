/* Florian Heringa
* 10385835
* Writes 'Hallo!' to the terminal
*/

import java.util.Arrays;

public class sort_and_remove_duplicates {

	public static void main(String[] args) {

		int[] test = {1,1,1,4,3};
		int[] holder = new int[5];
		int[][] small = {{1,2,3,4}, {2,3,4,5}, {3,4,5,6}};
		int[][] big = {{1,2,3,4,5}, {2,3,4,5,6}};
		Boolean smallStraight = false;
		Boolean largeStraight = false;

		int n = 0;

		// Sort array
		Arrays.sort(test);

		// Set initial values
		int prev = test[n];
		int cur;
		holder[n] = test[n];

		// remove duplicates (save into holder)
		for (int i = 1; i < test.length; i++) {

			cur = test[i];
			if (cur == prev + 1) {
				n++;
				holder[n] = cur;
			}
			prev = cur;
		}

		// Compare with pattern for large or small
		for (int[] pattern : small) {
			if (Arrays.equals(Arrays.copyOfRange(holder, 0,4), pattern)) {
				smallStraight = true;
				break;
			}
		}

		for (int[] pattern : big) {
			if (Arrays.equals(Arrays.copyOfRange(holder, 0,5), pattern)) {
				largeStraight = true;
				break;
			}
		}

		printArr(holder);
		System.out.println(smallStraight);
		System.out.println(largeStraight);
	}

	public static void show(String a) {
		System.out.println(a);
	}

	public static void printArr(int[] arr) {

		for (int num : arr) {
			System.out.print(num);
		}

		System.out.println();
	}
}
