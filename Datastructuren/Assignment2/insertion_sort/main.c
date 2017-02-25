#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <ctype.h>

#include "list.h"

/* Author: Florian Heringa
 * Student id: 10385835
 *
 * This program reads a list from the standard input (numbers seperated by
 * whitespace characters) and saves it into a double linked list structure.
 * Then the list is sorted with an insertion sort algorithm and printed,
 * one character per line, to the standard output.
 */

struct config {
    // You can ignore these options until/unless you implement the
    // bonus features.

    // Set to 1 if -u is specified, 0 otherwise.
    int remove_duplicates;

    // Set to 1 if -S is specified, 0 otherwise.
    int add_sum;

    // Set to N if -s N is specified, 0 otherwise.
    int select_multiple;

    // Set to N if -x N is specified, 0 otherwise.
    int remove_multiple;

    // Set to N if -h N is specified, 0 otherwise.
    int show_first;

    // Set to N if -t N is specified, 0 otherwise.
    int show_last;

    // Set to 1 if -3 is specified, 0 otherwise.
    int scribble;
};

static
int parse_options(struct config *cfg, int argc, char *argv[]);

#define BUF_SIZE 1024
static char buf[BUF_SIZE];

//Check if two nodes are equal
int node_equals_main(struct list *l, struct node *n, struct node *m) {
  if (list_node_data(n) == list_node_data(m) &&
      list_next(n) == list_next(m) &&
      list_prev(l, n) == list_prev(l, m)) {
    return 1;
  }
  return 0;
}

int remove_duplicates(struct list *l) {

  struct node *check = list_head(l);
  struct node *compare = list_next(check);
  struct node *next;

  while (check != NULL) {

    while (compare != NULL) {

      if (list_node_data(check) == list_node_data(compare)) {
        next = list_next(compare);
        list_unlink_node(l, compare);
        list_free_node(compare);
        compare = next;
        continue;
      }

      compare = list_next(compare);
    }

    check = list_next(check);
  }

  return 0;
}

// Insertion sort algorithm
int insertion_sort(struct list *l, struct config cfg) {

  if (cfg.remove_duplicates) {
    remove_duplicates(l);
  }

  // Check if list is empty or just single element.
  if (list_length(l) == 0) {
    return 1;
  } else if (list_length(l) == 1) {
    return 1;
  }

  // Setup variables for use in loop
  struct node *to_sort = list_head(l);
  struct node *next;
  struct node *compare;

  //Option variables
  int sum = 0;

  // Loop until last element in linked list
  while (to_sort != NULL) {

    // Remember position in list (next) and unlink current node
    next = list_next(to_sort);
    list_unlink_node(l, to_sort);
    compare = list_head(l);
    if (compare == NULL) {
      break;
    }

    // Walk through list until comparison value gets higher than to_sort node
    while (list_node_data(to_sort) > list_node_data(compare)) {

      // Check for last item in list
      if (list_next(compare) == NULL) {
        break;
      }

      compare = list_next(compare);
    }

    if (cfg.add_sum) {
      sum += list_node_data(to_sort);
    }

    // Insert before last saved compare value and setup for next iteration
    list_insert_before(l, to_sort, compare);
    to_sort = next;
  }

  if (cfg.add_sum) {
    list_add_back(l, sum);
  }

  return 0;
}

/* Check if string is numeric, source:
   https://rosettacode.org/wiki/Determine_if_a_string_is_numeric#C */
int isNumeric (const char * s) {
  if (s == NULL || *s == '\0' || isspace(*s)) {
    return 0;
  }
  char *p;
  strtod (s, &p);
  return *p == '\0';
}

int main(int argc, char *argv[]) {

    struct config cfg;
    if (parse_options(&cfg, argc, argv) != 0)
        return 1;

    // Initialies linked list datastructure
    struct list *sorted_list = list_init();
    if (sorted_list == NULL) {
      return 1;
    }

    // Setup strtok for splitting input on whitespace
    const char delim[6] = " \t\r\n\f\v";
    char *token;

    // Read in a line
    while (fgets(buf, BUF_SIZE, stdin)) {

      if (*buf == 'q') {
        break;
      }

      // Split line on spaces
      token = strtok(buf, delim);

      while (token != '\0') {
        if (isNumeric(token)) {
          list_add(sorted_list, atoi(token));
        }
        token = strtok(NULL, delim);
      }
    }

    // Sort, print and clean up list
    insertion_sort(sorted_list, cfg);
    list_print(sorted_list);
    list_cleanup(sorted_list);

    return 0;
}

int parse_options(struct config *cfg, int argc, char *argv[]) {
    memset(cfg, 0, sizeof(struct config));
    int c;
    while ((c = getopt (argc, argv, "uSs:x:h:t:3")) != -1)
        switch (c) {
        case 'u': cfg->remove_duplicates = 1; break;
        case 'S': cfg->add_sum = 1; break;
        case '3': cfg->scribble = 1; break;
        case 's': cfg->select_multiple = atoi(optarg); break;
        case 'x': cfg->remove_multiple = atoi(optarg); break;
        case 'h': cfg->show_first = atoi(optarg); break;
        case 't': cfg->show_last = atoi(optarg); break;
        default:
            fprintf(stderr, "invalid option: -%c\n", optopt);
            return 1;
        }
    if (cfg->show_first != 0 && cfg->show_last != 0) {
        fprintf(stderr, "cannot specify both -h and -t\n");
        return 1;
    }
    return 0;
}
