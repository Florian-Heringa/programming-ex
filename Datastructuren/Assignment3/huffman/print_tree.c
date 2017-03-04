#include <stdio.h>
#include "huffman.h"

/* Florian Heringa
 * 10385835
 * 
 * print_tree.c
 * Uses recursion to print a binary tree structure.
 */

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
}
