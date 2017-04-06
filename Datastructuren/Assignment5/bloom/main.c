/* Florian Heringa
 * 10385835
 * main.c
 *
 * This program implements a bloom filter, in a bloom filter text lines are read
 * by the program. These words are then checke and classified by whether they
 * have been read before, this way it is possible to check if a word or line of
 * text has already occured in the input text.
 * This is done by calling one or more hashfunctions on the inupt text and then
 * setting bits corresponding to the values of these hashes in a bitvector. By
 * checking if bits are already set for every new word, we can check if a word
 * has already been seen.
 * The disadvantage of a bloom filter is that it can't be 100% sure if something
 * has already been seen, the only thing that can be said for sure is if the word 
 * has NOT been seen before. This is because of the finite amount of bits available
 * in the bitvector and some bits may already be set by other words. Because of this
 * it is very important that there is a careful balance between the size of the
 * bitvector, the amount of hashfunctions used and the expected size of the input.
 * This particular implementation was tested with the t2.txt file available at
 * https://gitlab-fnwi.uva.nl/informatica/datastructuren2017-checks
 * with input parameters: ./dups 10 10000 1 17 15 18 27 3 31 5 22 31
 * which means that 10 hashfunctions have been used and that the bitvector has a size
 * of 10000 bits (or [10000 / 32] 32-bit integers with this implementation).
 * A bloom filter calculator, http://hur.st/bloomfilter, has been used to find the
 * approximate values for the input size of 1000 elements, which came to 9,586 bits
 * and 7 hashfunctions as ideal values. The values above have been obtained by 
 * manually tweaking and checking what works.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "bitvec.h"
#include "hash.h"
#include "bloom.h"

// Maximum of 1024 characters per line
#define BUF_SIZE 1024

static char buf[BUF_SIZE];

/* Initialize the hashfunc arguments using the given `k` command line arguments
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

/* Takes a value k which determines the amount of hashes used in the algorithm
 * The value of n determines the amount of bits available for the filter,
 * finally ks[k], or <ks> takes values between 0 and 32 which are used
 * in the hash function to determine the shift (see function in hash.c)
 */
int main(int argc, char *argv[]) {
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

    struct bloom *bloom_filter;

    if (!(bloom_filter = init_bloom(n, k, ks))) {
        return EXIT_FAILURE;
    }

    size_t read = 0;
    size_t printed = 0;
    int not_in_filter;
    size_t len = 0;

    while (1) {
        char *s = fgets(buf, BUF_SIZE, stdin);

        if (s == NULL) {
            /* If end of input is found then print number of
             *read and printed strings */
            fprintf(stderr, "%d %d\n", (int)read, (int)printed);
            break;
        }

        // Remove trailing newline and find length of new string
        s = strtok(s, "\n");

        /* Strtok returns null pointer if line only contained a newline
         * character this resets the string to an empty one if that happens
         */
        if (!s) { s = ""; }
        len = strlen(s);

        // Check if the word is in the filter
        not_in_filter = bloom_check(bloom_filter, len, s);

        // If the string is not contained in the filter, add it and print
        if (not_in_filter) {
            printf("%s\n", s);
            bloom_add(bloom_filter, len, s);
            printed++;
        }

        len = 0;
        read++;
    }

    for (size_t i = 0; i < k; i++) {
        free(ks[i]);
    }
    free(ks);
    return EXIT_SUCCESS;
}