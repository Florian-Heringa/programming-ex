#include <stdlib.h>
#include <stdio.h>

#include "stack.h"

#define STACK_SIZE 100

struct stack {
    // ... SOME CODE MISSING HERE ...
    int *stack;
    int top;
    int lastChar;
    int pops;
    int pushes;
    int maxDepth;
};

int getSize(struct stack* stack) {
    return stack->top;
}

struct stack* stack_init() {
    struct stack *s = malloc(sizeof(struct stack));

    if (s == NULL) {
      return NULL;
    }

    s->stack = malloc(STACK_SIZE * sizeof(int));

    if (s->stack == NULL) {
      free(s);
      return NULL;
    }

    s->top = -1;
    s->lastChar = 0;
    s->pops = 0;
    s->pushes = 0;
    s->maxDepth = 0;

    return s;
}

int stack_push(struct stack* stack, int c) {

    //Overflow detection
    if (stack->top >= 100) {
      return 1;
    }
    stack->top += 1;
    stack->stack[stack->top] = c;
    stack->lastChar = c;
    stack->pushes += 1;

    if (stack->top > stack->maxDepth) {
      stack->maxDepth = stack->top;
    }

    // TODO; conditional returns
    return 0;
}

int stack_pop(struct stack *stack) {

  if (stack_empty(stack)) {
    return -1;
  }

  int c = stack_peek(stack);

  // printf("c: %c\n", c);
  // printf("inhoud @ top before: %c\n", stack->stack[stack->top]);
  // printf("lastChar before: %c\n", stack->lastChar);
  // printf("top before: %d\n", stack->top);

  stack->top -= 1;
  stack->pops += 1;
  if (!stack_empty(stack)) {
    stack->lastChar = stack->stack[stack->top];
  } else {
    stack->lastChar = 0;
  }

  //printf("inhoud @ top after: %c\n", stack->stack[stack->top]);
  // printf("lastChar after: %c\n", stack->lastChar);
  // printf("topafter: %d\n\n", stack->top);

  return c;
}

//Returns -1 if stack is empty, top character otherwise.
int stack_peek(struct stack *stack) {
  if (stack_empty(stack)) {
    return -1;
  }
  return stack->lastChar;
}

//Returns 1 if stack is empty, 0 otherwise.
int stack_empty(struct stack *stack) {
  if (stack->top == -1) {
    return 1;
  }
  return 0;
}

//Print stats (amount of pushes, pops and max depth), and frees all memory.
void stack_cleanup(struct stack *stack) {
    printf("Stats %d %d %d\n", stack->pushes, stack->pops, stack->maxDepth);
    free(stack->stack);
    free(stack);
}

// ... SOME CODE MISSING HERE ...
