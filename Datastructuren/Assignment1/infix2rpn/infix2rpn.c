#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

typedef struct stack stack;

// ... SOME CODE MISSING HERE ...

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: %s \"infix_expr\"\n", argv[0]);
        return 1;
    }

    char *input = argv[1];
    stack *s = stack_init();

    printf("%d\n", s->size);


    //printf("%lu", sizeof(s));
    //int tst = s->size;
    //int tst2 = s->size;
    //printf("%d", tst);
    //printf("%d", tst2);
    // ... SOME CODE MISSING HERE ...

    stack_cleanup(s);

    return 0;
}
