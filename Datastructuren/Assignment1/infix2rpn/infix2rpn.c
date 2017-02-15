#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#include "stack.h"

//Defining for code readability
#define LEFT_PARENTH 40
#define RIGHT_PARENTH 41
#define MULTIPLY 42
#define ADD 43
#define SUBTRACT 45
#define DIVIDE 47
#define EQUALS 61
#define FUNCTION 102
#define EXPONENT 94
#define NEGATE 126

//Checks if the character is a valid operator
int isOperator(char c) {
  if (c == MULTIPLY || c == ADD || c == SUBTRACT || c == DIVIDE ||
      c == EQUALS || c == EXPONENT || c == FUNCTION || c == NEGATE) {
    return 1;
  }
  return 0;
}

//Checks if a character is a left or right bracket
int isBracket(char c) {
  if (c == LEFT_PARENTH) {
    return 1;
  } else if (c == RIGHT_PARENTH) {
    return 2;
  }
  return 0;
}

//Returns precedence value of an operator
int precedenceVal(int operator) {

  switch(operator) {
    case FUNCTION:
      return 5;
    case NEGATE:
      return 4;
    case EXPONENT:
      return 3;
    case DIVIDE:
    case MULTIPLY:
      return 2;
    case ADD:
    case SUBTRACT:
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

//Checks the precedence of two operators with respect to eachother otherwise
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

//Run when bracket is detected.
int bracketRoutine(struct stack *s, char curChar) {

  if (isBracket(curChar) == 1) {
    stack_push(s, curChar);
  } else {
    while (isBracket(stack_peek(s)) != 1) {
      if (stack_empty(s)) {
        fprintf(stderr, "Incorrect bracketing detected.\n");
        exit(1);
      }
      printf("%c ", stack_pop(s));
    }
  }
  return 0;
}

int operatorRoutine(struct stack *s, char inputChar) {

  if (stack_empty(s)) {
    //Pushes value on to stack, detects if overflow occurs
    if (stack_push(s, inputChar)) {
      fprintf(stderr, "Stack underflow, invalid input\n");
      exit(1);
    };
    return 0;
  }

  char stack_top = stack_peek(s);

  //Checks if precedence of current char is higher than top of the stack,
  //and adds to the top if so. Else pops from the top of the stack until
  //precedence of top is lower.
  if (precedence(inputChar, stack_top) == 1) {
    stack_push(s, inputChar);
  }
  else {
    while (precedence(inputChar, stack_top) <= 0 && !stack_empty(s)) {
      printf("%c ", stack_pop(s));
      stack_top = stack_peek(s);
    }
    stack_push(s, inputChar);
  }
  return 0;
}

int main(int argc, char *argv[]) {

  if (argc != 2) {
    printf("usage: %s \"infix_expr\"\n", argv[0]);
    return 1;
  }

  char *input = argv[1];

  //find input length of input, check if not empty
  int input_length = inputLength(input);
  if (input_length < 1) {
    return 1;
  }

  // Create stack structure
  struct stack *s = stack_init();

  //Variables used inside loop
  char curChar = 0;

  //loop through all input, run algorithm
  for (int i = 0; i < input_length; ++i) {

    curChar = input[i];

    //Checks if the character is a valid digit.
    if (isdigit(curChar)) {
      printf("%c", curChar);
      while (isdigit(input[i+1])) {
        printf("%c", input[i+1]);
        ++i;
        curChar = input[i];
      }
      printf(" ");
      continue;
    }
    //If the character is an operator, the precedence is checked against the
    //top of the stack
    else if (isOperator(curChar)) {
      operatorRoutine(s, curChar);
    }
    else if (isBracket(curChar)) {
      bracketRoutine(s, curChar);
    }
    //Checks for invalid characters
    else if (curChar != ' ') {
      fprintf(stderr, "Invalid input character detected\n");
      return 1;
    }
  }

  while (!stack_empty(s)) {

    curChar = stack_pop(s);

    if (!isBracket(curChar)) {
      if (!stack_empty(s)) {
        printf("%c ", curChar);
      }
      else {
        printf("%c\n", curChar);
      }
    }
  }

  stack_cleanup(s);

  return 0;
}
