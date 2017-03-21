#include <stdio.h>
#include <stdlib.h>

#include "bitvec.h"
#include "hash.h"

/*
 * Initialize the hashfunc arguments using the given `k` command line arguments
 * `kv_args`. It returns these arguments converted in a fresh void pointer
 * array.
 */
void **init_ks(size_t k, char *kv_args[k])
{
    void **ks;

    if (!(ks = calloc(k, sizeof(void *)))) {
        return NULL;
    }

    for (size_t i = 0; i < k; i++) {
        long *param = malloc(sizeof(long));
        if (!param) {
            for (size_t i = 0; i < k; i++) {
                free(ks[i]);
            }
            free(ks);
            return NULL;
        }
        /* Set your param here. */
        *param = atol(kv_args[i]);
        ks[i] = param;
    }
    return ks;
}

int main(int argc, char *argv[])
{
    void **ks = NULL;
    size_t k, n;

    if (argc < 3 || (k = atol(argv[1])) < 1 || (argc - atoi(argv[1])) != 3) {
        printf("usage: %s <k> <n> <ks>\n", argv[0]);
        return 1;
    }

    n = atol(argv[2]);

    if (!(ks = init_ks(k, argv + 3))) {
        return EXIT_FAILURE;
    }

    /* GLORY AND HACKS AWAIT! */

    for (size_t i = 0; i < k; i++) {
        free(ks[i]);
    }
    free(ks);
    return EXIT_SUCCESS;
}
