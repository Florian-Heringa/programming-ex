/* Handle to stack */
struct stack;
typedef struct stack Stack;

/* Return a pointer to a stack data structure.
 * Size of stack is fixed. */
struct stack *stack_init();

/* Cleanup stack.
 * Also prints the statistics. */
void stack_cleanup(struct stack* stack);

/* Push character onto the stack.
 * Return 0 if succesful, 1 otherwise. */
int stack_push(struct stack *stack, int c);

/* Pop character from stack and return it.
 * Return top character if succesful, -1 otherwise. */
int stack_pop(struct stack *stack);

/* Return top of item from stack. Leave stack unchanged.
 * Return top character if succesful, -1 if stack is empty. */
int stack_peek(struct stack *stack);

/* Return 1 if stack is empty, 0 otherwise. */
int stack_empty(struct stack *stack);
