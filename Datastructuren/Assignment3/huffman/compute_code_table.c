#include <stdio.h>
#include <stdlib.h>
#include "huffman.h"

encoding_table_t* compute_code_table(tree_t root) {
    encoding_table_t *result = calloc(sizeof(encoding_table_t), 1);
    if (result == 0) {
        perror("calloc");
        exit(1);
    }

    // ... SOME CODE MISSING ...

    return result;
}

void free_code_table(encoding_table_t* table) {

    // ... SOME CODE MISSING ...

    free(table);
}
