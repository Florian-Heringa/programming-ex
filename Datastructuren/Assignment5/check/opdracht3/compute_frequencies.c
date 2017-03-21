#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>

#include "huffman.h"

frequency_table_t* compute_frequencies(char* input_data) {
    frequency_table_t *freqs = calloc(sizeof(frequency_table_t), 1);
    if (freqs == 0) {
        perror("calloc");
        exit(1);
    }

    for (char *p = input_data; *p; p++) {
        int c = (unsigned char)*p;

        if (!isprint(c) || c == SPECIAL_CHAR || c == '\n')
            // Special character: just ignore.
            continue;

        if ((*freqs)[c] == 0) {
            (*freqs)[c] = make_tree(c, 1, 0, 0);
        } else {
            (*freqs)[c]->value++;
        }
    }

    return freqs;
}
