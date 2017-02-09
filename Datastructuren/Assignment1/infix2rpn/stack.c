#include <stdlib.h>
#include <stdio.h>

#include "stack.h"

#define STACK_SIZE 100

struct stack {
    // ... SOME CODE MISSING HERE ...
    int *stack;
    int size;
    int lastChar;
};

Stack *stack_init() {
    Stack* s = malloc(sizeof(struct stack));

    if (s == NULL) {
      return NULL;
    }

    s->stack = malloc(STACK_SIZE * sizeof(int));

    if (s->stack == NULL) {
      free(s);
      return NULL;
    }

    s->size = 0;
    s->lastChar = NULL;
    // ... SOME CODE MISSING HERE ...
    //printf("%d\n", s->size);
    return s;
}

int stack_push(struct stack* stack, int c) {
    //printf("stacksize = %d\n", stackSize);
    stack->stack[stack->size] = c;
    stack->size += 1;
    stack->lastChar = c;

    // TODO; conditional returns
    return 0;
}

int stack_pop(struct stack *stack) {
  if (stack_empty(stack)) {
    return -1;
  }
  int c = stack->lastChar;
  stack->size -= 1;
  stack->lastChar = stack->stack[stack->size];
  return c;
}

int stack_peek(struct stack *stack) {
  if (stack_empty(stack)) {
    return -1;
  }
  return stack->lastChar;
}

int stack_empty(struct stack *stack) {
  if (stack->size == 0) {
    return 1;
  }
  return 0;
}

void stack_cleanup(struct stack* s) {
    free(s);
}

// ... SOME CODE MISSING HERE ...
