/* Florian Heringa
 * 10385835
 * bitvec.c
 *
 * Bitvec API for use in a bloom filter, bitvector is implemented as
 * an array of 32-bit integers whose individual bits can be set or reset.
 * This potentially reduces space complexity by a maximum factor of about 
 * 32. Changing the value from 32 to 64 changes the integer size used from
 * 32 to 64 potentially reduscing the space complexity even more on 64-bit
 * machines. This does however need significant modification within the
 * type of hash functions used and has not been considered any further.
 */

#include <stdlib.h>
#include <stdint.h>
#include <limits.h>

#include "bitvec.h"

/* The vec struct has two fields, one with the overall size of the array
 * (the amount of bits available) and the other with an array of chars.
 * In the code is used that the size of a char is always known through
 * the macro CHAR_BIT, which holds the size of a character in bits.
 */
struct vec {
	size_t size;
    unsigned char *vec_ar;
};

/* Allocate an array of n bits where the bits are contained within chars
 * of size CHAR_BIT.
 */
struct vec *bitvec_alloc(size_t n) {

	size_t num_elems = (n/CHAR_BIT) + 1;

    struct vec *v;
    if (!(v = malloc(sizeof(struct vec)))) {
    	return NULL;
    }

    // Calloc ensures that all bits are set to 0
    if (!(v->vec_ar = calloc(num_elems, CHAR_BIT))) {	
    	free(v);
    	return NULL;
    }

    v->size = n;

    return v;
}

void bitvec_free(struct vec *vec) {
    free(vec->vec_ar);
    free(vec);
}

/* As long as the asked index is not outside of the array, a mask is
 * created for bit i % CHAR_BIT at index i / CHAR_BIT into the bitvec
 * array of integers. Through application of the mask the correct bit
 * can be extracted.
 */
int bitvec_get(struct vec *vec, size_t i, bit *val) {

	if (i > vec->size) {
		return ARRAY_OUT_OF_BOUNDS;
	}

	// Create mask
    int index = i / CHAR_BIT;
    int get_bit = (i % CHAR_BIT);
    unsigned char mask = 1 << get_bit;

    // Apply mask and store at location 'val'
    *val = (vec->vec_ar[index] & mask) >> get_bit;

    return ARRAY_OK;
}

int bitvec_set(struct vec *vec, size_t i, bit v) {

	if (i > vec->size) {
		return ARRAY_OUT_OF_BOUNDS;
	}

	// Setup mask
    int index = i / CHAR_BIT;
    int set_bit = (i % CHAR_BIT);
    unsigned char mask = 1 << set_bit;

    /* Possibility of setting a bit to 0 or 1, not used in
     * current implementation but might be useful for resetting
     * the filter during run-time.
     */
    if (v == 1) {
    	vec->vec_ar[index] |= mask;
    } else if (v == 0) {
        vec->vec_ar[index] &= ~(mask);
    } else {
    	return ARRAY_ILLEGAL_VARIABLE;
    }

    return ARRAY_OK;
}