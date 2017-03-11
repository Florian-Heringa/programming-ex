#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "array.h"
#include "prioq.h"

#define INIT_SIZE 20

// Swap two items at index ia and ib of array ar
int swap(struct array* ar, int ia, int ib) {

	void *tmp = array_get(ar, ia);
	if (tmp == NULL) {
		return -1;
	}
    int chk = array_set(ar, ia, array_get(ar, ib));
    if (chk == -1) {
    	return chk;
    }
    chk = array_set(ar, ib, tmp);
    if (chk == -1) {
    	return chk;
    }
    return 0;
}

/* Creates a new heap structure, the first item is initialized
 * at NULL since this makes indexing more logical in the algorithms.
 * The array field is initialized with a dynamic-size array.
 * The compare function is dependent on what the heap will
 * be used for.
 */
static
struct heap* heap_init(int (*compare)(const void*, const void*)) {

	struct heap *new_heap = malloc(sizeof(struct heap));

	if (!new_heap) {
		return NULL;
	}

	// Array for heap->array field
    struct array *ar = array_init(INIT_SIZE);

    if (!ar) {
    	free(new_heap);
    	return NULL;
    }

    new_heap->array = ar;
    new_heap->compare = compare;

    // Set 0-index to NULL, makes indexing easier
    array_append(ar, NULL);

    return new_heap;
}

prioq* prioq_init(int (*compare)(const void*, const void*)) {
    return heap_init(compare);
}

// -1 because of indexing, see heap_init()
int prioq_size(struct heap* h) {
    return array_size(h->array) - 1;
}

static
int heap_cleanup(struct heap* h, void free_func(void*)) {
    array_cleanup(h->array, free_func);
    free(h);
    return 0;
}

int prioq_cleanup(prioq* h, void free_func(void*)) {
    heap_cleanup(h, free_func);
    return 0;
}

static
int heap_insert(struct heap* h, void* p) {

	struct array *ar = h->array;
    array_append(ar, p);

    // Get position of recently inserted item
    int cur_pos = prioq_size(h);
    // No need for algorithm if there is only one node
	if (cur_pos == 1) {
		return 0;
	}

	void* node = array_get(ar, cur_pos);
	void* parent = array_get(ar, cur_pos/2);

    while(h->compare(node, parent) < 1) {

    	swap(ar, cur_pos, cur_pos/2);
    	cur_pos /= 2;
    	node = array_get(ar, cur_pos);
    	parent = array_get(ar, cur_pos/2);
    	if (parent == NULL) {
    		break;
    	}
    }
    return 0;
}

int prioq_insert(prioq* h, void* p) {
    return heap_insert(h, p);
}

static
void* heap_pop(struct heap* h) {

    struct array *ar = h->array;
    int heap_size = prioq_size(h);

    /* Algorithm does not work for arrays with less than four items in it
     * before popping. Since indexing begins at 1 instead of 0 (see init),
     * ar_size = 1 means there are no items in the array.
     */
    if (heap_size == 0) {
    	return NULL;
    } else if (heap_size == 1) {
    	return array_pop(ar);
    } else if (heap_size == 2) {
    	swap(ar, 1, heap_size);
    	return array_pop(ar);
    } else if (heap_size == 3) {
    	swap(ar, 1, heap_size);
    	if (h->compare(array_get(ar, 1), array_get(ar, 2)) > -1) {
    		swap(ar, 1, 2);
    	}
    	return array_pop(ar);
    }

    //Swap last item with first item
    swap(ar, 1, heap_size);
    // Save last item in variable for return later
    void* popped = array_pop(ar);

    int i = 1;
    int chk;

    void* node = array_get(ar, i);
    void* left_child = array_get(ar, 2*i);
    void* right_child = array_get(ar, 2*i + 1);

    /* While the swapped node is higher value (dependent on compare function) 
     * than any of the child nodes, swap it with the least valued child node.
     */
    while (h->compare(node, left_child)  > -1 ||
    	   h->compare(node, right_child) > -1) {

    	if (h->compare(right_child, left_child) > -1) {
    		chk = swap(ar, i, 2*i);
    		if (chk != 0) {
    			return NULL;
    		}
    		i = 2*i;
    	}
    	else {
    		chk = swap(ar, i, 2*i + 1);
    		if (chk != 0) {
    			return NULL;
    		}
    		i = 2*i + 1;
    	}

    	if (2*i + 1 > prioq_size(h)) {
    		break;
    	}
    	// Redefine node positions
    	node = array_get(ar, i);
     	left_child = array_get(ar, 2*i);
     	right_child = array_get(ar, 2*i + 1);
    }

    return popped;
}

void* prioq_pop(prioq* h) {
    return heap_pop(h);
}
