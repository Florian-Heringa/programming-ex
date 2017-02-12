#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

// ... SOME CODE MISSING HERE ...
// EXTRA FUNCTIONS

//Checks if the character is a valid operator
int isOperator(char c) {
    if (c == 40 || c == 41 || c == 42 || c == 43 || c == 45 || c == 47 || c == 61 || c == 94) {
      return 1;
    }
    return 0;
}

//Returns length of a string
int inputLength(char *str) {
    int i;
    for (i = 0; str[i] != '\0'; i++);
    return i;
}

//Checks the precedence of two operators with respect to each otherwise
//returns 1 if P[a] > P[b], -1 if P[a] < P[b] and 0 if P[a] = P[b]
int precedence(char op_a, char op_b) {}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: %s \"infix_expr\"\n", argv[0]);
        return 1;
    }

    char *input = argv[1];
    //find input length of expression, check if not empty
    int input_length = inputLength(input);

    if (input_length < 1) {
      return 1;
    }

    // Create stack structure, malloc output
    char* output = malloc(input_length);
    struct stack *s = stack_init();

    //loop init var
    char curChar = 0;
    int outputPos = 0;

    //loop through all input, run algorithm
    for (int i = 0; i < input_length; ++i) {
        curChar = input[i];

        if (isdigit(curChar)) {
          output[outputPos] = curChar;
          ++outputPos;
          continue;
        }
        if (isOperator(curChar)) {
          stack_push(s, curChar);
        }

        //output[i] = input[i];
    }

    while (!stack_empty(s)) {
        output[outputPos] = stack_pop(s);
        ++outputPos;
    }


    printf("Output: %s\n", output);


    //
    // printf("%d\n", getSize(s));     //size should be 0
    // stack_push(s, 6);               //push 6 onto stack
    // printf("%d\n", getSize(s));     //size should be 1
    // printf("%d\n", stack_peek(s));  //should print 6
    // printf("%d\n", stack_pop(s));   // should print 6
    // printf("%d\n", getSize(s));     // should print 0

    // ... SOME CODE MISSING HERE ...

    stack_cleanup(s);

    return 0;
}
