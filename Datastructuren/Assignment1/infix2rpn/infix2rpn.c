#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

// ... SOME CODE MISSING HERE ...
// EXTRA FUNCTIONS

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: %s \"infix_expr\"\n", argv[0]);
        return 1;
    }

    char *input = argv[1];
    struct stack *s = stack_init();

    int input_length = sizeof(input) / sizeof(char);
    //printf("%d\n\n", input_length);
    char* output = malloc(input_length);

    for (int i = 0; i < input_length; ++i) {
        output[i] = input[i];
        //printf("akjbsf");
    }


    printf("%s\n", output);

    printf("%d\n", getSize(s));     //size should be 0
    stack_push(s, 6);               //push 6 onto stack
    printf("%d\n", getSize(s));     //size should be 1
    printf("%d\n", stack_peek(s));  //should print 6
    printf("%d\n", stack_pop(s));   // should print 6
    printf("%d\n", getSize(s));     // should print 0

    // ... SOME CODE MISSING HERE ...

    stack_cleanup(s);

    return 0;
}
