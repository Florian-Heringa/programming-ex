#include <stdio.h>
#include <stdlib.h>

#include "huffman.h"

int decode_full(char*, encoding_table_t*);
char correct_code_found(int, int, encoding_table_t*);
int decode_part(char*, int*, char*, char*, encoding_table_t*);

/* Florian Heringa
 * 10385835
 * 
 * decode.c
 * Program uses auxiliary functions, described in huffman.h, to decode an
 * ASCII text encoded with the Huffman algorithm as described in encode.h.
 */

static
char* my_get_line(void) {
    char *line = 0;
    size_t n = 0;
    if (-1 == getline(&line, &n, stdin)) {
        perror("getline");
        exit(1);
    }
    return line;
}
/* Decodes the full input by calling the decode_part() function
 * repeatedly to decode the input in sequence.
 */
int decode_full(char *input, encoding_table_t *table) {

    int position = 0;
    char c = input[position];
    char decoded_letter;

    // Watch for end of line, defined by '.' character
    while (c != '.') {

        // Check for premature EOL.
        if(decode_part(input, &position, &c, &decoded_letter, table)) {
            return 1;
        }
        printf("%c", decoded_letter);

        // Keep track of position into input and next character.
        position += 1;
        c = input[position];
    }

    return 0;
}

/* If a character with the given code and code length is found, return that
 * character, else return 0.
 */
char correct_code_found(int code, int length, encoding_table_t *table) {

    // Walk through code table and check if there is a match
    for(int i = 0; i < 256; i++) {
        if ((*table)[i].code_length == length && 
            (*table)[i].code_value  == code   &&
            (*table)[i].code_length != 0        ) {
            return i;
        }
    }

    return 0;
}

/* Returns 1 if end of line has been reached, 0 if correct encoding
 * character has been found.
 */
int decode_part(char *input            , int *position,
                char *c                , char *decoded_letter,
                encoding_table_t *table) {

    // Value of the code in decimal representation as saved in code struct
    int code = 0;
    // length of the code, defaults to 1.
    int length = 1;

    while (1) {

        // Set value of code depending on 1 or 0 in input string.
        code = (code << 1) + (*c - '0');

        // Check if correct code has been found in table
        *decoded_letter = correct_code_found(code, length, table);

        // If found, return 0
        if (*decoded_letter) {
            return 0;
        }

        /* If not found, increment position into input array and use the new
         * character to find next value of code.
         */
        *position += 1;
        *c = input[*position];
        length += 1;

        if (*c == '.') {
            fprintf(stderr, "Premature line ending found, exiting..\n");
            return 0;
        }
    }

    return 0;
}


int main(void) {

    char *tree_line = my_get_line();
    char *coded_input = my_get_line();

    tree_t tree = load_tree(tree_line);

    if (tree == 0) {
        tree = fixed_tree();
    }

    // Make encoding table from input tree
    encoding_table_t *table = compute_code_table(tree);

    decode_full(coded_input, table);
    putchar('\n');

    // Cleanup.
    free(tree_line);
    free_tree(tree);
    free(coded_input);

    return 0;
}
