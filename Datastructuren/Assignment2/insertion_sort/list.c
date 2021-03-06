#include <stdio.h>
#include <stdlib.h>

#include "list.h"

/* Author: Florian Heringa
 * Student id: 10385835
 *
 * Implementation of a double linked list. Includes ways to get relevant
 * variables, print the list structure and to clean it up after use.
 */

struct list {
  int length;
  struct node *root;
  struct node *lastElement;
  struct node *firstElement;
};

struct node {
  int data;
  struct node *previous;
  struct node *next;
};

// Initialises a list datastructure
struct list* list_init() {

  struct list *listInit = malloc(sizeof(struct list));

  if (listInit == NULL) {
    return NULL;
  }

  listInit->root = NULL;
  listInit->length = 0;
  listInit->lastElement = NULL;
  return listInit;
}

// Initialises node datastructure
struct node* node_init(struct node *previous,
                       struct node *next,
                       int data) {

  struct node *new_node = malloc(sizeof(struct node));
  if (new_node == NULL) {
    return NULL;
  }

  new_node->previous = previous;
  new_node->next = next;
  new_node->data = data;

  return new_node;
}

/* Add item to the front of the list.
 * Return 0 if succesful, 1 otherwise. */
int list_add(struct list *l, int num) {

  // Base case for empty list, using same code as in list_add_back.
  if (l->length == 0) {
    if (list_add_back(l, num) == 0) {
      return 0;
    }
    return 1;
  }

  struct node *old_root = l->root;
  struct node *new_root = node_init(NULL, old_root, num);
  if (new_root == NULL) {
    return 1;
  }
  old_root->previous = new_root;
  l->root = new_root;
  l->length += 1;

  return 0;
}

/* Add item to the back of the list.
 * Return 0 if succesful, 1 otherwise. */
int list_add_back(struct list *l, int num) {

  //Base case for empty list
  if (l->length == 0) {
    struct node *new_node = node_init(NULL, NULL, num);
    if (new_node == NULL) {
      return 1;
    }
    l->root = new_node;
    l->lastElement = new_node;
    l->length = 1;
    return 0;
  }

  struct node *new_node = node_init(l->lastElement, NULL, num);
  if (new_node == NULL) {
    return 1;
  }
  l->lastElement->next = new_node;
  l->lastElement = new_node;
  l->length += 1;

  return 0;
}

// Checks if two nodes are equal
int node_equals(struct node *n, struct node *m) {
  if (n->data == m->data && n->next == m->next && n->previous == m->previous) {
    return 1;
  }
  return 0;
}

// Returns 1 (true) is n is in m, 0 (false) otherwise
int is_in_list(struct list *l, struct node *n) {

  struct node *temp = l->root;
  int found = 0;

  // Checks if node is in list.
  while (temp != NULL) {
    if (node_equals(n, temp)) {
      found = 1;
      break;
    }
    temp = temp->next;
  }

  if (!found) {
    return 0;
  }

  return 1;
}

/* Print list l, one number per line. */
void list_print(struct list *l) {

  struct node *temp = l->root;
  int i = 0;

  while (temp != NULL) {
    printf("%d\n", temp->data);
    temp = temp->next;
    ++i;
  }
}

/* Return the first node of the list or NULL is list is empty. */
struct node* list_head(struct list *l) {
  return l->root;
}

/* Return the data element of the list node. */
int list_node_data(struct node* n) {
  return n->data;
}

/* Return a pointer to the next node in the list or NULL if
 * 'n' is the last node in the list. */
struct node* list_next(struct node* n) {
  return n->next;
}

/* Return a pointer to the previous node in the list l or NULL if
 * 'n' is the first node in the list. */
struct node* list_prev(struct list* l, struct node* n) {

  if (!is_in_list(l, n)) {

  }

  return n->previous;
}

/* Unlink node n from list l.
 * Return 0 if n was succesfully removed from list l,
 * return 1 if an error unoccured during unlinking. */
int list_unlink_node(struct list* l, struct node* n) {

  struct node *n_minus_one = n->previous;
  struct node *n_plus_one = n->next;

  //Set root if unlinked node is first in list
  if (n_minus_one == NULL) {
    l->root = n_plus_one;
    n_plus_one->previous = NULL;
    n->next = NULL;
  }
  // Set last item if unlinked node was last in list
  else if (n_plus_one == NULL) {
    l->lastElement = n_minus_one;
    n_minus_one->next = NULL;
    n->previous = NULL;
  }
  // Else node was in the middle of the list
  else {
    n_minus_one->next = n_plus_one;
    n_plus_one->previous = n_minus_one;
    n->next = NULL;
    n->previous = NULL;
  }

  l->length -= 1;

  return 0;
}

/* Free node n. */
void list_free_node(struct node* n) {
  free(n);
}

/* Cleanup linked list data structure.
 * Return 0 if succesful, 1 otherwise. */
int list_cleanup(struct list *l) {

  struct node *to_be_removed = l->root;
  struct node *temp;

  while (to_be_removed != NULL) {
    temp = to_be_removed->next;
    free(to_be_removed);
    to_be_removed = temp;
  }

  free(l);
  return 0;
}

/* Insert node n after node m in list l. Node n must already be unlinked.
 * Return 0 if n was succesfully inserted, 1 otherwise.  */
int list_insert_after(struct list* l, struct node* n, struct node* m) {

  struct node *m_plus_one = m->next;
  m->next = n;
  n->previous = m;
  n->next = m_plus_one;

  /* If m is not the last element, just insert normally, else set new
     last element to n. */
  if (m_plus_one != NULL) {
    m_plus_one->previous = n;
  } else {
    l->lastElement = n;
  }

  l->length += 1;

  return 0;
}

/* Insert node n before node m in list l. Node n must already be unlinked.
 * Return 0 if n was succesfully inserted, 1 otherwise.  */
int list_insert_before(struct list* l, struct node* n, struct node* m) {

    struct node *m_minus_one = m->previous;
    m->previous = n;
    n->next = m;
    n->previous = m_minus_one;

    // Check if inserted at head, if not insert normally
    if (m_minus_one != NULL) {
      m_minus_one->next = n;
    } else {
      l->root = n;
    }

    l->length += 1;

    return 0;
}

/* Return list length. */
int list_length(struct list *l) {
  return l->length;
}
