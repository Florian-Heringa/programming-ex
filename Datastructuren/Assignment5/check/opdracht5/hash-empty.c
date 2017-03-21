#include "hash.h"

struct hashfunc {
    /* GLORY AND HACKS AWAIT! */
};

/*
 * This function should create a fresh hashfunc array  of size `k`. It is passed
 * an array, `ks` of `k` void pointers which serve as its arguments to
 * initialize the hash function with.
 *
 * On failure this function should return `NULL`.
 */
struct hashfunc *hash_alloc(size_t k, void *ks[k])
{
    /* GLORY AND HACKS AWAIT! */
    return NULL;
}

/*
 * This function should compute a hash for the given input `value` of length
 * `len` and the given index `f_index` in the given hashfunc array `f`. It
 * should return a single number, the hash of `value`. It is important that
 * this function returns the same value given the same input!
 */
size_t hash(struct hashfunc *f, const size_t f_index, const size_t len,
            const char value[len])
{
    /* GLORY AND HACKS AWAIT! */
    return 0;
}

/*
 * Free a given hashfunc array `f` allocated by the `hash_alloc` function.
 */
void hash_free(struct hashfunc *f)
{
    /* GLORY AND HACKS AWAIT! */
}
