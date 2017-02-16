#include <stdio.h>
#include <stdlib.h>

#include "list.h"

// ... SOME CODE MISSING HERE ...

struct list {
  struct node *root;
  int size;
  struct node *lastElement;
  struct node *firstElement;
};

struct node {
  int data;
  struct node *right_link;
  struct node *left_link;
};

struct list* list_init() {
  struct list *listInit;
  listInit = malloc(sizeof(listInit));
  return listInit;
}

int list_add(struct list *l, int num) {
  
}
