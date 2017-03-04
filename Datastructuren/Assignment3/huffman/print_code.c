#include <stdio.h>

#include "huffman.h"

/* Florian Heringa
 * 10385835
 * 
 * print_code.c
 * Prints a representation of a code struct to be used in Huffman encoding
 * A binary representation of the code is printed. An integer representation
 * of this code is stored in the struct and thus it is converted by this file
 * with the tobinstr function.
 */

/* Courtesy of: http://stackoverflow.com/questions/7911651/decimal-to-binary
 * slightly adapted to suit my needs. Converts an integer to a binary representation
 * used in the huffman code .
 */
void tobinstr(int value, int bitsCount, char* output) {

    int i;
    output[bitsCount] = '\0';
    for (i = bitsCount - 1; i >= 0; --i, value >>= 1) {
        output[i] = (value & 1) + '0';
    }
}

/* Prints the code, c, for a certain code struct. Which character belongs with
 * the code is printed in the print_code_table() function which calls this function.
 * returns the length of the code used. This is used to keep track of compression.
 */
int print_code(code c) {

    int length = c.code_length;
    int value = c.code_value;

    char str[length + 1];

    // Convert decimal saved value to binary representation.
    tobinstr(value, length, str);

    printf("%s", str);

    return length;
}

