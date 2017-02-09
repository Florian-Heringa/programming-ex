#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

// ... SOME CODE MISSING HERE ...

//typedef struct stack Stack;

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: %s \"infix_expr\"\n", argv[0]);
        return 1;
    }

    char *input = argv[1];
    Stack *s = stack_init();

    int input_length = sizeof(input) / sizeof(char);
    //printf("%d\n\n", input_length);
    char* output = malloc(input_length);

    for (int i = 0; i < input_length; ++i) {
        output[i] = input[i];
        //printf("akjbsf");
    }


    printf("%s\n", output);

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
