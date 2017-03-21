/* Florian Heringa
 * 10385835
 * bloom.c
 *
 * Bloom filter API, makes use of bitvector API and hashfunction API
 * as defined in bitvec.c and hash.c. Creates a bloom struct which contains
 * all necessary ingredients for implementing a bloom filter wit the
 * init_bloom() function. For the working and testing of the bloom filter
 * see comments at the top of main.c.
 */

#include <stdlib.h>

#include "bitvec.h"
#include "hash.h"
#include "bloom.h" 

struct bloom {
	size_t k;
	size_t n;
	struct vec *bv;
	struct hashfunc *hf;
};

struct bloom *init_bloom(size_t n, size_t k, void *ks[k]) {

	struct bloom *new_filter;
	if (!(new_filter = malloc(sizeof(struct bloom)))) {
		return NULL;
	}

	new_filter->bv = bitvec_alloc(n);
	if ((!new_filter->bv)) {
		free(new_filter);
		return NULL;
	}

	new_filter->hf = hash_alloc(k, ks);
	if ((!new_filter->hf)) {
		bitvec_free(new_filter->bv);
		free(new_filter);
	}

	new_filter->n = n;
	new_filter->k = k;

	return new_filter;
}

int bloom_check(struct bloom *filter, const size_t len, const char *value) {

	size_t hash_val;
	bit val;
	size_t bits_set = 0;

	/* Run all k hash functions for the input and get the value at that hash
	 * if all values are set then the item may be in the filter, else it is 
	 * definitely not in the filter */
	for (size_t i = 0; i < filter->k; ++i) {
		hash_val = hash(filter->hf, i, len, value) % filter->n;
		bitvec_get(filter->bv, hash_val, &val);
		if (val) {
			bits_set++;
		}
	}

	if (bits_set == filter->k) {
		return MAYBE_IN_FILTER;
	}
	return NOT_IN_FILTER;
}

void bloom_add(struct bloom *filter, const size_t len, const char *value) {

	size_t hash_val;

    for (size_t i = 0; i < filter->k; ++i) {
    	hash_val = hash(filter->hf, i, len, value) % filter->n;
    	bitvec_set(filter->bv, hash_val, 1);
    }
}

void free_bloom(struct bloom *filter) {
	bitvec_free(filter->bv);
	hash_free(filter->hf);
	free(filter);
}