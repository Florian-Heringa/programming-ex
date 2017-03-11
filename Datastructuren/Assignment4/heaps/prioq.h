#ifndef PRIOQ_H
#define PRIOQ_H

#include "array.h"

struct heap {
    struct array* array;
    // return a > b ? 1 : (a < b ? -1: 0)
    int (*compare)(const void*, const void*);
};

typedef struct heap prioq;

// return pointer to empty prioq on success, NULL on error
prioq* prioq_init(int (*compare)(const void*, const void*));

// return the size of priority queue.
int prioq_size(struct heap* h);

// return 0 on success, something else on error
int prioq_cleanup(prioq* h, void free_func(void*));

// return 0 on success, something else on error
int prioq_insert(prioq* h, void* p);

// return pointer to top element on success, NULL on error
void* prioq_pop(prioq* h);

#endif
