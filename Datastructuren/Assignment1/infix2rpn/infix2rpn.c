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
    if (c == 40 || c == 41 || c == 42 || c == 43 || c == 45 || c == 47 || c == 61 || c == 94 || c ==70) {
      return 1;
    }
    return 0;
}

//returns precedence value of an operators
int precedenceVal(int operator) {

    printf("operator inside precedenceVal: %c\n", operator);

    switch(operator) {
      case 'F':
        return 5;
      case '~':
        return 4;
      case '^':
        return 3;
      case '/' + '*':
        return 2;
      case '+' + '-':
        return 1;
      default:
        return 0;
    }
}

//Returns length of a string
int inputLength(char *str) {
    int i;
    for (i = 0; str[i] != '\0'; i++);
    return i;
}

//Checks the precedence of two operators with respect to each otherwise
//returns 1 if P[a] > P[b], -1 if P[a] < P[b] and 0 if P[a] = P[b]
int precedence(int op_a, int op_b) {
    int prec_a = precedenceVal(op_a);
    int prec_b = precedenceVal(op_b);

    if (prec_a == prec_b) {
      return 0;
    } else if (prec_a > prec_b) {
      return 1;
    } else {
      return -1;
    }
}

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
    int stack_top = 0;
    int tempOp = 0;

    //loop through all input, run algorithm
    for (int i = 0; i < input_length; ++i) {

      curChar = input[i];
      printf("%d: %c\n", outputPos, curChar);

        //Checks if the character is a valid digit.
        if (isdigit(curChar)) {
          output[outputPos] = curChar;
          ++outputPos;
          continue;
        }

        //If the character is an operator, the perecednce is checked against the
        //top of the stack
        if (isOperator(curChar)) {

          if (stack_empty(s)) {
            stack_push(s, curChar);
            continue;
          }

          stack_top = stack_peek(s);

          //Checks if precedence of current char is higher than top of the stack,
          //and adds to the top if so. Else pops from the top of the stack until
          //precedence of top is lower.
          printf("curChar: %c, stack_top: %c\n", curChar, stack_top);
          printf("precedence: %d\n", precedence(curChar, stack_top));
          if (precedence(curChar, stack_top) == 1) {
            stack_push(s, curChar);
          } else {
            output[outputPos] = curChar;
            ++outputPos;
            tempOp = curChar;
            while (precedence(tempOp, stack_top) < 1 && !stack_empty(s)) {
              printf("lkansfd\n");
              output[outputPos] = stack_pop(s);
              tempOp = stack_top;
              stack_top = stack_peek(s);
              ++outputPos;
            }
          }
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

//TODO: Fix precedence function, not identifying correct precedence...
