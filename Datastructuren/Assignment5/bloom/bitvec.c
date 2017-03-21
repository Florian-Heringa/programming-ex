/* Florian Heringa
 * 10385835
 * bitvec.c
 */

#include <stdlib.h>
#include <stdint.h>

#include "bitvec.h"

#define INT_SIZE 32

/* The vec struct has two fields, one with the overall size of the array
 * (the amount of bits available) and the other with an array of 32-bit
 * integers. The only assumption in this implementation is that the machine
 * is capable of using 32-bit integers. In every integer there is a 
 * possibility of 32 bits that can be set using masking. This is explained 
 * in the respective functions
 */
struct vec {
	size_t size;
    int32_t *vec_ar;
};

/* Allocate an array of n bits where the bits are contained within 32-bit 
 * integers. This means that the array has (n / 32) + 1 elements.
 */
struct vec *bitvec_alloc(size_t n) {

	size_t num_elems = (n/INT_SIZE) + 1;

    struct vec *v;
    if (!(v = malloc(sizeof(struct vec)))) {
    	return NULL;
    }

    // Calloc ensures that all bits are set to 0
    if (!(v->vec_ar = calloc(num_elems, sizeof(int32_t)))) {	
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
 * created for bit i % INT_SIZE at index i / INT_SIZE into the bitvec
 * array of integers. Through application of the mask the correct bit
 * can be extracted.
 */
int bitvec_get(struct vec *vec, size_t i, bit *val) {

	if (i > vec->size) {
		return ARRAY_OUT_OF_BOUNDS;
	}

	// Create mask
    int index = i / INT_SIZE;
    int get_bit = i % INT_SIZE;
    int32_t mask = 1 << get_bit;

    // Apply mask and store at location 'val'
    *val = (vec->vec_ar[index] & mask) >> get_bit;

    return ARRAY_OK;
}

int bitvec_set(struct vec *vec, size_t i, bit v) {

	if (i > vec->size) {
		return ARRAY_OUT_OF_BOUNDS;
	}

	// Setup mask
    int index = i / INT_SIZE;
    int set_bit = i % INT_SIZE;
    int32_t mask = 1 << set_bit;

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