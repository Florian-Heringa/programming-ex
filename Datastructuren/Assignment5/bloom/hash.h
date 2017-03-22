/* Florian Heringa
 * 10385835
 * hash.h
 */

 #include <stddef.h>

/* Hash function struct holding the parameter used for the seed of the hash
 * and for the shift value used in the hash. Currently the seed variable is
 * constant between different hashes, but it can easily be changed to use
 * different seeds per hash.
 */
struct hashfunc;

/* This function should create a fresh hashfunc array  of size `k`. It is passed
 * an array, `ks` of `k` void pointers which serve as its arguments to
 * initialize the hash function with.
 *
 * On failure this function should return `NULL`.
 */
struct hashfunc *hash_alloc(size_t k, void *ks[k]);

/* This function computes a hash for the given input `value` of length
 * `len` and the given index `f_index` in the given hashfunc array `f`. It
 * should return a single number, the hash of `value`. It is important that
 * this function returns the same value given the same input!
 */
size_t hash(struct hashfunc *f, const size_t f_index, const size_t len,
            const char value[len]);

/* Free a given hashfunc array `f` allocated by the `hash_alloc` function.
 */
void hash_free(struct hashfunc *f);