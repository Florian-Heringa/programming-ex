/* Florian Heringa
 * 10385835
 * bloom.h
 */

#include <stddef.h>

#define MAYBE_IN_FILTER 0
#define NOT_IN_FILTER 1

struct bloom;

/* Initialises a bloom_filter struct with in it contained the
 * amount of hash functions used (k), the size of the bitvector 
 * (n) and pointers to a bitvector and an array of hash
 * function parameters
 */
struct bloom *init_bloom(size_t n, size_t k, void *ks[k]);

/* Frees bloom filter struct created by the init_bloom() function
 */
void free_bloom(struct bloom *filter);

/* Adds a word to the filter through use of hash function defined in
 * hash.c. Takes the hash function parameter array made by init_bloom
 * and sets the correct bits
 */
void bloom_add(struct bloom *filter, const size_t len, const char *value);

/* Checks if a word is definitely not in, or maybe in the filter. Returns
 * the values defined by the macro's depending on whether the character is
 * maybe in, or definitely not in the filter.
 */
int bloom_check(struct bloom *filter, const size_t len, const char *value);