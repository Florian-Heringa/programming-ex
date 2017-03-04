#include <stdio.h>
#include <stdlib.h>

#include "huffman.h"
#include "stack.h"

/* Florian Heringa
 * 10385835
 * 
 * load_tree.c
 * This file handles the conversion of a tree in rpn notation to an
 * actual tree data structure.
 */

/* Pushes characters from input sequentially onto the stack until a '.' is found
 * then pops and merges top two items and pushes them back onto the stack.
 * repeat until no more input characters remain (relies on \0 end of line).
 */
tree_t load_tree(char *input) {

	struct stack *s = stack_init();

	if (s == NULL) {
		fprintf(stderr, "Could not initialize stack...\n");
		exit(1);
	}
	tree_t node_1;
	tree_t node_2;
	tree_t final_tree = NULL;

    for (char *in = input; *in; in++) {
  	
    	if (*in == '.') {
    		node_1 = stack_pop(s);
    		node_2 = stack_pop(s);
    		if (node_1 == NULL || node_2 == NULL) {
    			fprintf(stderr, "Stack error; invalid tree representation...\n");
    			exit(1);
    		}
    		stack_push(s, make_tree(0, 0, node_2, node_1));
    		continue;
    	}

    	stack_push(s, make_tree(*in, 0, 0, 0));
    }

    /* Account for possible spaces at the end of the line which might have been
     * pushed onto the stack by accident */
    while (!stack_empty(s)) {
    	final_tree = stack_pop(s);
    }

    stack_cleanup(s);

    return final_tree;
}