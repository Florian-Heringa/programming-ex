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

// Checks if two nodes are equal
int node_equals_main(struct list *l, struct node *n, struct node *m) {
  if (list_node_data(n) == list_node_data(m) &&
      list_next(n) == list_next(m) &&
      list_prev(l, n) == list_prev(l, m)) {
    return 1;
  }
  return 0;
}

//Uses the insertion sort algorithm on an unsorted list datastructure
int insertion_sort(struct list *l) {

  // Init node to sort and temporary cycle variables
  struct node *to_sort = list_head(l);
  struct node *compare;
  struct node *to_sort_next;

  //DEBUG variables
  int i = 0;

  printf("to_sort beginning: %d\n", list_node_data(to_sort));

  while (to_sort != NULL) {

    to_sort_next = list_next(to_sort);
    list_unlink_node(l, to_sort);

    compare = list_head(l);

    printf("Iteration: %d, comp: %d, toSort: %d\n", i, list_node_data(compare),list_node_data(to_sort));

    // while (list_node_data(to_sort) > list_node_data(compare)) {
    //   printf("Iteration: %d, comp: %d, toSort: %d\n", i, list_node_data(compare),list_node_data(to_sort));
    //   if (list_next(compare) == NULL) {
    //     break;
    //   }
    //   compare = list_next(compare);
    // }
    printf("List print:\n");
    list_print(l);

    list_insert_after(l, to_sort, compare);
    to_sort = to_sort_next;

    ++i;
  }
  return 0;
}

void test_list() {

  struct list *l = list_init();
  list_add(l, 10);
  list_add(l, 16);
  list_add(l, 107);
  list_add(l, 12);
  list_add(l, 1);

  list_print(l);

  struct node *three = list_next(list_next(list_head(l)));
  printf("%d\n", list_node_data(three));

  list_unlink_node(l, three);

  struct node *four = list_next(list_next(list_head(l)));

  list_print(l);

  list_insert_after(l, three, four);

  list_print(l);

}

int main(int argc, char *argv[]) {

  //test_list();

    struct config cfg;
    if (parse_options(&cfg, argc, argv) != 0)
        return 1;

    // Initialies linked list datastructure
    struct list *sorted_list = list_init();
    if (sorted_list == NULL) {
      return 1;
    }

    while (fgets(buf, BUF_SIZE, stdin)) {

      // Skip over non-digit input lines
      if (!isdigit(*buf)) {
        //Exit condition for debug
        if (*buf == 'q') {
          break;
        }
        fprintf(stderr, "Non-integer input detected: -%s-\n", buf);
        continue;
      }

      list_add(sorted_list, atoi(buf));
    }

    list_print(sorted_list);
    insertion_sort(sorted_list);
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
