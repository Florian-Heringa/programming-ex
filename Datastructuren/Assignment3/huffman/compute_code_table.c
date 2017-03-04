#include <stdio.h>
#include <stdlib.h>
#include "huffman.h"

/* Florian Heringa
 * 10385835
 * 
 * compute_code_table.c
 * This file handles calculation of a code table from an input Huffman tree,
 * the code table has entries for all ASCII characters with their code values
 * derived from the Huffman tree.
 */

/* Takes a pointer to an array of codes, an int num showing whether a 1 or 0
 * is part of the code an int called depth which is representative of the depth
 * into the tree and an int showing the current value of the code.
 */
int traverse_tree(tree_t root , encoding_table_t *result,
                   int num    , int depth,
                   int total) {

	int is_leaf = 1;
  // At every iteration, shift value and add 1 or 0 depending on left or right branch
	total = (total << 1) + (num);

	if (root->left != 0) {
		// If there is a left node, call function recursively, depth increased by 1
    	traverse_tree(root->left, result, 0, depth + 1, total);
    	is_leaf = 0;
    }
    if (root->right != 0) {
    	traverse_tree(root->right, result, 1, depth + 1, total);
    	is_leaf = 0;
    }

    if (is_leaf) {
      /* Relies on the fact that the encoding table is stored in sorted
       * ascii order, where c represents the index into tha array.*/
    	int c = root->letter;
    	(*result)[c].code_length = depth;
    	(*result)[c].code_value = total;
    }

    return 0;
}

encoding_table_t* compute_code_table(tree_t root) {
    encoding_table_t *result = calloc(sizeof(encoding_table_t), 1);
    if (result == 0) {
        perror("calloc");
        exit(1);
    }

    // Call recursive funtion to walk through tree
    traverse_tree(root, result, 0, 0, 0);

    return result;
}

void free_code_table(encoding_table_t* table) {

    /* No additional cleanup needed, since calloc already makes
     * structs inside array and code datatype has no pointer fields
     */
    free(table);
}