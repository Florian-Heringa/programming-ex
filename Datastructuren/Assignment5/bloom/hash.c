/* Florian Heringa
 * 10385835
 * hash.c
 *
 * Hash API for use in a bloom filter. The function hash() contains the hash used
 * int his case combined with an external mixing function. The struct holds an 
 * array of parameters and potentially different seeds (currently constants) to use
 * in the hash. Currently the hash is quite simple but it seems to satisfy 
 * rudimentary calculation results. The struct might be changed to also allow
 * a function pointer giving a possibility of multiple different hash functions
 * within the hashfunction array.
 */

#include <stdlib.h>
#include <stdint.h>

#include "hash.h"
#include "bitvec.h"

/* Hash function seed, used for all hash functions, changing the
 * value seemingly had no effect on the performance of the filter.
 */
#define SEED 5381

struct hashfunc {
    size_t params;
    uint32_t seed;
};

/* This function createes a fresh hashfunc array  of size `k`. It is passed
 * an array, `ks` of `k` void pointers which serve as its arguments to
 * initialize the hash function with.
 *
 * On failure this function should return `NULL`.
 */
struct hashfunc *hash_alloc(size_t k, void *ks[k]) {
    
    struct hashfunc *hf_ar = calloc(k, sizeof(struct hashfunc));
    if (!hf_ar) {
    	return NULL;
    }

    for (size_t i = 0; i < k; ++i) {
    	struct hashfunc t;
    	t.params = *((long*)ks[i]);
    	t.seed = SEED;
    	hf_ar[i] = t;
    }

    return hf_ar;
}

/* Mixing stage for the used hash function */
size_t hash_mix(char c1, char c2, size_t hash, size_t shift) {

	size_t mix_var = (c1 + (c2 << (sizeof(char) * 4))) << shift;

	return hash ^ mix_var;
}

/* This function computes a hash for the given input `value` of length
 * `len` and the given index `f_index` in the given hashfunc array `f`. It
 * should return a single number, the hash of `value`. It is important that
 * this function returns the same value given the same input!
 */
size_t hash(struct hashfunc *f, const size_t f_index, const size_t len,
            const char value[len]) {

	struct hashfunc used = f[f_index];
	size_t hash = used.seed;
	size_t shift = used.params;
	char c_prev = 0;
	char c_now;
	
	/* Hash algorithm is djb2 base with an additional mixing step dedendent on
	 * consecutive characters in the input string and variable shift value. */
	for (size_t i = 0; i < len; ++i) {
		c_now = value[i];
		hash = ((hash << shift) + hash) + c_now; /* hash * (shift + 1) + c */
		hash = hash_mix(c_prev, c_now, hash, shift);
		c_prev = c_now;
	}
	
    return hash;
}

/* Free a given hashfunc array `f` allocated by the `hash_alloc` function.
 */
void hash_free(struct hashfunc *f) {
    free(f);
}
