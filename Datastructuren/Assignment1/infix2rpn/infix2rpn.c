#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

// ... SOME CODE MISSING HERE ...
// EXTRA FUNCTIONS

//Converts to correct output format
char* toOutput(char* out, int outLength) {
  char *finalOut = malloc(2 * outLength * sizeof(char));
  int i;

  for (i = 0; i < outLength - 1; ++i) {
    finalOut[2 * i] = out[i];
    finalOut[2 * i + 1] = ' ';
  }
  finalOut[i + 2] = out[outLength - 1];
  finalOut[i + 3] = '\0';

  return finalOut;
}

//Checks if the character is a valid operator
int isOperator(char c) {
  if (c == 42 || c == 43 || c == 45 || c == 47 ||
      c == 61 || c == 94 || c ==70) {
    return 1;
  }
  return 0;
}

//Checks if a character is a left or right bracket
int isBracket(char c) {
  if (c == '('/*40*/) {
    return 1;
  } else if (c == ')' /*41*/) {
    return 2;
  }
  return 0;
}

//returns precedence value of an operators
int precedenceVal(int operator) {

  switch(operator) {
    case 'F':
      return 5;
    case '~':
      return 4;
    case '^':
      return 3;
    case '/':
    case '*':
      return 2;
    case '+':
    case '-':
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

    //Checks if the character is a valid digit.
    if (isdigit(curChar)) {
      output[outputPos] = curChar;
      ++outputPos;
      continue;
    }

    //If the character is an operator, the precedence is checked against the
    //top of the stack
    else if (isOperator(curChar)) {

      if (stack_empty(s)) {
        //Pushes value on to stack, detects if overflow occurs
        if (stack_push(s, curChar)) {
          return 1;
        };
        continue;
      }

      stack_top = stack_peek(s);

      //Checks if precedence of current char is higher than top of the stack,
      //and adds to the top if so. Else pops from the top of the stack until
      //precedence of top is lower.
      if (precedence(curChar, stack_top) == 1) {
        stack_push(s, curChar);
      } else {
        output[outputPos] = curChar;
        ++outputPos;
        tempOp = curChar;
        while (precedence(tempOp, stack_top) < 1 && !stack_empty(s)) {
          output[outputPos] = stack_pop(s);
          tempOp = stack_top;
          stack_top = stack_peek(s);
          ++outputPos;
        }
      }
    }
    else if (isBracket(curChar)) {
      if (isBracket(curChar) == 1) {
        stack_push(s, curChar);
      } else {
        while (isBracket(stack_peek(s)) != 1) {
          output[outputPos] = stack_pop(s);
          ++outputPos;
        }
      }
    }
    //Checks for invalid characters
    else if (curChar != ' ') {
      return 1;
    }
  }

  while (!stack_empty(s)) {
    printf("%c\n", stack_peek(s));
    output[outputPos] = stack_pop(s);
    ++outputPos;
  }

  char* finalOut = toOutput(output, outputPos);
  printf("%s\n", finalOut);

  stack_cleanup(s);

  return 0;
}

//TODO: fix adding to outputstring
//      fix bracketing
