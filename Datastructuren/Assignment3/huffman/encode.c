#include <stdio.h>
#include <stdlib.h>

#include "huffman.h"

/* Florian Heringa
 * 10385835
 * 
 * encode.c
 * Program uses auxiliary functions, described in huffman.h, to encode an
 * input text represented by ASCII characters with the Huffman encoding algorithm.
 * This is done by saving the data in a binary tree structure where the depth 
 * into the tree of a character is dependent on freqeuncy in the text. This tree 
 * can be interpreted as a code for any one character by, depending on left or
 * right branches, coding the character with a sequence of 0's or 1's.
 */

int main(int argc, char *argv[]) {
    char* input_data = read_data();

    // Select which tree to use.
    tree_t tree = 0;
    if (argc > 1) {
        tree = load_tree(argv[1]);
    } else {
        frequency_table_t* freqs = compute_frequencies(input_data);
        tree = compute_tree(freqs);
    }

    // Until load_tree and/or compute_tree is complete, fixed_tree()
    // returns a premade tree.
    if (tree == 0) {
        tree = fixed_tree();
    }

    // Show the tree being used.
    print_tree(tree);
    putchar('\n');

    // Convert the tree to a code table.
    encoding_table_t* table = compute_code_table(tree);

    int num_printed = 0;
    int num_encoded = 0;

    // Encode and print the encoded data.
    encode_data(table, input_data, &num_encoded, &num_printed);

    // Print the statistics.
    printf(".\n%d %d %d %d%%\n", num_encoded, count_tree(tree), num_printed,
            (int)(100. * (float)num_printed / (float)(num_encoded * 8)));

    // Cleanup.
    free(input_data);
    free_code_table(table);
    free_tree(tree);

    return 0;
}
