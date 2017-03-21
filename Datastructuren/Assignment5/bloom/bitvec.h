/* Florian Heringa
 * 10385835
 * bitvec.h
 */

#include <stddef.h>

/* NOTE: It is not guaranteed the values will stay the same. DO use the macro's!
 */
#define ARRAY_ILLEGAL_VARIABLE (2)
#define ARRAY_OUT_OF_BOUNDS (1)
#define ARRAY_OK (0)

typedef unsigned char bit;

struct vec;

/* Create a new vector which can hold `n` bits. It returns `NULL` if any errors
 * were encountered.
 *
 * The initial values of the array should set to zero.
 */
struct vec *bitvec_alloc(size_t n);

/* Release all memory allocated for a bitvector as allocated by `bitvec_alloc`.
 *
 * This means that `bitvec_free(bitvec_alloc(n))` does not have any effects
 * outside of memory shuffling.
 */
void bitvec_free(struct vec *vec);

/* Get the bit from `vec` at the given index `i`. The value is saved in `val`.
 *
 * If any errors are encountered a suitable `ARRAY_*` error should be returned
 * and `val` should not be altered. Otherwise `ARRAY_OK` should be returned.
 */
int bitvec_get(struct vec *vec, size_t i, bit *val);

/* Set the bit from `vec` at the given index `i` to `v`. `v` should be between 0
 * and 1, however this is not guaranteed by the compiler.
 *
 * If any errors are encountered a suitable `ARRAY_*` error should be returned
 * and `val` should not be altered. Otherwise `ARRAY_OK` should be returned.
 */
int bitvec_set(struct vec *vec, size_t i, bit v);
