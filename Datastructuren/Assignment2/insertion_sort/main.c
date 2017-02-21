#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <ctype.h>

#include "list.h"

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

//Uses the insertion sort algorithm on an unsorted list datastructure
int insertion_sort(struct list *l) {

  if (list_length(l) == 0) {
    fprintf(stderr, "Empty list detected\n");
    return 1;
  }

  // Init node to sort and temporary cycle variables, start with value at head
  struct node *to_sort = list_head(l);
  struct node *compare;
  struct node *to_sort_next;

  int inserted_last = 0;

  //DEBUG variables
  int i = 0;
  int j = 0;

  printf("to_sort beginning: %d\n", list_node_data(to_sort));

  while (to_sort != NULL) {

    // Save position into list and unlink node to be sorted.
    to_sort_next = list_next(to_sort);
    list_unlink_node(l, to_sort);

    j = 0;

    // Set comparison point to first item in list.
    compare = list_head(l);

    printf("Iteration: %d, comp: %d, toSort: %d\n", i, list_node_data(compare),list_node_data(to_sort));

    /* As long as val(compare) is lower than val(to_sort) the to_sort node
       cannot be insterted. If the camper value is largest of the list, the if
       statement will take care of it. */
    while (list_node_data(compare) < list_node_data(to_sort)) {
      //printf("iteration: %d, inner while: %d\n", i, j);
      if (list_next(compare) == NULL) {
        list_insert_after(l, to_sort, compare);
        inserted_last = 1;
        break;
      }
      compare = list_next(compare);
      ++j;
    }

    if (inserted_last == 1) {
      inserted_last = 0;
      continue;
    }

    // Insert right after the last compared to node
    list_insert_after(l, to_sort, compare);

    // Set to_sort to the to_sort_next, this way
    to_sort = to_sort_next;

    ++i;
  }
  return 0;
}

void test_list(struct list *l) {

  //
  // struct list *l = list_init();
  // list_add(l, 10);
  // list_add(l, 26);
  // list_add(l, 107);
  // list_add(l, 12);
  // list_add(l, 1);
  //
  // list_print(l);
  //
  // struct node *three = list_next(list_next(list_head(l)));
  //
  // list_unlink_node(l, three);
  //
  // struct node *four = list_next(list_next(list_head(l)));
  //
  // list_insert_after(l, three, four);
  //
  // list_print(l);
  //
  // list_unlink_node(l, three);
  // list_insert_before(l, three, four);
  //
  // list_print(l);

  struct node *to_sort = list_head(l);
  struct node *next;
  struct node *compare;

  //printf("%d\n", list_node_data(to_sort));

  while (to_sort != NULL) {

    next = list_next(to_sort);
    list_unlink_node(l, to_sort);
    //printf("unlinked: %d\n", list_node_data(to_sort));
    compare = list_head(l);
    //printf("compare: %d\n", list_node_data(compare));

    //printf("Current list: \n");
    //list_print(l);


    while (list_node_data(to_sort) > list_node_data(compare)) {

      if (list_next(compare) == NULL) {
        break;
      }

      compare = list_next(compare);
    }


    list_insert_before(l, to_sort, compare);
    to_sort = next;

  }

  //printf("Final list: ");
  //list_print(l);

}

int main(int argc, char *argv[]) {
  //
  // test_list();
  //
  // exit(1);

    struct config cfg;
    if (parse_options(&cfg, argc, argv) != 0)
        return 1;

    // Initialies linked list datastructure
    struct list *sorted_list = list_init();
    if (sorted_list == NULL) {
      return 1;
    }

    //Setup strtok for splitting input on spaces
    const char delim[1] = " ";
    char *token;

    // Read in a line
    while (fgets(buf, BUF_SIZE, stdin)) {

      if (*buf == 'q') {
        break;
      }

      // Split line on spaces
      token = strtok(buf, delim);
      while (token != NULL) {
        list_add(sorted_list, atoi(token));
        token = strtok(NULL, delim);
      }
    }

    //list_print(sorted_list);
    test_list(sorted_list);
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
