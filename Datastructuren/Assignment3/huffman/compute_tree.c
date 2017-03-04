#include <stdlib.h>
#include <limits.h>
#include <stdio.h>

#include "huffman.h"

/* Florian Heringa
 * 10385835
 * 
 * compute_tree.c
 * Takes an input of frequency data representation of an ASCII-text and
 * converts this to an optimal Huffman tree.
 */

/* Seperates array of nodes into useful values (value != 0) and non useful 
 * values (value = 0). Returns index of last element that is not zero, also 
 * interpretable as last element in list
 */
int sort_freqs(frequency_table_t *freqs) {

    int low = 0;
    int high = 255;
    tree_t temp;

    while (low <= high) {

        // Loop from beginning until a zero-value node is found
        while ((*freqs)[low] && low <= high) {
            ++low;
        }
        // Loop from end until zero-valued node is found
        while (!(*freqs)[high] && low <= high) {
            --high;
        }

        // Exchange nodes found in previous loops
        if (low < high) {
            temp = (*freqs)[low];
            (*freqs)[low] = (*freqs)[high];
            (*freqs)[high] = temp;
        }
    }

    // Return index of last non-zero-valued element.
    return high;
}

int merge_lowest(frequency_table_t *freqs, int *index) {

    if (*index < 2) {
        return 1;
    }

    // Variables to keep track of lowest-valued elements and their index
    tree_t first;
    int first_i = 0;
    int first_val = INT_MAX;

    tree_t second;
    int second_i = 0;
    int second_val = INT_MAX;

    for (int i = 0; i <= *index; i++) {

        if ((*freqs)[i]->value < first_val) {
            second = first;
            second_i = first_i;
            second_val = first_val;
            first = (*freqs)[i];
            first_i = i;
            first_val = (*freqs)[i]->value;
        } else if ((*freqs)[i]->value < second_val) {
            second = (*freqs)[i];
            second_i = i;
            second_val = (*freqs)[i]->value;
        }
    }

    int new_value = first->value + second->value;

    tree_t new_node = make_tree(0, new_value, first, second);

    (*freqs)[first_i] = new_node;
    (*freqs)[second_i] = (*freqs)[*index];
    (*freqs)[*index] = NULL;

    (*index)--; 

    return new_value;
}

/* Sorts an input array of frequency data as descibed in sort_freqs(),
 * then walks through the array and merges the two lowest frequency nodes
 * and put them back into the array. This is done until only a single node
 * remains, which represents the root of the optimal Huffman tree.
 */
tree_t compute_tree(frequency_table_t *freqs) {

    // Save index of last useful element of 'sorted' array in i
    int i = sort_freqs(freqs);
    int loop_range = i;

    for (int j = 0; j < loop_range; j++) {
        merge_lowest(freqs, &i);
    }

    /* Correct with last merge and return. New total frequence
     * does not need to be calculated since it is not relevant
     */
    return make_tree(0, 0, (*freqs)[0], (*freqs)[1]); 
}

void free_tree(tree_t tree) {
    if (!tree) {
        return;
    }
    free_tree(tree->left);
    free_tree(tree->right);
    free(tree);
}
