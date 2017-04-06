#include <stdio.h>

#include "ast.h"
#include "front.h"

void my_free_tree(void *pt)
{
    free_tree((node_t *)pt);
}



void initialize(void)
{
    printf("%s\n", "Welcome to shello world!");
}

void run_command(node_t* node)
{
    // ...
    register_global_mem(node, &my_free_tree);
    // (for testing:)
    print_tree(node);

    // don't forget:
    free_from_global_mem(node);
}
