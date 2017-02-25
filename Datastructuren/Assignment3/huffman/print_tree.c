#include <stdio.h>
#include "huffman.h"

int leaf(tree_t node) {
	if (node->left != 0 && node->right != 0)
	{
		return 1;
	}
	return 0;
}

void recursive_print(tree_t root) {

	if (root->left != 0) {
    	recursive_print(root->left);
    }
    if (root->right != 0) {
    	recursive_print(root->right);
    }

    if (root->letter != 0) {
    	printf("%c", root->letter);
    } else {
    	printf(".");
    }
}

void print_tree(tree_t root) {
    
    recursive_print(root);
    printf("\n");
}
