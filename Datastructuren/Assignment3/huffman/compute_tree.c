#include <stdlib.h>
#include <limits.h>

#include "huffman.h"

tree_t compute_tree(frequency_table_t* freqs) {

    // ... SOME CODE MISSING HERE ...

    tree_t first, second;
    int first_val = INT_MAX;
    int second_val = INT_MAX;

    //Find two lowest frequency nodes
    for (frequency_table_t *item; *item; item++) {

    	if (item->value < first_val) {
    		second_val = first_val;
    		second = first
    		first_val = item->value;
    		first = item
    	} else if (item->value < second_val) {
    		second = item->value
    		second = item
    	}
    }

    // Make new subnode
    make_tree(0, first_val + second_val, first, lowest);

    return 0;
}

void free_tree(tree_t tree) {
    if (!tree) {
        return;
    }
    free_tree(tree->left);
    free_tree(tree->right);
    free(tree);
}
